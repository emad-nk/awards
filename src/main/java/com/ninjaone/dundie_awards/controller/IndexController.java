package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.service.AwardsCacheService;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
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
        model.addAttribute("totalDundieAwards", awardsCacheService.getTotalAwards());
        return "index";
    }
}
