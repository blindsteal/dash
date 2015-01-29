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
import de.livinglab.dashboard.data.LogisticServiceProvider;
import de.livinglab.dashboard.data.LogisticsServiceProvider;
import de.livinglab.dashboard.events.InstanceSelected;
import de.livinglab.dashboard.events.ProcessSelected;

@Component("lspPanel")
public class LspPanel extends Panel {
	private enum IDS{CAPTION}

	private static final String CAPTION = "Providers";
	
	private TreeTable tt;
	
	private List<LogisticServiceProvider> lsps;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	Environment env;
	
	public LspPanel(){
		super(CAPTION);
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
				
			}
		});
		
		
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
