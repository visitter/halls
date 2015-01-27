package testb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty; 
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

public class DataExporterView implements Serializable {
  
	private static final long serialVersionUID = 1L;

	private List<Hall> halls;
	public List<Hall> getHalls() {
        return halls;
    }
    
    @ManagedProperty("#{hallsManager}")
    private HallsManager manager;
    public HallsManager getManager() {
		return manager;
	}
    public void setManager(HallsManager manager){
    	this.manager = manager;
    }
    
    private Hall selectedHall;       
    public Hall getSelectedHall() {
		return selectedHall;
	}
	public void setSelectedHall(Hall hall) {
		this.selectedHall = hall;
	}

	private Hall newHall;		
	public Hall getNewHall() {
		return newHall;
	}
	public void setNewHall(Hall newHall) {
		this.newHall = newHall;
	}	
	
	@PostConstruct
    public void init() {
    	System.out.println("init");
    	
    	newHall = new Hall(0, "Нова зала", 0, 0);
    	manager = new HallsManager();        
    }
	public void getAllHalls(){
		halls = HallsManager.getAllHalls();
		
		if( halls==null){
        	halls = new ArrayList<Hall>();
        	System.out.println("halls was null");
        	if( selectedHall==null ){
            	selectedHall =  new Hall(0, "Нова зала1", 0, 0);;
            	System.out.println("selectedHall was null");
            }        	
        	halls.add(selectedHall);        
        }else{
        
        }
		setSelectedHall(halls.get(0));
        System.out.println("newHall = "+newHall);
        System.out.println("selHall = "+selectedHall);
        System.out.println("halls = "+halls);        
	}
	
	public void update(){
		//manager.setSelectedHall(selectedHall);
		System.out.println(selectedHall.getId());
		manager.update(selectedHall);		
		getAllHalls();
	}
	
	public void insert(){
		System.out.println("Inserting...");
		Hall hall = new Hall( newHall.getId(), newHall.getName(), newHall.getFloor(), newHall.getCapacity());
		halls.add(hall);		
		setSelectedHall(hall);		
		manager.save(hall);
		getAllHalls();
		
	}
	
	public void delete(){
		/*
		manager.setSelectedHall(selectedHall);
		System.out.println(manager.getSelectedHall().getName());*/
		System.out.println(selectedHall.getId());
		
		manager.delete(selectedHall);
		getAllHalls();		
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);        
    }
	
	public void onRowSelect(SelectEvent event) {
		setSelectedHall(((Hall) event.getObject()));
		System.out.println(this.selectedHall.getId());		
    }
}