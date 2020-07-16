package com.coronovirus.tracker.controllers;

import com.coronovirus.tracker.domain.LocationStats;
import com.coronovirus.tracker.service.CoronaVirusDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CoronaVirusDataService mainService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> stats = mainService.getAllStats();
        int totalReportedCases = stats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = stats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", stats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }


}
