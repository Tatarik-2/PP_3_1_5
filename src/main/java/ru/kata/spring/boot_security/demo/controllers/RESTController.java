package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class RESTController {
    private final UserService userService;
    private final UserValidator userValidator;

    public RESTController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


/*--------------------------------------------------------------------------*/
//    @GetMapping("/")
//    public String MainPage() {
//        return "redirect:/login";
//    }

//    @GetMapping("/admin")
//    public ModelAndView adminPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("adminPage");
//        return modelAndView;
//    }

//    @GetMapping("/user")
//    public ModelAndView userPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("adminUserPage");
//        return modelAndView;
//    }

    //Возвращаем список пользователей для заполнения форм страницы allusers
    @GetMapping("/admin")
    public ResponseEntity<List<User>> getUserForAdminPage() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Возвращаем юзера для заполнения форм страницы
    @GetMapping("/user")
    public ResponseEntity<User> getUser(Principal principal) {
        return new ResponseEntity<>(userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    //Возвращаем спилок существующих ролей
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.listRoles(), HttpStatus.OK);
    }

    //Получаем пользователя по id
    @GetMapping("/admin/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.get(id);
    }
    //создаем

    @PostMapping(value = "/admin")
    public ResponseEntity<User> addUserAction(@RequestBody User user, BindingResult bindingResult) {
        System.out.println(user);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);//Как вывести сообщение об ошибке?
        }
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //меняем, предварительно проверив, не было ли такого юзера в базе. Так нужно для проверки валидатора))
    @PutMapping("/admin")
    public ResponseEntity<User> updateUser(@RequestBody User user, BindingResult bindingResult) {
        if (!user.getUsername().equals(userService.get(user.getId()).getUsername())){
            userValidator.validate(user, bindingResult);
            if (bindingResult.hasErrors()){
                return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);//Как вывести сообщение об ошибке?
            }
        }

        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //удаляем
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
