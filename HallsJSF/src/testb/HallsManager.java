package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class HallsManager implements Serializable{	
	private static final long serialVersionUID = 1L;	
	
	private Hall selectedHall;
	private ArrayList<Hall> halls = null;
	private String output;
	
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public ArrayList<Hall> getHalls() {
		halls = getAllHalls();
		return halls;
	}
	public Hall getSelectedHall() {
		return selectedHall;
	}
	public void setSelectedHall(Hall selectedHall) {
		this.selectedHall = selectedHall;
	}	
      
	public static ArrayList<Hall> getAllHalls(){
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			ArrayList<Hall> arr  = oCon.getAllHalls();
			oCon.close();			
			return arr;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e){
			e.printStackTrace();
			addMessage("Error in database",e.getMessage());
			return null;
		}   	
    }	 
	
    @PostConstruct
    public void init() {
    	
    }   
     
    public void save(Hall hall) {
    	if( hall!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.insertHall(hall) ){
					addMessage("Hall added successfully", hall.getName());
				}else addMessage("Error" , "Error in database(insert)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, enter a hall!");
    }
    
    public void update(Hall hall) {
    	//selectedHall = hall;
    	if( hall!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.updateHall(hall) ){
					addMessage("Update successful", hall.getName());
				}else addMessage("Error" , "Error in database(update)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, choose a hall!");
    }
     
    public void delete(Hall hall) {
    	if( hall!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.deleteHall(hall) ){
					addMessage("Delete successful", hall.getName());
				}else addMessage("Error" , "Error in database(delete)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, choose a hall!");
    }
       
    public static void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public void submit(){
    	output = selectedHall.getName();
    }
}
