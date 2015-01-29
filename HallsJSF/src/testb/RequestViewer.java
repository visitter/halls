package testb;

import java.awt.Point;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

public class RequestViewer implements Serializable {
	private static final long serialVersionUID = 1L;
	private int iterator = 0;
	
	private ScheduleModel eventModel;
	public ScheduleModel getEventModel() {
		return eventModel;
	}
	
	private ScheduleEvent event;	
	public ScheduleEvent getEvent() {
		return event;
	}
	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	private List<Request> requests;
	public List<Request> getRequests() {
		return requests;
	}

    private Request selectedRequest;       
	public Request getSelectedRequest() {
		return selectedRequest;
	}
	public void setSelectedRequest(Request selectedRequest) {
		this.selectedRequest = selectedRequest;
	}
    
	private Request newRequest;
	public Request getNewRequest() {
		return newRequest;
	}
	public void setNewRequest(Request newRequest) {
		this.newRequest = newRequest;
	}
		
	private Integer hallId;
	public Integer getHallId() {
		return hallId;
	}
	public void setHallId(Integer hallId) {
		this.hallId = hallId;
	}

	private Integer meetingType;
	public Integer getMeetingType() {
		return meetingType;
	}
	public void setMeetingType(Integer meetingType) {
		this.meetingType = meetingType;
	}

	private List<BaseNomenclatureRow> meetingTypes;
	public List<BaseNomenclatureRow> getMeetingTypes() {
		return meetingTypes;
	}
	
	private Hall hall;
	public Hall getHall() {
		return hall;
	}
	public void setHall(Hall hall) {
		this.hall = hall;
	}
	
	private List<Hall> halls;
	public List<Hall> getHalls() {
		return halls;
	}
	
	public String meetingDesc;
	public String getMeetingDesc() {
		return meetingDesc;
	}
	public void setMeetingDesc(String meetingDesc) {
		this.meetingDesc = meetingDesc;
	}
	
	public List<AvailableResources> hashMap;	
	public List<AvailableResources> getHashMap() {
		return hashMap;
	}
	
	
	@PostConstruct
    public void init() {		
    	System.out.println("init");    		
    	
    	newRequest = new Request(0,"","Ново","01-01-2015 12:00:00","01-01-2015 14:00:00");
            
    	eventModel = new DefaultScheduleModel();
    	eventModel.clear();
    	
    	try {
			event = new DefaultScheduleEvent(
												newRequest.getName(),
												ScheduleView.setMyDate(newRequest.getStartDate()),
												ScheduleView.setMyDate(newRequest.getEndDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	if(eventModel==null)
    		System.out.println("eventModel = nul");
    	//eventModel.addEvent(event);
    	
    	halls = HallsManager.getAllHalls();
    	hall = halls.get(0);	
    	
    	getAllMeetingTypes();
    	
        //checkAvailability();
    }
	
	
	public void getSelectedHall(){
		System.out.println(hallId);
		System.out.println(meetingType);
	}
	
	public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();        
    	System.out.println(event.getData());
    	selectedRequest = (Request)event.getData();
    	System.out.println(selectedRequest.getId());
    	addElement();
    	checkAvailability();
    }
	public void onEventSelectNoAdd(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    	selectedRequest = (Request)event.getData();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	this.event = (ScheduleEvent) event.getScheduleEvent();        
     	System.out.println(this.event.getData());
     	selectedRequest = (Request)this.event.getData();
     	selectedRequest.setStartDate(format.format(this.event.getStartDate()));
        selectedRequest.setEndDate(format.format(this.event.getEndDate()));
        checkAvailability();
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for(Request req : requests){
        	if( req.getId().toString().equals(event.getScheduleEvent().getId())){
        		req.setStartDate( format.format( event.getScheduleEvent().getStartDate()));
        		req.setEndDate( format.format( event.getScheduleEvent().getEndDate()));        		
        		System.out.println(req.toString());
        	}
        }
    }
    
    public void getAllHalls(){
		halls = HallsManager.getAllHalls();
    	hall = halls.get(0);
	}
    public void getAllRequests(){
    	getAllRequests(false);
    }
    public void getAllRequestsAdmin(){
    	getAllRequests(true);
    	checkAvailability();
    }
    public void getAllRequests(Boolean addEl){    	
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();				
			requests = oCon.getAllRequests(false);
			oCon.close();
			if( (requests!=null)&&(requests.size()>0)){
				for( Request req : requests){
					DefaultScheduleEvent ev=null;
					try {
						System.out.println(req.getStartDate());
						ev = new DefaultScheduleEvent( 
																	req.getName(),
																	ScheduleView.setMyDate( req.getStartDate() ),
																	ScheduleView.setMyDate(req.getEndDate() )
																	);
						ev.setData(req);
					} catch (ParseException e) {					
						e.printStackTrace();
					}
					if( ev!=null ){
						System.out.println("me.getmId() ="+  req.getId());
						eventModel.addEvent(ev);
						ev.setId(req.getId().toString());
						setEvent(ev);						
					}
				}
				selectedRequest = (Request)event.getData();
				if(addEl)
					addElement();
			}
							
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		} catch (SQLException e){
			e.printStackTrace();
			addMessage("Error in database",e.getMessage());
		}
		if( (requests!=null)&&(requests.size()>0))
			setSelectedRequest(requests.get(0));    
	}
	
	public void insert(){
		System.out.println("Inserting...");
		
		try {
			JdbcConnector oCon = new JdbcConnector();
			Meeting me = new Meeting(0, meetingDesc, event.getTitle(), event.getStartDate(), event.getEndDate(), meetingType, selectedRequest.getId(), hallId);
			Schedule sch = new Schedule(0, hallId, 0, event.getStartDate(), event.getEndDate());
			if( oCon.isHallAvailable(sch)){
				if( oCon.insertMeeting(me, sch)){
					addMessage("Meeting created successfully", event.getTitle());
				}
			}else{
				addMessage("Hall is occupied for the given period", sch.getStartDate()+" "+sch.getEndDate());
			}
			oCon.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			addMessage("Error in database",e.getMessage());
			e.printStackTrace();
		}
				
	}
	
	public HashMap<String, Integer> checkAvailability(){

		try {
			JdbcConnector oCon = new JdbcConnector();
			HashMap<String, Integer> hash = oCon.checkAvailableResources(event.getStartDate(), event.getEndDate());
			
			if(hashMap==null)
				hashMap = new ArrayList<AvailableResources>();
			else
				hashMap.clear();
			
	    	for( Map.Entry<String, Integer> entry: hash.entrySet() ){
	    		hashMap.add( new AvailableResources(entry.getKey(), entry.getValue()));
	    	}
			oCon.close();
			return hash;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			addMessage("Error in database",e.getMessage());
			e.printStackTrace();
			return null;
		}
	
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);        
    }
	
