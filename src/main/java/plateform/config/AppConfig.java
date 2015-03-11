package plateform.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import plateform.rs.JaxRsApiApplication;
import plateform.rs.PeopleRestService;
import plateform.services.PeopleService;
import res.DirResource;
import res.HelloWorldResource;

@Configuration
public class AppConfig {	
	@Bean( destroyMethod = "shutdown" )
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	@Bean @DependsOn( "cxf" )
	public Server jaxRsServer() {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
		
		List<Object> serviceBeans = new ArrayList<Object>();
//		serviceBeans.add(peopleRestService());
		serviceBeans.add(new HelloWorldResource());
		serviceBeans.add(new DirResource());
		
		
		factory.setServiceBeans(serviceBeans);
		factory.setAddress( "/" + factory.getAddress() );
		factory.setProviders( Arrays.< Object >asList( jsonProvider() ) );
		return factory.create();
	}
	
	@Bean 
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}
	
	@Bean 
	public PeopleRestService peopleRestService() {
		return new PeopleRestService();
	}
	
	@Bean 
	public PeopleService peopleService() {
		return new PeopleService();
	}
		
	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}