package com.sinha.resteazy.services.user;

import com.sinha.resteazy.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();
    void addNewUser(User user);
    User findUserById(long id);
    User deleteUserById(long id);
    public User loadUserByUsername(String username);
}