	public void onRowSelect(SelectEvent event) {
		setSelectedRequest(((Request) event.getObject()));
		System.out.println(this.selectedRequest.getId());
		addMessage("Choosen", selectedRequest.getName());
    }
	
	public Point convertCoordinates(String coord){		
		String arr[] = coord.split(",");
		if(arr.length>1){			
			return new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]));
		}else
			return new Point(0, 0);		
	}
	
	public void addElement() {
		UIComponent component=null;
		try{
			component = FacesContext.getCurrentInstance().getViewRoot().
				findComponent("form").
					findComponent("myPanelGrid").
						findComponent("myPanelGrid1").
							findComponent("myPanelGrid2");
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}

		if( component!=null )
			component.getChildren().clear();
		
		
		if( selectedRequest!=null)
		{
			if( (selectedRequest.getReq()!=null)&&(selectedRequest.getReq().size()>0) )
			{	
				for( Requirements req:selectedRequest.getReq()){
					String[] arrP = req.getPositions().split(";");
					ArrayList<Point> points = null; 
					if( arrP.length>0 ){
						points = new ArrayList<Point>();
						for(String strP:arrP){
							points.add(convertCoordinates(strP));
						}
					}
					
					for( int i = 0;i<req.getCount();i++){
						GraphicImage p = new GraphicImage();
				    	p.setId("test"+iterator );
				    	p.setAlt(req.getTypeId().toString());
				    	String tmp = String.format("http://127.0.0.1:8080/HallsJSF/img/equip/eq%s.png",req.getTypeId().toString());
				    	p.setValue(tmp);
				    	if( (points!=null)&&(points.size()>0))
				    		p.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", points.get(i).y, points.get(i).x));
				    	else
				    		p.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", 10, 10));
				    	//p.setOnmouseup("setItemId(this.id)");
				    	//p.setOndblclick("removeItem(this)");	    
		
				    	Draggable drag = new Draggable();	    	
				    	drag.setFor("test"+iterator++);
				    	drag.setId("test"+iterator++);
				    	drag.setZindex(1000);
		
				    	component.getChildren().add(p);
				    	component.getChildren().add(drag);        
					}
				}
			}
		}
	}
	
	public void getAllMeetingTypes(){
		BaseNomManager man = new BaseNomManager();
		meetingTypes = man.getAllNomRows("NOM_ME_TYPES");
	}
}