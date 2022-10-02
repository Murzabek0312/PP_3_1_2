package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.UserValidator;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.service.ServiceUser;

import java.security.Principal;
import javax.validation.Valid;

@Controller

public class UsersController {

    private final ServiceUser serviceUser;

    private final UsersRepository usersRepository;

    private final UserValidator userValidator;




    @Autowired
    public UsersController(ServiceUser serviceUser, UsersRepository usersRepository,
                           UserValidator userValidator) {
        this.serviceUser = serviceUser;
        this.usersRepository = usersRepository;
        this.userValidator = userValidator;
    }


    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("users", serviceUser.getAll());
        return "users/getAll";
    }


    @GetMapping("/user")
    public String getUser(Model model, Principal principal) {
       String username = principal.getName();
        System.out.println(username);
       User user = new User();
       user = usersRepository.findByUsername(username);
       int id = user.getId();
        model.addAttribute("user", serviceUser.getUserbyId(id));
        return "users/getUser";
    }

    @GetMapping("/users/{id}")
    public String getUserForUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", serviceUser.getUserbyId(id));
        return "users/getUser";
    }

    @GetMapping("admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user")  @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            return "/users/new";
        }
        serviceUser.add(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/{id}/edit")
    public String editUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", serviceUser.getUserbyId(id));
        model.addAttribute("listRoles", serviceUser.listRoles());
        return "users/edit";

    }

    @PatchMapping("admin/{id}")
    public String edit(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        userValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            return "users/edit";
        } else
        serviceUser.edit(id, user);
        return "redirect:/admin";
    }
    @DeleteMapping("admin/{id}")
    public String delete(@PathVariable("id") int id) {
        serviceUser.delete(id);
        return "redirect:/admin";
    }


}
