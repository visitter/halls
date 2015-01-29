package testb;


import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;


@ManagedBean
@ApplicationScoped
public class RegisterUser {
	private String username;
	private String password1;
	private String password2;
	private String mail;	
	private String realname;
	
	public String getRealname() {
		return realname;
	}


	public void setRealname(String realname) {
		this.realname = realname;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword1() {
		return password1;
	}


	public void setPassword1(String password1) {
		this.password1 = password1;
	}


	public String getPassword2() {
		return password2;
	}


	public void setPassword2(String password2) {
		this.password2 = password2;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	@PostConstruct
	public void Init(){
		
	}
	
	public void register(javax.faces.event.ActionEvent ae){
		if( validateUserName() &&
				validatePass1() &&
					validatePass2() &&
						validateMail() &&
							validateRealUserName()){
		
			JdbcConnector oCon=null;
			try {
				oCon = new JdbcConnector();
				
				if( oCon.isUserAvailable(username)){
					if( oCon.insertClient(new UserClient(getUsername(), getPassword1(), getMail(), getRealname()))){
						addMessage("Success", "User created");
					};
				}else{
					addMessage("Error", "User name is not available");
				}
				oCon.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();			
				addMessage("Error in database",e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
				addMessage("Error in database",e.getMessage());
			}
		}
	}

	public Boolean validateUserName(){
		System.out.println("Validating name");
		if(getUsername().length()<4){			
			addMessage("Username invalid","Minimum 4 symbols required");
			return false;
		}else return true;
	}
	public Boolean validatePass1(){
		if(getPassword1().length()==0){
			addMessage("Invalid password","");
			return false;
		}else return true;
	}
	public Boolean validatePass2(){
		if(getPassword2().length()==0){
			addMessage("Invalid repeat password","");
			return false;
		}else return true;
	}
	public Boolean validateMail(){
		if(getMail().length()==0 ){
			addMessage("Invalid E-mail","");
			return false;
		}else return true;
	}
	public Boolean validateRealUserName(){
		System.out.println("Validating name");
		if(getRealname().length()<4){			
			addMessage("First name and Last name invalid","Minimum 4 symbols required");
			return false;
		}else return true;
	}
	public void addMessage(String summary, String detail) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	}	
  }

