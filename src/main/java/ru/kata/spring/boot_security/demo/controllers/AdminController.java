package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/{id}")
    public String showInfo(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.get(id));
        return "info";
    }


    @GetMapping("/get")
    public String getAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        System.out.println(userService.getAllUsers());
        return "showUsers";
    }

    @GetMapping("/add_user")
    public String addUser(@ModelAttribute("user") User user) {
//        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/update/get")
    public String create(@ModelAttribute("user") User user) {
//        user.setId(2);
        userService.createUser(user);
        return "redirect:/admin/get";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/get";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute(userService.get(id));
        return "updateUser";
    }

    @PostMapping("/update/{id}")
    public String update_2(@PathVariable("id") int id, User user) {
        userService.update(id, user);
        return "redirect:/admin/get";
    }
}
