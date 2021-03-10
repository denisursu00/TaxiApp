package ro.cloudSoft.cloudDoc.web.security;

public class JwtSettings {

	private String secretKey;	
	private int tokenValidityInDays;
    private int tokenValidityInDaysForRememberMe;
    
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getTokenValidityInDays() {
		return tokenValidityInDays;
	}
	public void setTokenValidityInDays(int tokenValidityInDays) {
		this.tokenValidityInDays = tokenValidityInDays;
	}
	public int getTokenValidityInDaysForRememberMe() {
		return tokenValidityInDaysForRememberMe;
	}
	public void setTokenValidityInDaysForRememberMe(int tokenValidityInDaysForRememberMe) {
		this.tokenValidityInDaysForRememberMe = tokenValidityInDaysForRememberMe;
	}
}
