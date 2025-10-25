package com.devrezaur.main.controller;

import com.devrezaur.main.model.Feedback;
import com.devrezaur.main.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class AppController {

    private final int PAGE_SIZE = 3;
    private final FeedbackService feedbackService;

    @GetMapping({"/", "/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Feedback> feedbackPage = feedbackService.getFeedbacks(pageable);

        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);

        if(httpServletRequest.isUserInRole("ADMIN")) {
            model.addAttribute("isAdmin", true);
        }

        return "home-page";
    }

    @GetMapping("/search")
    public String searchFeedbacks(@RequestParam String searchQuery, @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Feedback> feedbackPage = feedbackService.searchFeedbacksByName(searchQuery, pageable);
        
        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "home-page";
    }
//    UserDashboard
    @GetMapping("/user/dashboard")
    public String userDashboardPage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest) {
        String userName = httpServletRequest.getUserPrincipal().getName();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Feedback> feedbackPage = feedbackService.searchFeedbacksByUser(userName, pageable);
        
        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "user-dashboard-page";
    }

// Post Feedback --only user
    @GetMapping("/user/feedback")
    public String addFeedbackPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("mode", "add");
        return "post-page";
    }

    @PostMapping("/user/feedback")
    public String addFeedback(@ModelAttribute Feedback feedback, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        feedback.setFeedbackBy(username);
        feedback.setCreatedAt(currentTime);
        feedbackService.saveFeedback(feedback);
        return "redirect:/home";
    }

//  Edit feedback -- GetMapping and post mapping  -- only user editable
    @GetMapping("/user/feedback/{feedbackId}/edit")
    public String editFeedbackPage(@PathVariable UUID feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        model.addAttribute("feedback", feedback);
        model.addAttribute("mode", "edit");
        return "post-page";
    }

    @PostMapping("/user/feedback/{feedbackId}/edit")
    public String updateFeedback(@PathVariable UUID feedbackId, @ModelAttribute Feedback feedback, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        LocalDateTime currentTime = LocalDateTime.now();
        feedback.setFeedbackBy(username);
        feedback.setCreatedAt(currentTime);
        feedback.setFeedbackId(feedbackId);
        feedbackService.saveFeedback(feedback);
        return "redirect:/home";
    }


//      Delete -- user and admin
    @PostMapping("/feedback/{feedbackId}/delete")
    public String deleteFeedback(@PathVariable UUID feedbackId) {
        feedbackService.deleteFeedbackById(feedbackId);
        return "redirect:/home";
    }


//      See Full Review
    @GetMapping("/feedback/{feedbackId}")
    public String feedbackDetailPage(@PathVariable UUID feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        model.addAttribute("feedback", feedback);
        return "feedback-page";
    }
}
