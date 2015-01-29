package de.livinglab.dashboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;

import de.livinglab.dashboard.events.LoadComplete;
import de.livinglab.dashboard.events.LoadRequest;

public class DataService extends AbstractScheduledService {
	private static final Logger log = LoggerFactory.getLogger(DataService.class);

	@Autowired
	Environment env;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	RestTemplate client;
	
	List<LoadRequest> toLoad;
	Map<LoadRequest, Object> loaded;

	class LoadRecorder{
		@Subscribe
		public void recordLoadRequest(LoadRequest req){
			toLoad.add(req);
			log.info(req.toString());
		}
		@Subscribe
		public void recordLoadComplete(LoadComplete event){
			log.info(event.toString());
		}
		
	}
	
	public DataService() {
		toLoad = Lists.newCopyOnWriteArrayList();
		loaded = Maps.newHashMap();
	}
	
	@Override
	protected void startUp() throws Exception {
		super.startUp();
		eb.register(new LoadRecorder());
	}

	@Override
	protected void runOneIteration() throws Exception {
		loadDocs(toLoad);
	}

	private void loadDocs(List<LoadRequest> reqs) {
		for(LoadRequest req : reqs){
				loadResource(req);
		}
	}

	private void loadResource(LoadRequest req) {
		HttpHeaders headers = new HttpHeaders();
		
		if(req.getType()==LoadRequest.ResourceType.PLAIN_XML){
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		}
		else if(req.getType()==LoadRequest.ResourceType.PLAIN_JSON){
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		}
		else if(req.getType()==LoadRequest.ResourceType.BASICAUTH_XML){
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
			String plainCreds = env.getProperty("jbpm.credentials");
			String base64Creds = new String(Base64.encodeBase64(plainCreds.getBytes()));
			headers.add("Authorization", "Basic " + base64Creds);
		}
		else{
			headers.setAccept(Arrays.asList(MediaType.ALL));
		}
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<?> ret = client.exchange(req.getUri(), HttpMethod.GET, entity, req.getReturnType());
		
		loaded.put(req, ret.getBody());
		if(!req.isRefresh()) toLoad.remove(req);
		eb.post(new LoadComplete(req));
	}
	
	public boolean isLoaded(String uri){
		for(LoadRequest req : loaded.keySet()){
			if(req.getUri().equals(uri)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isLoaded(String uri, Class<?> clazz){
		for(LoadRequest req : loaded.keySet()){
			if(req.getUri().equals(uri) && req.getReturnType() == clazz){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getResource(LoadRequest req, Class<T> clazz){
		return (T) loaded.get(req);
	}

	@SuppressWarnings("unchecked")
	public <T> T getResource(String uri, Class<T> clazz){
		for(LoadRequest req : loaded.keySet()){
			if(req.getUri().equals(uri)){
				return (T) loaded.get(req);
			}
		}
		return null;
	}

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, Integer.parseInt(env.getProperty("polling")), TimeUnit.SECONDS);
	}

}
