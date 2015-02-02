package de.livinglab.dashboard;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.vaadin.spring.UIScope;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.livinglab.dashboard.data.Definition;
import de.livinglab.dashboard.data.Definitions;
import de.livinglab.dashboard.data.LogisticsServiceProvider;
import de.livinglab.dashboard.data.Task;
import de.livinglab.dashboard.data.Tasks;
import de.livinglab.dashboard.events.LoadComplete;
import de.livinglab.dashboard.events.LoadRequest;

@Component("taskPanel")
@UIScope
public class TaskPanel extends Panel {
	private enum IDS{CAPTION}

	private static final String CAPTION = "Tasks";
	
	private TreeTable tt;
	
	private String user;
	
	private List<Task> taskList;
	
	@Autowired
	EventBus eb;
	
	@Autowired
	Environment env;
	
	@Autowired
	DataService dataService;

	private Window taskWindow;
	
	class LoadEventRecorder{
		private final UI ui;
		public LoadEventRecorder(UI ui) {
			this.ui = ui;
		}
		@Subscribe
		public void recordLoadComplete(final LoadComplete event){
			if(event.getReq().getReturnType() == Tasks.class){
				ui.access(new Runnable() {
					@Override
					public void run() {
						readTasks(dataService.getResource(event.getReq(), Tasks.class));
					}
				});
			}
		}
	}
	
	public TaskPanel(){
		super(CAPTION);
		buildPanel();
	}
	
	@PostConstruct
	private void init(){
		eb.register(new LoadEventRecorder(UI.getCurrent()));
		user = env.getProperty("jbpm.tasks.user");
		requestTaskList();
	}
	
	private void requestTaskList(){
		eb.post(new LoadRequest(String.format(env.getProperty("jbpm.tasks"), user), LoadRequest.ResourceType.PLAIN_JSON, Tasks.class, false));
		
	}
	
	private void buildPanel(){
		addStyleName("layout-panel");
		addStyleName("task-panel");
		setSizeFull();
		
		taskWindow = new Window("Task Form");
		taskWindow.setWidth("40%");
		taskWindow.setHeight("60%");
		taskWindow.center();
		
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
				Task t = (Task)tt.getValue();
				BrowserFrame bf = new BrowserFrame(null, new ExternalResource(t.getUrl()));
				bf.setSizeFull();
				taskWindow.setContent(bf);
				UI.getCurrent().addWindow(taskWindow);
			}
		});
		
		
	}
	
	public void readTasks(Tasks tasks){
		tt.removeAllItems();
		taskList = tasks.getTasks();
		for(Task t : tasks.getTasks()){
			tt.addItem(t);
			String caption = String.format("Task: %1$s (%2$s, Instance: %3$s)", t.getName(), t.getProcessId(), t.getProcessInstanceId());
			tt.getItem(t).getItemProperty(IDS.CAPTION).setValue(caption);
		}
		markAsDirtyRecursive();
	}
	
}
