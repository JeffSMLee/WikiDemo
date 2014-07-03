package com.jeffrey.wikidemo;

import java.io.File;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.jxpath.ri.model.beans.LangAttributePointer;
import org.wikibrain.core.lang.Language;

import com.jeffrey.handler.WikiBrainHandler;


@Path("/language/")
public class LanguageDemo {
	
	//private static WikiBrainHandler wbHandler = WikiBrainHandler.getInstance();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response readMe() {
		File f = new File("C:\\Users\\Jeffrey\\Dropbox\\WebDev\\restful\\com.jeffrey.wikidemo\\WebContent\\readme.html");
		return Response.ok(f).build();
	}
	
	@POST
	@Path("json")
	@Produces(MediaType.TEXT_PLAIN)
	public String getJSON(String data) {
		//return wbHandler.getLanguagesWithArticle(Language.SIMPLE, data, false);
		return "";
	}
	
	@POST
	@Path("plaintext")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPlainText(String data) {
		//return wbHandler.getLanguagesWithArticle(Language.SIMPLE, data, false);
		return "en sw la";
	}
}
