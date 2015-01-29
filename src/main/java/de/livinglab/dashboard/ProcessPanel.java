package de.livinglab.dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import de.livinglab.dashboard.data.Asset;
import de.livinglab.dashboard.data.Definition;
import de.livinglab.dashboard.data.Definitions;
import de.livinglab.dashboard.data.Instance;
import de.livinglab.dashboard.data.Instances;
import de.livinglab.dashboard.events.InstanceSelected;
import de.livinglab.dashboard.events.ProcessSelected;

@Component("processPanel")
public class ProcessPanel extends Panel {
	private enum IDS{CAPTION}

	private static final String CAPTION = "Process View";
	
	private TreeTable tt;
	
	private Window designerWindow, detailsWindow;
	
	private XmlPanel detailsPanel;
	
	private List<Definition> definitions;
	
	private Map<Definition, List<Instance>> instanceMap;
	
	private Map<Definition, String> urlMap;
	
	private Map<Instance, String> detailsMap;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	Environment env;
	
	public ProcessPanel(){
		super(CAPTION);
		instanceMap = Maps.newConcurrentMap();
		urlMap = Maps.newConcurrentMap();
		detailsMap = Maps.newConcurrentMap();
		buildPanel();
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("process-panel");
		setSizeFull();
		
		designerWindow = new Window("Process Designer");
		designerWindow.setWidth("60%");
		designerWindow.setHeight("60%");
		designerWindow.center();
		
		detailsWindow = new Window("Instance Details");
		detailsWindow.setWidth("40%");
		detailsWindow.setHeight("60%");
		detailsWindow.center();
		detailsPanel = new XmlPanel(null);
		detailsPanel.setSizeFull();
		detailsWindow.setContent(detailsPanel);
		
		tt = new TreeTable();
		tt.setSelectable(true);
		tt.setImmediate(true);
		tt.setSizeFull();
		tt.addContainerProperty(IDS.CAPTION, String.class, "");
		tt.setColumnHeaderMode(TreeTable.ColumnHeaderMode.HIDDEN);
		tt.setColumnExpandRatio(IDS.CAPTION, 1);
		setContent(tt);
		
		tt.addValueChangeListener(new Property.ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				Object o = tt.getValue();
				if(o.getClass()==Definition.class){
					eb.post(new ProcessSelected((Definition)o));
					BrowserFrame bf = new BrowserFrame(null, new ExternalResource(urlMap.get((Definition)o)));
					bf.setSizeFull();
					designerWindow.setContent(bf);
					UI.getCurrent().addWindow(designerWindow);
				}
				else if(o.getClass()==Instance.class){
					eb.post(new InstanceSelected((Instance)o));
					detailsPanel.clear();
					detailsPanel.readXml(detailsMap.get((Instance)o));
					UI.getCurrent().addWindow(detailsWindow);
				}
				
			}
		});
		
		
	}
	
	private Instance getInsById(String id){
		for(List<Instance> insList : instanceMap.values()){
			for(Instance ins : insList){
				if(ins.getId().equals(id)) return ins;
			}
		}
		return null;
	}
	
	private Definition getDefById(String id){
		for(Definition def : definitions){
			if(def.getId().equals(id)) return def;
		}
		return null;
	}
	
	private Definition getDefByName(String name){
		for(Definition def : definitions){
			if(def.getName().equals(name)) return def;
		}
		return null;
	}

	public void readDefinitions(Definitions defs){
		definitions = defs.getDefinitions();
		for(Definition def : definitions){
			String caption = def.getId() + " (version: " + def.getVersion() +", suspended: "+def.isSuspended()+")";
			tt.addItem(def);
			tt.getItem(def).getItemProperty(IDS.CAPTION).setValue(caption);
		}
		markAsDirtyRecursive();
	}
	
	public void readInstances(Instances insts){
		if(insts.getInstances().size()==0) return;
		Definition def = getDefById(insts.getInstances().get(0).getDefinitionId());
		for(Instance inst : insts.getInstances()){
			String caption = String.format("ID: %1$s, Started: %2$s", inst.getId(), inst.getStartDate());
			tt.addItem(inst);
			tt.getItem(inst).getItemProperty(IDS.CAPTION).setValue(caption);
			tt.setParent(inst, def);
		}
		instanceMap.put(def, insts.getInstances());
		markAsDirtyRecursive();
		
	}

	public void makeDesignerUrl(Asset asset) {
		String url = String.format(env.getProperty("jbpm.designer"), asset.getMetadata().getUuid());
		urlMap.put(getDefByName(asset.getTitle()), url);
	}
	
	public void readDetailsXml(String xml){
		Document xmldoc = null;
		try {
			xmldoc = new Builder().build(xml, null);
		} catch (ParsingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element root = xmldoc.getRootElement();
		String instanceId = root.getAttributeValue("ref");
		
		detailsMap.put(getInsById(instanceId), xml);
	}
	
	
}
