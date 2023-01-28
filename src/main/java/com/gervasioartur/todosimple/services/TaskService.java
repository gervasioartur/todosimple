package com.gervasioartur.todosimple.services;

import com.gervasioartur.todosimple.models.Task;
import com.gervasioartur.todosimple.models.User;
import com.gervasioartur.todosimple.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException(
                "Task not found, id: " + id + " Type: " + Task.class.getName()
        ));
    }

    @Transactional
    public Task create(Task task) {
        User user = this.userService.findById(task.getId());
        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task update(Task task) {
        Task newTask = findById(task.getId());
        newTask.setDescription(task.getDescription());
        newTask = this.taskRepository.save(newTask);
        return newTask;
    }

    public void delete(Long id) {
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Can not delete because there are related entities");
        }
    }
}

