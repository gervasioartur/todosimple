package com.gervasioartur.todosimple.services;

import com.gervasioartur.todosimple.models.User;
import com.gervasioartur.todosimple.repositories.TaskRepository;
import com.gervasioartur.todosimple.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(
                "User not found! id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    @Transactional
    public User create(User user){
        user.setId(null);
        user = this.userRepository.save(user);
        this.taskRepository.saveAll(user.getTasks());
        return  user;
    }

    @Transactional
    public User update(User user){
        User newUser =  findById(user.getId());
        newUser.setPassword(user.getPassword());
        return this.userRepository.save(newUser);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.userRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Can not delete, it may have been deleted!");
        }
    }
}
