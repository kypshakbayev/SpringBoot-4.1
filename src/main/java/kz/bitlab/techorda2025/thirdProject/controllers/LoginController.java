package kz.bitlab.techorda2025.thirdProject.controllers;

import jakarta.servlet.http.HttpSession;
import kz.bitlab.techorda2025.thirdProject.db.Users;
import kz.bitlab.techorda2025.thirdProject.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    UsersRepository usersRepository;

    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(@RequestParam String fullName, @RequestParam String email, @RequestParam String password, Model model) {
        Users newEmail = usersRepository.getUsersByEmail(email);
        if (newEmail != null) {
            model.addAttribute("doubleEmail", "Пользователь с таким email уже существует. Пожалуйста, используйте другой!");
            return "register";
        }

        Users newUser = Users.builder()
                .fullName(fullName)
                .email(email)
                .password(password)
                .build();
        usersRepository.save(newUser);
        return "redirect:/tasks";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Users currentUser = usersRepository.getUsersByEmail(email);
        String redirect = "";
        if (currentUser != null) {
            if (password.equals(currentUser.getPassword())) {
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpSession session = attr.getRequest().getSession();
                session.setAttribute("currentUser", currentUser);

                redirect = "redirect:/tasks";
            } else {
                model.addAttribute("error", "Неправильный пароль. Повторите еще!");
                return "login";
            }
        } else {
            model.addAttribute("error", "Пользователь с таким email не найден, зарегистрируйтесь пожалуйста!");
            return "login";
        }
        return redirect;
    }

}
