package com.jeffrey.handler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.wikibrain.conf.ConfigurationException;
import org.wikibrain.conf.Configurator;
import org.wikibrain.core.cmd.Env;
import org.wikibrain.core.cmd.EnvBuilder;
import org.wikibrain.core.dao.DaoException;
import org.wikibrain.core.dao.LocalLinkDao;
import org.wikibrain.core.dao.LocalPageDao;
import org.wikibrain.core.dao.UniversalArticleDao;
import org.wikibrain.core.dao.UniversalPageDao;
import org.wikibrain.core.lang.Language;
import org.wikibrain.core.lang.LanguageSet;
import org.wikibrain.core.model.LocalLink;
import org.wikibrain.core.model.LocalPage;
import org.wikibrain.core.model.NameSpace;
import org.wikibrain.core.model.Title;
import org.wikibrain.core.model.UniversalArticle;
import org.wikibrain.core.model.UniversalPage;

public class WikiBrainHandler {
	private static WikiBrainHandler instance;
	private Env env;
	private Configurator conf;
	private LocalPageDao lpDao;
	private LocalLinkDao llDao;
	private UniversalPageDao upDao;
	
	private LanguageSet langs;
	
	public static final String FORMATCODE_JSON = "json";
	public static final String FORMATCODE_PLAINTEXT = "plaintext";
	
	{
		try {
			env = new EnvBuilder().build();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conf = env.getConfigurator();
		try {
			lpDao = conf.get(LocalPageDao.class);
			llDao = conf.get(LocalLinkDao.class);
			upDao = conf.get(UniversalPageDao.class);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private WikiBrainHandler() {
		langs = new LanguageSet("en,la,sw");
	}
	
	public static WikiBrainHandler getInstance() {
		if(instance == null) {
			instance = new WikiBrainHandler();
		}
		return instance;
	}
	
	public void setLangs(LanguageSet langs) {
		this.langs = langs;
	}
	
	private LocalPage getLocalPageByTitle(String title, Language lang) {
		try {
			return lpDao.getByTitle(new Title(title, lang), NameSpace.ARTICLE);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean hasArticle(Language nativeLang, Language targetLang, String title) throws DaoException{
		UniversalPage page = upDao.getByLocalPage(getLocalPageByTitle(title, nativeLang), 1);
		if(page.getLocalId(targetLang) == -1) {
			return false;
		}
		return true;
	}
	
	
	//http://af.wikipedia.org/wiki/Appel
	private String getURL(String title, Language lang) {
		String[] urlElements = getLocalPageByTitle(title, lang).getCompactUrl().split("/");
		return "http://" + urlElements[2] + ".wikipedia.org/wiki/" + urlElements[4];
	}
	
	public String getLanguagesWithArticle(Language nativeLang, 
										  String title, 
										  //String formatCode,
										  boolean includeURL) throws DaoException {
		List<String> langsWithArticle = new ArrayList<String>();
		
		for (Language lang : langs) {
			if(hasArticle(nativeLang, lang, title)) {
				langsWithArticle.add(lang.toString() + " " +
						(includeURL ? getURL(title, lang) : ""));
			}
		}
		
//		switch (formatCode) {
//		case FORMATCODE_JSON:
//			try {
//				return new JSONObject().put("languages", langsWithArticle).toString();
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return "(None)";
//			}
//			break;
//		}
		
		
		return langsWithArticle.size() == 0 ? "(None)" : langsWithArticle.toString();
	}
	
	public Iterable<LocalLink> getLinks(LocalPage page) throws DaoException {
		return llDao.getLinks(page.getLanguage(), page.getLocalId(), true);
	}
	
	public LanguageSet getLangs() {
		return langs;
	}
}