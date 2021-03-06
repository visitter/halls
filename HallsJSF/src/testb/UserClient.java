package testb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class UserClient implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String email;
	private String userRole = "user";
	private String userRealName;
	
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public UserClient( String username, String userpass, String email, String userRealName){
		setUsername(username);
		setPassword(userpass);
		setEmail(email);
		setUserRealName(userRealName);
	}

}
