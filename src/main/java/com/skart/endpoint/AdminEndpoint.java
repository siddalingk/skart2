package com.skart.endpoint;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skart.entity.Article;
import com.skart.service.IArticleService;

@Component
@Path("/admin")
public class AdminEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(ArticleEndpiont.class);
	@Autowired
	private IArticleService articleService;

	@GET
	@Path("/details")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticleDetails() {
		List<Article> list = articleService.getAllArticles();
		return Response.ok(list).build();
	}

}
