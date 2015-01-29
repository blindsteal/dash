package de.livinglab.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.vaadin.spring.navigator.SpringViewProvider;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

@Configuration
public class AppConfig {
	
	private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
	
	@Autowired
	Environment env;
    
    @Bean
    public EventBus eb(){
    	return new EventBus();
    }
    
    @Bean
    public RestTemplate client(){
    	return new RestTemplate();
    }
    
    @Bean
    @DependsOn("eb")
    public DataService dataService(){
    	DataService service = new DataService();
    	return service;
    }
    
    @Bean
    public ServiceManager manager(DataService dataService){
    	ServiceManager manager = new ServiceManager(Lists.newArrayList(dataService));
    	manager.addListener(new Listener(){
    		public void stopped() {
            	log.info("Services stopped");}
            public void healthy() {
            	log.info("Services healthy");
            }
            public void failure(Service service) {
            	log.info("Service "+service.getClass().toString()+" failed");
            }
    	});
        manager.startAsync();
    	return manager;
    }
}