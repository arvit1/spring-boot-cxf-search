package com.massoudafrashteh.code.starter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.massoudafrashteh.code.controller.PeopleRestService;
import com.massoudafrashteh.code.controller.UserController;
import com.massoudafrashteh.code.domain.Person;
import com.massoudafrashteh.code.domain.User;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.search.SearchContextProvider;
import org.apache.cxf.jaxrs.ext.search.odata.ODataParser;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.jaxrs.ext.search.fiql.FiqlParser;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Server rsServer() {
        final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setProvider(new JacksonJsonProvider());
        factory.setBus(bus);
        factory.setAddress("/");
        factory.setServiceBeans(Arrays.<Object>asList(userController(),peopleRestService()));
        factory.setFeatures(Arrays.asList(new Swagger2Feature()));
        factory.setProvider( new SearchContextProvider() );
        factory.setProvider( new JacksonJsonProvider() );

        return factory.create();
    }

    @Bean
    public UserController userController() {
        return new UserController();
    }

    @Bean
    public PeopleRestService peopleRestService() {return new PeopleRestService();}

    //	 The default address of CXF RESTfull API is /services to change the API
    // sub-directory from /services with /api or anything that you like
    @Bean
    public ServletRegistrationBean cxfServlet() {
        final ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new CXFServlet(), "/api/*");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }
}
