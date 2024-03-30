package com.todo.app.todoapp.service;

import com.todo.app.todoapp.entities.User;
import com.todo.app.todoapp.repository.UserRepository;
import com.todo.app.todoapp.utils.UserLoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserLoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found " + username);
        }
        return new UserLoginDetails(user);

    }
}