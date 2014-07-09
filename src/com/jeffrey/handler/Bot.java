package com.jeffrey.handler;

import org.wikibrain.conf.ConfigurationException;
import org.wikibrain.conf.Configurator;
import org.wikibrain.core.cmd.Env;
import org.wikibrain.core.cmd.EnvBuilder;
import org.wikibrain.core.dao.DaoException;
import org.wikibrain.core.lang.Language;
import org.wikibrain.core.model.LocalLink;
import org.wikibrain.sr.MonolingualSRMetric;

public class Bot {
	
	private String target;
	
	private Env env;
	private Configurator conf;
	private Language simple;
	private MonolingualSRMetric sr;
	
	public Bot(String target) throws ConfigurationException {
		this.target = target;
		
		env = new EnvBuilder().build();
		conf = env.getConfigurator();
		sr = conf.get(
                MonolingualSRMetric.class, "ensemble",
                "language", simple.getLangCode());
	}
	
	public double get(String phrase1, String phrase2) throws DaoException {
		return sr.similarity(phrase1, phrase2, false).getScore();
	}
	
	public LocalLink pick(Iterable<LocalLink> links) throws DaoException {
		LocalLink choice = null;
		double highScore = 0.0;
		for(LocalLink link : links) {
			if(get(link.getAnchorText(), target) > highScore) {
				choice = link;
			}
		}
		return choice;
	}
}
