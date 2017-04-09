package tn.hich.app.security;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1678278523293226422L;
	
	private String authority;
	
	public Role(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String authority){
		this.authority = authority;
	}

}
