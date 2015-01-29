package de.livinglab.dashboard;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ServiceManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.livinglab.dashboard.data.Asset;
import de.livinglab.dashboard.data.Definition;
import de.livinglab.dashboard.data.Definitions;
import de.livinglab.dashboard.data.Instance;
import de.livinglab.dashboard.data.Instances;
import de.livinglab.dashboard.data.Locations;
import de.livinglab.dashboard.data.LogisticServiceProvider;
import de.livinglab.dashboard.data.LogisticsServiceProvider;
import de.livinglab.dashboard.events.InstanceSelected;
import de.livinglab.dashboard.events.LoadComplete;
import de.livinglab.dashboard.events.LoadRequest;
import de.livinglab.dashboard.events.ProcessSelected;
import de.livinglab.dashboard.events.XmlSelected;

@VaadinView(name = "mainView")
@UIScope
public class MainView extends VerticalLayout implements View {
	private static final Logger log = LoggerFactory.getLogger(MainView.class);
	private static final String MAP_CAPTION = "Map";

	@Autowired
	Environment env;
	
	@Autowired
	ServiceManager manager;
	
	@Autowired
	DataService dataService;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	XmlPanel xmlPanel;
	
	@Autowired
	MapPanel mapPanel;
	
	@Autowired
	LspPanel lspPanel;
	
	@Autowired
	ProcessPanel processPanel;
	
	private GridLayout grid;
	
	class LoadRecorder{
		private final UI ui;
		public LoadRecorder(UI ui) {
			this.ui = ui;
		}
		
		@Subscribe
		public void recordLoadComplete(final LoadComplete event){
			if(event.getReq().getUri().equals(env.getProperty("lsp.url")) && (event.getReq().getReturnType() == String.class)){
				ui.access(new Runnable() {
					@Override
					public void run() {
						loadProviders(dataService.getResource(event.getReq(), String.class));
					}
				});
			}
			if(event.getReq().getReturnType() == LogisticsServiceProvider.class){
				ui.access(new Runnable() {
					@Override
					public void run() {
						loadLsps(dataService.getResource(event.getReq(), LogisticsServiceProvider.class));
						requestLocations(dataService.getResource(event.getReq(), LogisticsServiceProvider.class));
					}
				});
			}
			else if(event.getReq().getUri().equals(env.getProperty("jbpm.definitions"))){
				ui.access(new Runnable() {
					@Override
					public void run() {
						Definitions defs = dataService.getResource(env.getProperty("jbpm.definitions"), Definitions.class);
						loadDefinitions(defs);
						requestInstances(defs);
						requestDefinitionAssets(defs);
					}
				});
			}
			else if(event.getReq().getUri().contains("instances")){
				ui.access(new Runnable() {
					@Override
					public void run() {
						loadInstances(dataService.getResource(event.getReq(), Instances.class));
						requestInstanceDatasets(dataService.getResource(event.getReq(), Instances.class));
					}
				});
			}
			else if(event.getReq().getUri().contains("dataset")){
				ui.access(new Runnable() {
					@Override
					public void run() {
						loadDataset(dataService.getResource(event.getReq(), String.class));
					}
				});
			}
			else if(event.getReq().getUri().contains("assets")){
				ui.access(new Runnable() {
					@Override
					public void run() {
						loadDesignerUrl(dataService.getResource(event.getReq(), Asset.class));
					}
				});
			}
		}
		
	}
	
	class UserInputRecorder{
		@Subscribe
		public void recordXmlSelected(XmlSelected event){
			log.info(event.getNodeString());
		}
		@Subscribe
		public void recordProcessSelected(ProcessSelected event){
			log.info(event.getDefiniton().getId()+" selected");
		}
		@Subscribe
		public void recordInstanceSelected(InstanceSelected event){
			log.info(event.getInstance().getId()+" selected");
		}
	}

	public MainView() {
		setSizeFull();
		
		initTop();
		
		initGrid();
        
	}
	
