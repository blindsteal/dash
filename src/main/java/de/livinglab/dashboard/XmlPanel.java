package de.livinglab.dashboard;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.UIScope;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.google.common.eventbus.EventBus;

import de.livinglab.dashboard.events.XmlSelected;

@Component("xmlPanel")
@UIScope
public class XmlPanel extends Panel {
	private enum IDS{CAPTION}

	private static final String CAPTION = "Providers";
	
	private TreeTable tt;
	
	@Autowired
	EventBus eb;
	
	public XmlPanel(){
		super(CAPTION);
		buildPanel();
	}
	
	public XmlPanel(String caption){
		super(caption);
		buildPanel();
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("xml-panel");
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
				eb.post(new XmlSelected(tt.getItem(tt.getValue()).getItemProperty(IDS.CAPTION).getValue().toString()));
				
			}
		});
		
	}
	
	public void readXml(String doc){
		tt.removeAllItems();
		Document xmldoc = null;
		try {
			xmldoc = new Builder().build(doc, null);
		} catch (ParsingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element root = xmldoc.getRootElement();
		
		addXmlNodesRecursive(root, null);
		markAsDirtyRecursive();
	}
	
	private void addXmlNodesRecursive(Element node, Object parentId){
		Elements children = node.getChildElements();
		
		for(int i=0;i<children.size();i++){
			String nodeString = children.get(i).getLocalName();
			if(children.get(i).getAttributeCount() != 0){
				nodeString += " (";
				for(int j = 0;j<children.get(i).getAttributeCount();j++){
					nodeString += children.get(i).getAttribute(j).getLocalName() + "=" + children.get(i).getAttribute(j).getValue();
					if(j<children.get(i).getAttributeCount()-1) nodeString += ", ";
				}
				nodeString += ")";
			}
			if(children.get(i).getChildElements().size()==0){
				nodeString += ": "+children.get(i).getValue();
			}
			Object newId = tt.addItem();
			tt.getItem(newId).getItemProperty(IDS.CAPTION).setValue(nodeString);
			if(parentId != null){
				tt.setParent(newId, parentId);
			}
			addXmlNodesRecursive(children.get(i), newId);
		}
	}
	
	public void clear(){
		tt.removeAllItems();
	}
	
}
