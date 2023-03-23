package com.tatiSmol.TaskList.controllers;

import com.tatiSmol.TaskList.model.Task;
import com.tatiSmol.TaskList.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