	@PostConstruct
	public void init(){
		grid.addComponent(xmlPanel);
        grid.addComponent(mapPanel);
        grid.addComponent(processPanel);
        grid.addComponent(lspPanel);
        eb.register(new LoadRecorder(UI.getCurrent()));
        eb.register(new UserInputRecorder());
        loadInitialResources();
	}
	
	private void loadInitialResources() {
		if(!dataService.isLoaded(env.getProperty("lsp.url"), String.class)){
			eb.post(new LoadRequest(env.getProperty("lsp.url"), LoadRequest.ResourceType.PLAIN_XML, String.class, false));
			}
		else{
			loadProviders(dataService.getResource(env.getProperty("lsp.url"), String.class));
		}
		
		if(!dataService.isLoaded(env.getProperty("lsp.url"), LogisticsServiceProvider.class)){
			eb.post(new LoadRequest(env.getProperty("lsp.url"), LoadRequest.ResourceType.PLAIN_XML, LogisticsServiceProvider.class, false));
		}
		else{
			loadLsps(dataService.getResource(env.getProperty("lsp.url"), LogisticsServiceProvider.class));
		}

		if(!dataService.isLoaded(env.getProperty("jbpm.definitions"))){
			eb.post(new LoadRequest(env.getProperty("jbpm.definitions"), LoadRequest.ResourceType.PLAIN_JSON, Definitions.class, false));
		}
		else{
			loadDefinitions(dataService.getResource(env.getProperty("jbpm.definitions"), Definitions.class));
		}
		
	}
	
	private void requestInstances(Definitions defs){
		for(Definition def : defs.getDefinitions()){
			eb.post(new LoadRequest(String.format(env.getProperty("jbpm.instances"), def.getId()), LoadRequest.ResourceType.PLAIN_JSON, Instances.class, false));
		}
	}
	
	private void requestInstanceDatasets(Instances insts){
		for(Instance ins : insts.getInstances()){
			eb.post(new LoadRequest(String.format(env.getProperty("jbpm.instanceData"), ins.getId()), LoadRequest.ResourceType.ALL_XML, String.class, false));
		}
	}
	
	private void requestDefinitionAssets(Definitions defs){
		for(Definition def : defs.getDefinitions()){
			if(def.getId().equals("com.sample.evaluation")) continue;
			eb.post(new LoadRequest(String.format(env.getProperty("jbpm.asset"), def.getPackageName(), def.getName()), LoadRequest.ResourceType.BASICAUTH_XML, Asset.class, false));
		}
	}
	
	private void requestLocations(LogisticsServiceProvider lsps){
		for(LogisticServiceProvider lsp : lsps.getLogisticsServiceProvider()){
			eb.post(new LoadRequest(String.format(env.getProperty("lsp.locations"), lsp.getId()), LoadRequest.ResourceType.PLAIN_XML, Locations.class, false));
		}
	}
	
	private void loadDefinitions(Definitions definitions) {
		processPanel.readDefinitions(definitions);
	}
	
	private void loadInstances(Instances insts) {
		processPanel.readInstances(insts);
	}

	private void loadProviders(String xml) {
		xmlPanel.readXml(xml);
	}
	
	private void loadLsps(LogisticsServiceProvider providers) {
		lspPanel.readLsps(providers);
	}
	
	private void loadDesignerUrl(Asset asset){
		processPanel.makeDesignerUrl(asset);
	}

	private void loadDataset(String xml) {
		processPanel.readDetailsXml(xml);
	}

	private void initGrid() {
        grid = new GridLayout(2,2);
        grid.setStyleName("panel-grid");
        addComponent(grid);
        grid.setSpacing(true);
        grid.setSizeFull();
        setExpandRatio(grid, 1.0f);
        grid.setMargin(true);
	}

	private void initTop(){
        HorizontalLayout top = new HorizontalLayout();
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        
        Image logoImage = new Image(null, new ClassResource("/de/livinglab/dashboard/resources/livinglab_logo.png"));
        top.addComponent(logoImage);
        
        Label title = new Label("4PL Dashboard");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setExpandRatio(title, 1.0f);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        
        top.setSizeUndefined();
        top.setWidth("100%");
	}

	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
