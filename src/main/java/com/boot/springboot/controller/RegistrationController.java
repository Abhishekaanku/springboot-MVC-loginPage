package com.boot.springboot.controller;

import com.boot.springboot.model.User;
import com.boot.springboot.model.UserSession;
import com.boot.springboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;


@Controller
public class RegistrationController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserSession userSession;

    @RequestMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute("user", new User());

        return "register";

    }

    @PostMapping("/register")
    public String addUser(RedirectAttributes model, @RequestPart("profilePic") Part profilePic,
                          @ModelAttribute("user") @Valid User user, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("message", "Invalid Input");
            return "register";
        }
        String filePath = "/home/aparna/test/dp/" + profilePic.getSubmittedFileName();
        profilePic.write(filePath);
        user.setProfilePicture(filePath);

        userServiceImpl.register(user);

        userSession.logIn(user.getUserName());

        model.addAttribute("userName", user.getUserName());
        return "redirect:/users/{userName}";
    }
}
