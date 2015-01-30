package testb;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class UserClientManager implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<UserClient> arr;
	private UserClient selected = new UserClient("", "", "", "");
		
	public ArrayList<UserClient> getArr() {
		return arr;
	}
	public void setArr(ArrayList<UserClient> arr) {
		this.arr = arr;
	}

	public UserClient getSelected() {
		return selected;
	}
	public void setSelected(UserClient selected) {
		this.selected = selected;
	}

	@PostConstruct
	public void Init(){
		getAllUsers();
		if(arr.size()>0){
			setSelected(arr.get(0));
		}
	}
	
	public void getAllUsers(){
		try {
			JdbcConnector oCon = new JdbcConnector();
			arr = oCon.getAllUsers();
			oCon.close();
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void update(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();			
			if(oCon.updateUser(selected)){
				addMessage("Update", selected.getUserRealName()+" Successful",FacesMessage.SEVERITY_INFO);
			}else{
				addMessage("Update", selected.getUserRealName()+" failed. No such user",FacesMessage.SEVERITY_ERROR);
			}
			oCon.close();
			getAllUsers();
			
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	public void insert(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();
			if( oCon.isUserAvailable(selected.getUsername())){
				if( oCon.insertClient(selected)){
					addMessage("Adding","Successful",FacesMessage.SEVERITY_INFO);
				}
			}else{
				addMessage("Error", "User name is not available", FacesMessage.SEVERITY_ERROR);
			}
			oCon.close();
			getAllUsers();
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}	
	}
	public void delete(){
		JdbcConnector oCon;
		try {
			oCon = new JdbcConnector();			
			if( oCon.deleteUser(selected)){
				addMessage("Delete",selected.getUserRealName()+" Successful",FacesMessage.SEVERITY_INFO);
			}
			oCon.close();
			getAllUsers();
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
			addMessage("Error in database",e.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public static void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}