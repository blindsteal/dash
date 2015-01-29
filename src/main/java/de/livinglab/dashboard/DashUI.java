package de.livinglab.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.shared.ui.ui.Transport;

import org.springframework.core.env.Environment;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.annotations.Push;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Widgetset("de.livinglab.dashboard.AppWidgetSet")
@Push(transport = Transport.LONG_POLLING)
@VaadinUI
@Theme("dash")
public class DashUI extends UI {
	
	@Autowired
	Environment env;
	
	@Autowired
	SpringViewProvider svp;
	
	Navigator nav;
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
    	getPage().setTitle("Logistics LivingLab Dashboard");
    	nav = new Navigator(this, this);
		nav.addProvider(svp);
    	nav.navigateTo("mainView");
        
    }
}
