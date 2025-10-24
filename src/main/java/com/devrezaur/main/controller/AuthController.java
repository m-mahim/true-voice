package com.devrezaur.main.controller;

import com.devrezaur.main.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class AuthController {

  private final UserService userService;

  @GetMapping("/login")
  public String loginPage(@RequestParam @Nullable String logout, @RequestParam @Nullable String error, Model model) {
    if (logout != null && logout.equals("true")) {
      model.addAttribute("message", "Logout successful");
    } else if (error != null && error.equals("true")) {
      model.addAttribute("error", "Invalid credentials!");
    }

    return "login-page";
  }

  @GetMapping("/register")
  public String registrationPage() {
    return "registration-page";
  }

  @PostMapping("/register")
  public String doRegisterUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
    try {
      userService.registerUser(username, password);
      redirectAttributes.addFlashAttribute("message", "Registration Successful!");
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
    }

    return "redirect:/register";
  }

  @GetMapping("/dashboard")
  public String dashboardPage(HttpServletRequest httpServletRequest) {
    if (httpServletRequest.isUserInRole("USER")) {
      return "redirect:/user/dashboard";
    }

    return "redirect:/";
  }

}
