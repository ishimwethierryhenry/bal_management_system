// MatchController.java
package com.bal.bal_management_system.controller;

import com.bal.bal_management_system.model.Match;
import com.bal.bal_management_system.service.MatchService;
import com.bal.bal_management_system.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class MatchController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private TeamService teamService;

    // Public endpoint for viewing matches
    @GetMapping("/matches")
    public String viewMatches(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "matchDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Page<Match> matches = matchService.findMatches(search,
                PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));

        addCommonModelAttributes(model, matches, search, page, sortBy, sortDir);
        return "public/matches/list";
    }

    // Admin endpoint for managing matches
    @GetMapping("/admin/matches")
    public String manageMatches(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "matchDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Page<Match> matches = matchService.findMatches(search,
                PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));

        addCommonModelAttributes(model, matches, search, page, sortBy, sortDir);
        return "admin/matches/list";
    }

    private void addCommonModelAttributes(Model model, Page<Match> matches, String search,
                                          int page, String sortBy, String sortDir) {
        model.addAttribute("matches", matches);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    }

    @GetMapping("/admin/matches/add")
    public String showAddForm(Model model) {
        model.addAttribute("match", new Match());
        model.addAttribute("teams", teamService.getAllTeams());
        return "admin/matches/add";
    }

    @PostMapping("/admin/matches/add")
    public String addMatch(
            @Valid @ModelAttribute Match match,
            BindingResult result,
            @RequestParam("flyerFile") MultipartFile flyerFile,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("teams", teamService.getAllTeams());
            return "admin/matches/add";
        }

        matchService.saveMatch(match, flyerFile);
        return "redirect:/admin/matches";
    }

    @GetMapping("/admin/matches/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("match", matchService.getMatch(id));
        model.addAttribute("teams", teamService.getAllTeams());
        return "admin/matches/edit";
    }

    @PostMapping("/admin/matches/edit/{id}")
    public String updateMatch(
            @PathVariable Long id,
            @Valid @ModelAttribute Match match,
            BindingResult result,
            @RequestParam("flyerFile") MultipartFile flyerFile,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("teams", teamService.getAllTeams());
            return "admin/matches/edit";
        }

        matchService.updateMatch(match, flyerFile);
        return "redirect:/admin/matches";
    }

    @GetMapping("/admin/matches/delete/{id}")
    public String deleteMatch(@PathVariable Long id) throws IOException {
        matchService.deleteMatch(id);
        return "redirect:/admin/matches";
    }
}