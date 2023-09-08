package com.sinha.resteazy.services.user;

import com.sinha.resteazy.daos.UserRepository;
import com.sinha.resteazy.entities.User;
import com.sinha.resteazy.exceptions.DuplicateEntryException;
import com.sinha.resteazy.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addNewUser(User user) {
        try {
            userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new DuplicateEntryException("Phone/Email");
        }
    }

    @Override
    public User findUserById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("User does not exist...");
        }
        return optionalUser.get();
    }

    @Override
    public User deleteUserById(long id) {
        User user = findUserById(id);
        userRepository.delete(user);
        return user;
    }
}
