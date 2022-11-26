package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserValidator userValidator;

    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/get")
    public String getAllUsers(Model model, Principal principal, @ModelAttribute("user") User user) {
        User user1 = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user1);
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("rolesList", userService.listRoles());
        model.addAttribute("newUser", new User());
        return "adminPage";
    }

    @PostMapping("/update/{id}")
    public String update_2(@PathVariable("id") int id,
                           @ModelAttribute("user") @Valid User user) {
        userService.update(id, user);
        return "redirect:/admin/get";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/add_user")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult
            , Model model
    ) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roleList", userService.listRoles());
            return "redirect:/admin/get";
        }
        userService.createUser(user);
        return "redirect:/admin/get";
    }

    @GetMapping("/{id}")
    public String showInfo(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.get(id));
        return "adminUserPage";
    }
}
