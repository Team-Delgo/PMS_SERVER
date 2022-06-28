package com.pms.comm.security.services;

import com.pms.domain.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PrincipalDetails implements UserDetails{

	private final Admin admin;

    public PrincipalDetails(Admin admin){
        this.admin = admin;
    }

    public Admin getUser() {
		return admin;
	}

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        admin.getRoleList().forEach(r -> {
//        	authorities.add(()->{ return r;});
//        });
//        return authorities;
        return null;
    }
}
