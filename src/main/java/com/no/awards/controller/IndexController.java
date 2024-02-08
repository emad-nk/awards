package com.no.awards.controller;

import com.no.awards.MessageBroker;
import com.no.awards.repository.ActivityRepository;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.service.AwardsCacheService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class IndexController {

    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;
    private final MessageBroker messageBroker;
    private final AwardsCacheService awardsCacheService;

    @GetMapping()
    public String getIndex(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("queueMessages", messageBroker.getMessages());
        model.addAttribute("totalAwards", awardsCacheService.getTotalAwards());
        return "index";
    }
}
