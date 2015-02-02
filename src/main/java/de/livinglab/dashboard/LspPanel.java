package de.livinglab.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.vaadin.spring.UIScope;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import de.livinglab.dashboard.data.Location;
import de.livinglab.dashboard.data.Locations;
import de.livinglab.dashboard.data.LogisticServiceProvider;
import de.livinglab.dashboard.data.LogisticsServiceProvider;
import de.livinglab.dashboard.events.LocationSelected;
import de.livinglab.dashboard.events.LspSelected;

@Component("lspPanel")
@UIScope
public class LspPanel extends Panel {
	private enum IDS{CAPTION}

	private static final String CAPTION = "Providers";
	
	private TreeTable tt;
	
	private List<LogisticServiceProvider> lsps;
	
	private Map<LogisticServiceProvider, List<Location>> locMap;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	Environment env;
	
	public LspPanel(){
		super(CAPTION);
		locMap = Maps.newConcurrentMap();
		buildPanel();
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("lsp-panel");
		setSizeFull();
		
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
				if(o.getClass()==LogisticServiceProvider.class){
					LogisticServiceProvider lsp = (LogisticServiceProvider)o;
					eb.post(new LspSelected(lsp, locMap.get(lsp)));
				}
				if(o.getClass()==Location.class){
					eb.post(new LocationSelected((Location)o));
				}
				
			}
		});
		
		
	}
	
	private LogisticServiceProvider getLspById(String id){
		for(LogisticServiceProvider lsp : lsps){
			if(lsp.getId().equals(id)) return lsp;
		}
		return null;
	}

	public void readLsps(LogisticsServiceProvider providers){
		lsps = providers.getLogisticsServiceProvider();
		for(LogisticServiceProvider lsp : lsps){
			String caption = String.format("%1$s (ID: %2$s)", lsp.getName(), lsp.getId());
			tt.addItem(lsp);
			tt.getItem(lsp).getItemProperty(IDS.CAPTION).setValue(caption);
			addContactDetails(lsp);
			
		}
		markAsDirtyRecursive();
	}
	
	public void readLocations(Locations locs){
		LogisticServiceProvider lsp = getLspById(locs.getLspId());
		locMap.put(lsp, locs.getLocations());
		for(Location loc : locs.getLocations()){
			tt.addItem(loc);
			String caption = loc.getName();
			tt.getItem(loc).getItemProperty(IDS.CAPTION).setValue(caption);
			tt.setParent(loc, lsp);
			Object idA = tt.addItem();
			tt.getItem(idA).getItemProperty(IDS.CAPTION).setValue("Adresse: "+loc.getStreet()+" "+loc.getStreetnumber()
					+", "+loc.getPostCode()+" "+loc.getCity());
			tt.setParent(idA, loc);
			Object idM = tt.addItem();
			tt.getItem(idM).getItemProperty(IDS.CAPTION).setValue("Mail: "+loc.getMail());
			tt.setParent(idM, loc);
			Object idP = tt.addItem();
			tt.getItem(idP).getItemProperty(IDS.CAPTION).setValue("Phone: "+lsp.getPhone());
			tt.setParent(idP, loc);
			
		}
		
	}
	
	private void addContactDetails(LogisticServiceProvider lsp){
		Object id = tt.addItem();
		tt.getItem(id).getItemProperty(IDS.CAPTION).setValue("Kontakt");
		tt.setParent(id, lsp);
		Object idIp = tt.addItem();
		tt.getItem(idIp).getItemProperty(IDS.CAPTION).setValue("IP: "+lsp.getIntegrationIP());
		tt.setParent(idIp, id);
		Object idM = tt.addItem();
		tt.getItem(idM).getItemProperty(IDS.CAPTION).setValue("Mail: "+lsp.getMail());
		tt.setParent(idM, id);
		Object idP = tt.addItem();
		tt.getItem(idP).getItemProperty(IDS.CAPTION).setValue("Phone: "+lsp.getPhone());
		tt.setParent(idP, id);
	}
	
}
