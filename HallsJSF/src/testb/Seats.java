package testb;

import java.awt.Point;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.dnd.Draggable;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@ViewScoped
public class Seats implements Serializable{
	
	class EqItem implements Serializable {
		private static final long serialVersionUID = -2870000903242940119L;
		private String id;
		private String type;
		private Point coords;	
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Point getCoords() {
			return coords;
		}
		public void setCoords(Point coords) {
			this.coords = coords;
		}

		public EqItem(String id, String type, Point coords){
			setId(id);
			setType(type);
			setCoords(coords);
		}
	}	
	
	private static final long serialVersionUID = 2547791646470775157L;
	private int iterator = 0;	 
	private HashMap<String,Integer> hmItemsCount = new HashMap<String, Integer>();
	
	private Date minDate;	
	public Date getMinDate() {
		return minDate;
	}
	
	private Date startDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	private Date endDate;
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	private Integer readyReq;
	public Integer getReadyReq() {		
		return readyReq;
	}
	public void setReadyReq(Integer readyReq) {
		this.readyReq = readyReq;
	}

	
	private ArrayList<Request> requestsList = null;
	public ArrayList<Request> getRequestsList() {
		return requestsList;
	}

	private Request request = new Request();	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}

	private MenuModel model;	
	public MenuModel getModel() {
		return model;
	}
	public void setModel(MenuModel model) {
		this.model = model;
	}

	private ArrayList<EqItem> equipment = new ArrayList<EqItem>(); 
	
	private String selectedEq;	
	public String getSelectedEq() {
		return selectedEq;
	}
	public void setSelectedEq(String selectedEq) {
		this.selectedEq = selectedEq;
	}
	
	private String eqItems;
	public String getEqItems() {
		return eqItems;
	}
	public void setEqItems(String eqItems) {
		this.eqItems = eqItems;
	}
	
	@PostConstruct	
	public void Init(){		
		Calendar cal = Calendar.getInstance();
	
		minDate = cal.getTime();
		startDate =  cal.getTime();
		endDate =  cal.getTime();
		getAllRequests();
		
	}	
	
	public void addElement(ActionEvent event) {
		UIMenuItem mi = (UIMenuItem)event.getComponent();		
		
		UIComponent component = FacesContext.getCurrentInstance().getViewRoot().
	    						findComponent("form").
	    							findComponent("myPanelGrid").
	    								findComponent("myPanelGrid1").
											findComponent("myPanelGrid2");
	    
	    if (component != null) {	       
	    	GraphicImage p = new GraphicImage();
	    	p.setId("test"+iterator );
	    	p.setAlt(mi.getId());
	    	//p.setValue(String.format(".\\..\\img\\equip\\%s.png",mi.getId()));
	    	p.setValue(String.format("http://127.0.0.1:8080/HallsJSF/img/equip/%s.png",mi.getId()));
	    	p.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", 10,10));	    
	    	p.setOnmouseup("setItemId(this.id)");
	    	p.setOndblclick("removeItem(this)");	    

	    	Draggable drag = new Draggable();	    	
	    	drag.setFor("test"+iterator++);
	    	drag.setId("test"+iterator++);
	    	drag.setZindex(1000);
	    		    	
	    	EqItem eq = new EqItem("form:"+p.getId(), p.getAlt(), new Point(10,10));
			equipment.add(eq);
			selectedEq = eq.getId();

	    	component.getChildren().add(p);
	    	component.getChildren().add(drag);        
	    }else System.out.println("component is null");	    
	}
	
	public Point convertCoordinates(){		
		String arr[] = eqItems.split(";");
		if(arr.length>1){			
			return new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]));
		}else
			return new Point(0, 0);		
	}
	
	public void refresh(){
		//debug info
		System.out.println("EqItems  = "+eqItems);
		System.out.println("Selected = "+selectedEq);
		for( EqItem eq:equipment){
			if(eq.getId().equals(selectedEq))
				System.out.println("FOUND");
		}
	}
	
	public void onDrop(DragDropEvent event) {
		UIComponent ui = FacesContext.getCurrentInstance().getViewRoot().
						 findComponent("form").
						 	findComponent("myPanelGrid").
						 		findComponent("myPanelGrid1").
						 			findComponent("myPanelGrid2").
						 				findComponent(event.getDragId());
		
		GraphicImage gr =(GraphicImage) ui;
		Point p = convertCoordinates();
		selectedEq = event.getDragId();
						
		System.out.println(gr.getStyle());
		gr.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", p.y,p.x));
		System.out.println(gr.getStyle());
		
		for( EqItem eq:equipment){
			if(eq.getId().equals(selectedEq))
				eq.setCoords(p);
		}
    }
	
	public void removeItem(ActionEvent event){
		 UIComponent component = FacesContext.getCurrentInstance().getViewRoot().
					findComponent("form").
						findComponent("myPanelGrid").
							findComponent("myPanelGrid1").findComponent("myPanelGrid2");
		 
		 for( UIComponent comp: component.getChildren()){
			 if( comp.getClientId().equals(selectedEq)){
				 for( EqItem eq:equipment){
					if(eq.getId().equals(selectedEq)){						
						equipment.remove(eq);							
						component.getChildren().remove(comp);
						break;
					}
				 }
				 break;
			 }
		 }
		 
		 String drag = selectedEq.substring(0, selectedEq.length()-1);
		 drag = drag+(Integer.parseInt(
				 						selectedEq.substring(selectedEq.length()-1, selectedEq.length())
				 					   )+1);
		 for( UIComponent comp: component.getChildren()){
			 System.out.println(comp.getClientId());
			 if( comp.getClientId().equals(drag)){				 			
				component.getChildren().remove(comp);
				break;
			 }
		 }
	}
		
	private boolean countItems(){
		hmItemsCount.clear();
		for( EqItem comp:equipment){
			Object key = comp.getType();
			Integer value = 1;
			if( hmItemsCount.containsKey(key) )
				value = hmItemsCount.get(key)+1;
			
			System.out.println(value);			
			hmItemsCount.put(comp.getType(), value );			
		}
		
		for (Entry<String, Integer> entry : hmItemsCount.entrySet()) {
			System.out.println( entry.getKey().toString() );
			System.out.println( entry.getValue().toString()) ;
		}
		return true;//hmItemsCount.size()>0;
	}
	
	public void insert(){		
		if( countItems() )
		{			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		     
		    request.setStartDate( format.format(startDate));
		    request.setEndDate( format.format(endDate));		    		
		   
			Request request = new Request( 0,
											this.request.getClientId(),
											this.request.getName(),
											this.request.getStartDate(),
											this.request.getEndDate()
										  );
			System.out.println(request.toString());
			
			ArrayList<Requirements> reqs = new ArrayList<Requirements>();
			
			for (Entry<String, Integer> entry : hmItemsCount.entrySet()) {
				StringBuilder positions = new StringBuilder();				
				for( EqItem comp:equipment){
					if(comp.getType().equals(entry.getKey())){
						positions.append(comp.getCoords().y+","+comp.getCoords().x+";");						
					}
				}
				Integer key =  Integer.parseInt( entry.getKey().substring(2, entry.getKey().length()) );
				String pos = positions.substring(0);
				
				reqs.add(new Requirements(0, key, entry.getValue(), pos));
			}			
			request.setReq(reqs);			
			
			JdbcConnector oConnector;
			try {
				oConnector = new JdbcConnector();
				if( oConnector.insertRequest(request) ){
					addMessage("Request added successfully", request.getName());
				}else addMessage("Error" , "Error in database");
				oConnector.close();				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}	
		}
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void setReadyReq(){
		switch(getReadyReq()){
			case 1:
				{break;}
			case 2:
				{break;}
			case 3:
				{break;}
			default:
			{}		
		}
	}
	
	public void addElements() {
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
		
		//iterator=0;
		equipment.clear();
		if( request!=null)
		{
			if( (request.getReq()!=null)&&(request.getReq().size()>0) )
			{					
				for( Requirements req:request.getReq()){
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
				    	p.setAlt("eq"+ req.getTypeId().toString());
				    	String tmp = String.format("http://127.0.0.1:8080/HallsJSF/img/equip/eq%s.png",req.getTypeId().toString());
				    	p.setValue(tmp);
				    	if( (points!=null)&&(points.size()>0))
				    		p.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", points.get(i).y, points.get(i).x));
				    	else
				    		p.setStyle(String.format("position:absolute;top:%dpx;left:%dpx;", 10, 10));
		
				    	p.setOnmouseup("setItemId(this.id)");
				    	p.setOndblclick("removeItem(this)");				    	
				    	
				    	Draggable drag = new Draggable();	    	
				    	drag.setFor("test"+iterator++);
				    	drag.setId("test"+iterator++);
				    	drag.setZindex(1000);
		
				    	component.getChildren().add(p);
				    	component.getChildren().add(drag);
				    	
				    	EqItem eq = new EqItem("form:"+p.getId(), p.getAlt(), new Point(points.get(i).x, points.get(i).y));
						equipment.add(eq);
					}
				}
			}
		}
	}
	public Point convertCoordinates(String coord){		
		String arr[] = coord.split(",");
		if(arr.length>1){			
			return new Point(Integer.parseInt(arr[1]), Integer.parseInt(arr[0]));
		}else
			return new Point(0, 0);		
	}
	
	
	public void getAllRequests(){    	
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			requestsList = new ArrayList<Request>();
			requestsList.add( new Request(0, "", "", "", ""));
			ArrayList<Request> arr = oCon.getAllRequests(true);
			if(arr!=null)
				requestsList.addAll(arr);
			oCon.close();
			setReadyReq(0);			
							
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		} catch (SQLException e){
			e.printStackTrace();
			addMessage("Error in database",e.getMessage());
		}
		if( (requestsList!=null)&&(requestsList.size()>0))
			setRequest(requestsList.get(0));    
	}
	
	public void setPositions(){
		if( readyReq>-1){
			for( Request req:requestsList){
				if( readyReq.equals(req.getId())){
					setRequest(req);
					break;
				}
			}
			
			System.out.println("request "+request.getId());
			addElements();
		}
		System.out.println("readyReq "+ readyReq);
	}
}
