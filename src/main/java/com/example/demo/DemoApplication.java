package com.example.demo;

import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
public class DemoApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	public void onStartup(ServletContext servletContext) throws ServletException{
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
    }
	
	@Bean
	  public EmbeddedServletContainerFactory servletContainer() {

	    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

	        @Override
	        protected void postProcessContext(Context context) {

	          SecurityConstraint securityConstraint = new SecurityConstraint();
	          securityConstraint.setUserConstraint("CONFIDENTIAL");
	          SecurityCollection collection = new SecurityCollection();
	          collection.addPattern("/*");
	          securityConstraint.addCollection(collection);
	          context.addConstraint(securityConstraint);
	        }
	    };
	    tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
	    return tomcat;
	  }

	  private Connector initiateHttpConnector() {

	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    connector.setScheme("http");
	    connector.setPort(80);
	    connector.setSecure(false);
	    connector.setRedirectPort(443);
	    return connector;
	  }
}
