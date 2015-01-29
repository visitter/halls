package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

//@ManagedBean
//@ViewScoped
public class ScheduleView implements Serializable {

	private static final long serialVersionUID = 1L;

	private ScheduleModel eventModel;     
    private ScheduleEvent event;
    private Meeting meeting = new Meeting();   
    private  ArrayList<Meeting> arr = null;
    
    private final String startTime = "07:00";
    private final String endTime = "18:00";
    
    
    public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public String incTime(int hour) {		
		return startTime;
	}
	public Meeting getMeeting() {
		return meeting;
	}
	
	@PostConstruct
    public void init() {		
    	eventModel = new DefaultScheduleModel();
    	eventModel.clear();
    	try {
			event = new DefaultScheduleEvent("TEST", setMyDate("01-01-2015 10:00:00"), setMyDate("01-01-2015 12:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	if(eventModel==null)
    		System.out.println("eventModel = null");
    	
    	System.out.println("Init");
    }
	
	public void getAllEvents(){
		System.out.println("getallevents");
		eventModel.clear();		
		if(arr!=null)
			arr.clear();
		
		arr = getAll();
		
    	if( arr!=null){
	        for( Meeting me : arr){
        		DefaultScheduleEvent ev = new DefaultScheduleEvent( me.getmName(), me.getmSDate(), me.getmEDate());
	        		
        		System.out.println("me.getmId() ="+  me.getmId());
        		ev.setData(me);
        		eventModel.addEvent(ev);
        		ev.setId(me.getmId().toString());
        		meeting = me;
	        }
    	}else{    		
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No events yet","");
    		addMessage(message);
    	}
	}
	
    public static Date setMyDate(String cDate) throws ParseException{
		Pattern datePattern = Pattern.compile("(0[1-9]|[1-9]|[12][0-9]|3[01])-([1-9]|0[1-9]|1[0-2])-((19|20)\\d{2}) "
				+ "([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])");		
        Matcher dateMatcher = datePattern.matcher(cDate);
        
		if( dateMatcher.matches() ){
			String[] sDate = cDate.split(" ");
			String[] mydate = sDate[0].split("-");
			String[] mytime = sDate[1].split(":");
			
			Calendar date = Calendar.getInstance();
			date.set( Calendar.YEAR        , Integer.parseInt(mydate[2]));
			date.set( Calendar.MONTH       , Integer.parseInt(mydate[1])-1);
			date.set( Calendar.DAY_OF_MONTH, Integer.parseInt(mydate[0]));
			date.set( Calendar.HOUR_OF_DAY , Integer.parseInt(mytime[0]));
			date.set( Calendar.MINUTE      , Integer.parseInt(mytime[1]));

			return date.getTime();
		}else throw new ParseException("Invalid format", 1);
    }
     
    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }   

     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null)
            eventModel.addEvent(event);
        else {
        	System.out.println(event.getStartDate().toString());
        	System.out.println(event.getEndDate().toString());
        	
            eventModel.updateEvent(event);
        }
         
        event = new DefaultScheduleEvent();
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        meeting = ((Meeting)event.getData());
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        
        System.out.println(event.getScheduleEvent().getId());
        System.out.println(event.getScheduleEvent().getTitle());
        
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
  
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void bla(ActionEvent actionEvent){
    	eventModel.updateEvent(event);
        
    	System.out.println("Entering bla1");
    	
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			int i = 0;
			for( Meeting me:arr){
				if(me.getmId().toString().equalsIgnoreCase(event.getId())){
					i=arr.indexOf(me);
					
					Calendar cal = Calendar.getInstance();					 
					
					cal.setTime(event.getStartDate());
					//String date = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);
					me.setmSDate( cal.getTime() );
										
					cal.setTime(event.getEndDate());
					//date        = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);
					me.setmEDate( cal.getTime() );
					
					me.setmName( event.getTitle());
					//append hall also					
				}
			}
			
			FacesMessage message;			
			
			if( oCon.updateSchMeeting( arr.get(i))){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "OK");				 
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated", "ERROR");
			}
			addMessage(message);
			
			oCon.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		}  catch (SQLException e){
			e.printStackTrace();
			FacesMessage message;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in database", e.getMessage());
			addMessage(message);
		}	
    }    
   
    public ArrayList<Meeting> getAll(){
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			System.out.println("getall");
			ArrayList<Meeting> arr  = oCon.getAllMeetings();
			oCon.close();
			return arr;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}  catch (SQLException e){
			e.printStackTrace();
			FacesMessage message;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in database", e.getMessage());
			addMessage(message);
			return null;
		}	    	
    }
    
    public void delete(){
    	System.out.println(meeting.getmName());
    	FacesMessage message;
    	JdbcConnector oCon;
    	try {
			oCon = new JdbcConnector();
			if(oCon.deleteScheduledMeeting(meeting) ){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event deleted successfully", meeting.getmName());
				addMessage(message);
			}
			oCon.close();
			
			eventModel.deleteEvent(event);
			getAllEvents();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event delete error", e.getMessage());
			addMessage(message);
		}  catch (SQLException e){
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event delete error", e.getMessage());
			addMessage(message);
		}
    	
    }
    public void update(){
    	meeting.setmName(event.getTitle());
    	meeting.setmSDate(event.getStartDate());
    	meeting.setmEDate(event.getEndDate());
    	FacesMessage message;
    	JdbcConnector oCon;
    	try {
			oCon = new JdbcConnector();
			
			if(oCon.updateScheduledMeeting(meeting) ){
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event updated successfully", meeting.getmName());
				addMessage(message);
			}			
			oCon.close();
			
			eventModel.deleteEvent(event);
			getAllEvents();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event update error", e.getMessage());
			addMessage(message);
		}  catch (SQLException e){
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event update error", e.getMessage());
			addMessage(message);
		}
    	
    }
    
}