package com.fsre.attendance_tracker_backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private Person person;

    public UserPrincipal(Person person) {
        this.person = person;
    }

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(person.getRole().name()));
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (person.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (person.isCompanyAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_COMPANY_ADMIN"));
        }
        if (person.isSupervisor()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));
        }
        if (person.isWorker()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_WORKER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
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
}