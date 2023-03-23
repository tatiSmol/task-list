package com.tatiSmol.TaskList.controllers;

import com.tatiSmol.TaskList.model.Task;
import com.tatiSmol.TaskList.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private TaskRepository repository;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/tasklist")
    public String taskList(Model model) {
        Iterable<Task> taskIterable = repository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

        for (Task task : taskIterable) {
            tasks.add(task);
        }
        tasks.sort(Comparator.comparing(Task::getDateTime));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Map<String, List<Task>> tasksByDate = new TreeMap<>();
        for (Task task : tasks) {
            String[] dataArr = task.getDateTime().format(formatter).split("T");
            tasksByDate.computeIfAbsent(dataArr[0], k -> new ArrayList<>()).add(task);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("tasksCount", tasks.size());
        model.addAttribute("tasksByDate", tasksByDate);

        return "task-list";
    }

    @RequestMapping("/addnew")
    public String addNewTask(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);

        return "newtask";
    }

    @RequestMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task) {
        repository.save(task);

        return "redirect:/tasklist";
    }

    @RequestMapping("/deletetask/{id}")
    public String deleteTask(@PathVariable(value = "id") int id) {
        repository.deleteById(id);

        return "redirect:/tasklist";
    }
}
