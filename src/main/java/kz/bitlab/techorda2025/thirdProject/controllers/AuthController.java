package kz.bitlab.techorda2025.thirdProject.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Удаляет все данные из сессии
        return "redirect:/login"; // Перенаправление на страницу входа
    }
}
