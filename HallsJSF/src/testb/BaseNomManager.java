package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class BaseNomManager implements Serializable{	
	private static final long serialVersionUID = 1L;	
	
	private BaseNomenclatureRow selectedHall;
	private ArrayList<BaseNomenclatureRow> bnr = null;
	private String nomName;
	public String getNomName() {
		return nomName;
	}
	public void setNomName(String nomName) {
		this.nomName = nomName;
	}
	public ArrayList<BaseNomenclatureRow> getRows() {
		bnr = getAllNomRows(this.nomName);
		return bnr;
	}
	public BaseNomenclatureRow getSelectedHall() {
		return selectedHall;
	}
	public void setSelectedHall(BaseNomenclatureRow selectedHall) {
		this.selectedHall = selectedHall;
	}	
      
	public ArrayList<BaseNomenclatureRow> getAllNomRows(String name){
    	JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			ArrayList<BaseNomenclatureRow> arr  = oCon.getAllNomRows(name);
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
     
    public void save(BaseNomenclatureRow row) {
    	if( row!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.insertIntoNom(row) ){
					addMessage("New row added successfully", row.getDesc());
				}else addMessage("Error" , "Error in database(insert)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, enter a value!");
    }
    
    public void update(BaseNomenclatureRow row) {
    	if( row!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.updateNom(row) ){
					addMessage("Update successful", row.getDesc());
				}else addMessage("Error" , "Error in database(update)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, enter a value!");
    }
     
    public void delete(BaseNomenclatureRow row) {
    	if( row!=null ){
	    	JdbcConnector oCon;
			try {
				oCon = new JdbcConnector();				
				if( oCon.deleteFromNom(row) ){
					addMessage("Row successfully deleted", row.getDesc());
				}else addMessage("Error" , "Error in database(delete)");
				oCon.close();
								
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
			} catch (SQLException e){
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}else addMessage("Error", "Please, enter a value!");
    }
       
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }    
}
