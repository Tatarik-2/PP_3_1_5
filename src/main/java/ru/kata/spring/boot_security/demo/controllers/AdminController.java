package ru.kata.spring.boot_security.demo.controllers;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserValidator userValidator;

    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }



    @GetMapping("/hello")
    public String sayHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
//        Hibernate.initialize(user.getUser());
        System.out.println(user.getUser());
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

    @PostMapping("/get")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return "addUser";
        }
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
