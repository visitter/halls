package testb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
@ManagedBean
@ApplicationScoped
public class UserLoginView implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;     
    private String password;
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    private String isAdmin;
    public String getIsAdmin() {
		return isAdmin;
	}

	@PostConstruct
    public void init(){
    	getLoggedUser();
    }
       
    public String logout() {
    	System.out.println("session");
    	System.out.println( FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() );
    	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
    
    public String getLoggedUser() {
    	System.out.println("logged user = "+FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
    	setUsername( FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() );
    	if(username!=null)
    		isAdmin = ((Boolean)username.equalsIgnoreCase("admin")).toString(); 
    	return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
}