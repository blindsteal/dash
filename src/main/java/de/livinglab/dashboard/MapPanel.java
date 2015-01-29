package de.livinglab.dashboard;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.ui.Panel;

@Component("mapPanel")
public class MapPanel extends Panel {
	private static final String CAPTION = "Map";
	private GoogleMap googleMap;
	
	public MapPanel(){
		super(CAPTION);
		buildPanel();
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("map-panel");
		setSizeFull();
		
		String apiKey = "AIzaSyAEjVKUJie1w0Ea3RyCBxbhNYpNT8wgBww";
		googleMap = new GoogleMap(apiKey, null, null);
        googleMap.setSizeFull();
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        
        setContent(googleMap);
	}
}
