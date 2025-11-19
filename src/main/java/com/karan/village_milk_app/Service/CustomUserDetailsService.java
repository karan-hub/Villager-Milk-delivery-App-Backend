package com.karan.village_milk_app.Service;

import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.model.Type.Role;
import com.karan.village_milk_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        User user = userRepository.findByPhone(phone);

        if (user == null)
            throw new UsernameNotFoundException("User not found with phone: " + phone);

        Role userRole = (user.getRole() != null) ? user.getRole() : Role.ROLE_USER;

        GrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());

        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}

