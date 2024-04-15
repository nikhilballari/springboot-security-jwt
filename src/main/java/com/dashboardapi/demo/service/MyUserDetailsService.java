package com.dashboardapi.demo.service;

import com.dashboardapi.demo.entity.MyUserDetails;
import com.dashboardapi.demo.entity.User;
import com.dashboardapi.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        return user.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found "+userName));
    }
}
