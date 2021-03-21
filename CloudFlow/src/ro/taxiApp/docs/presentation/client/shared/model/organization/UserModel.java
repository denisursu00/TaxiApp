package ro.taxiApp.docs.presentation.client.shared.model.organization;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


public class UserModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
    
	private Long id;
	private String firstName;
	private String lastName;
	private String password;
	private String username;
	private String email;
    private String mobile;
    private List<RoleModel> roles;  
    
    public UserModel() {}

    public UserModel(String userId, String username) {
        this();
        setId(Long.getLong(userId));
        setUsername(username);
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleModel> roles) {
		this.roles = roles;
	}

	@Override
    public String toString() {
    	return ("Utilizator (" + getUsername() + ")");
    }
}