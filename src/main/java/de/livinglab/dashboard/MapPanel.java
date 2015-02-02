package de.livinglab.dashboard;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.vaadin.spring.UIScope;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Panel;

import de.livinglab.dashboard.data.Location;
import de.livinglab.dashboard.events.LocationSelected;
import de.livinglab.dashboard.events.LspSelected;

@Component("mapPanel")
@UIScope
public class MapPanel extends Panel {
	private static final String CAPTION = "Map";
	private GoogleMap googleMap;
	private List<GoogleMapMarker> markers;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	Environment env;
	
	class LocationRecorder{
		@Subscribe
		public void recordLspSelected(LspSelected event){
			googleMap.clearMarkers();
			for(Location loc : event.getLocations()){
				LatLon coords = new LatLon(Double.parseDouble(loc.getLatitude()), Double.parseDouble(loc.getLongitude()));
				googleMap.addMarker(loc.getName(), coords, false, null);
				googleMap.setCenter(coords);
			}
		}
		@Subscribe
		public void recordLocSelected(LocationSelected event){
			googleMap.clearMarkers();
			LatLon coords = new LatLon(Double.parseDouble(event.getLoc().getLatitude()), Double.parseDouble(event.getLoc().getLongitude()));
			googleMap.addMarker(event.getLoc().getName(), coords, false, null);
			googleMap.setCenter(coords);
		}
	}
	
	public MapPanel(){
		super(CAPTION);
	}
	
	@PostConstruct
	private void init(){
		buildPanel();
		eb.register(new LocationRecorder());
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("map-panel");
		setSizeFull();
		googleMap = new GoogleMap(env.getProperty("maps.key"), null, null);
        googleMap.setSizeFull();
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        
        setContent(googleMap);
	}
}
