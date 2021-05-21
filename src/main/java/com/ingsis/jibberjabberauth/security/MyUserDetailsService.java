package com.ingsis.jibberjabberauth.security;

import com.ingsis.jibberjabberauth.models.User;
import com.ingsis.jibberjabberauth.repository.UserRepository;
import com.ingsis.jibberjabberauth.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw new UsernameNotFoundException(userName + "not found");
        }
        return new MyUserDetails(user);
    }
}
