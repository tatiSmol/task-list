package com.tatiSmol.TaskList.controllers;

import com.tatiSmol.TaskList.model.Task;
import com.tatiSmol.TaskList.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository repository;

    @GetMapping("/tasklist/")
    public List<Task> list() {
        Iterable<Task> taskIterable = repository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

        for (Task task : taskIterable) {
            tasks.add(task);
        }

        return tasks;
    }

    @PostMapping("/tasklist/")
    public synchronized int add(Task task) {
        Task newTask = repository.save(task);
        return newTask.getId();
    }

    @DeleteMapping("/tasks/{id}")
    public synchronized ResponseEntity delete(@PathVariable int id) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        repository.deleteById(id);
        return new ResponseEntity("Task with id" + id + " has been deleted.", HttpStatus.OK);
    }
}
