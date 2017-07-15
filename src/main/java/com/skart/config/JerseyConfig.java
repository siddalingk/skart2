package com.skart.config;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.skart.endpoint.AdminEndpoint;
import com.skart.endpoint.ArticleEndpiont;

@Component
@ApplicationPath("/skart-app")
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		register(ArticleEndpiont.class);
		register(AdminEndpoint.class);
	}
}
