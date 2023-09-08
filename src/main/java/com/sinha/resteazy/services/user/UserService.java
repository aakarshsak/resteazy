package com.sinha.resteazy.services.user;

import com.sinha.resteazy.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void addNewUser(User user);

    User findUserById(long id);

    User deleteUserById(long id);
}
