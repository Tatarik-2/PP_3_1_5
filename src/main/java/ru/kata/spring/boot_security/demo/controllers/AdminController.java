package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


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
    public String getAllUsers(Model model, Principal principal, @ModelAttribute ("user") User user) {
        User user1 = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user1);
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("rolesList", userService.listRoles());
        model.addAttribute("newUser", new User());
        return "adminPage";
    }

    @PostMapping("/update/{id}")
    public String update_2(@PathVariable("id") int id, /*User user*/
                           @ModelAttribute("user") @Valid User user) {
        List<String> lsr = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        List<Role> liRo = userService.listByRole(lsr);
        user.setRoles(liRo);
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
        List<String> lsr = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        List<Role> liRo = userService.listByRole(lsr);
        user.setRoles(liRo);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roleList", userService.listRoles());
//            return "addUser";
//            return "redirect:/admin/add_user";
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

    //    @GetMapping("/get")
//    public String getAllUsers(Model model) {
//        model.addAttribute("allUsers", userService.getAllUsers());
//        System.out.println(userService.getAllUsers());
//        return "showUsers";
//    }

//    @GetMapping("/add_user")
//    public String addUser(@ModelAttribute("user") User user, Model model) {
//        model.addAttribute("roleList", userService.listRoles());
//        return "addUser";
//    }
//
//    @GetMapping("/hello")
//    public String sayHello() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
////        Hibernate.initialize(user.getUser());
//        System.out.println(user.getUser());
//        return "hello";
//    }

//    @GetMapping("/update/{id}")
//    public String update(@PathVariable("id") int id, Model model) {
//        model.addAttribute(userService.get(id));
//        model.addAttribute("roleList", userService.listRoles());
//        return "updateUser";
//    }

}
