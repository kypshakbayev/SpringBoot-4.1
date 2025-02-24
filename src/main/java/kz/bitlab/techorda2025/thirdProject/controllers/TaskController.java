package kz.bitlab.techorda2025.thirdProject.controllers;

import jakarta.servlet.http.HttpSession;
import kz.bitlab.techorda2025.thirdProject.db.Task;
import kz.bitlab.techorda2025.thirdProject.db.Users;
import kz.bitlab.techorda2025.thirdProject.repositories.TaskRepository;
import kz.bitlab.techorda2025.thirdProject.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

//    @Autowired
//    private UsersRepository usersRepository;

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        model.addAttribute("currentUser", currentUser);

        if(currentUser == null) {
            model.addAttribute("noCurrentUser", "Зайдите под своим email, чтобы посмотреть задачи.");
        } else {
            model.addAttribute("view", "show");
            model.addAttribute("tasks", taskRepository.findAllByUser(currentUser));
        }
        return "tasks";
    }

    @GetMapping("/details/{idshka}")
    public String getDetailsOfTask(Model model,
                           @PathVariable("idshka") Long id) {
//        Task task = taskRepository.findById(id).orElse(null);
        Task task = new Task();
        if(taskRepository.findById(id).isPresent()) {
            task = taskRepository.findById(id).get();
        } else {
            task.setName("Default Task");
            task.setDescription("Default Description");
            task.setDeadlineDate("");
            task.setId(id);
            task.setCompleted(false);
        }
        model.addAttribute("task", task);
        return "details";
    }

    @PostMapping("/saveTask")
    public String saveTask(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "taskName") String taskname,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "date") String date,
                           @RequestParam(name = "complete") String complete) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        Task task = Task.builder()
                .id(id)
                .name(taskname)
                .description(description)
                .deadlineDate(date)
                .isCompleted(Boolean.parseBoolean(complete))
                .user(currentUser)
                .build();

        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @DeleteMapping(value = "/deleteTask")
    public String deleteTask(@RequestParam(name = "id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam(name = "taskName") String taskName,
                          @RequestParam(name= "taskDescription") String taskDescription,
                          @RequestParam(name = "date") String date,
                          @RequestParam(name = "complete", defaultValue = "false") String complete) {

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        Task task = Task.builder()
                .name(taskName)
                .description(taskDescription)
                .deadlineDate(date)
                .isCompleted(Boolean.parseBoolean(complete))
                .user(currentUser)
                .build();

        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping(value = "/search")
    public String search(@RequestParam(name = "searchValue") String searchValue, Model model) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Task> taskList = taskRepository.findAllByUserAndNameContainsIgnoreCase(currentUser, searchValue);
        model.addAttribute("tasks", taskList);
        model.addAttribute("currentUser", currentUser);

        return "tasks";
    }

}
