package com.nchl.authorization_server.security;

import com.nchl.authorization_server.model.dto.customer.CustomerDetails;
import com.nchl.authorization_server.service.ICustomerDetailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("customUserDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final ICustomerDetailService customerDetailService;

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {

        CustomerDetails customerDetail = customerDetailService.getCustomerDetail(customerId);
        return new User(customerDetail.getCustId(), customerDetail.getPassword(), true, true,
                true, true, getAuthorities(customerDetail.getRoleCode()));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return getGrantedAuthorities(getRoles(role));
    }

    public List<String> getRoles(String role) {

        List<String> roles = new ArrayList<String>();

        if (role.equals("TXN001")) {
            roles.add("Transactionable Role");
        }
        return roles;
    }
}
