package com.bal.bal_management_system.controller;

import com.bal.bal_management_system.model.AuditTrail;
import com.bal.bal_management_system.model.Team;
import com.bal.bal_management_system.repository.TeamRepository;
import com.bal.bal_management_system.service.TeamService;
import com.bal.bal_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showTeamList(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        int pageSize = 5;

        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = sortDir.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(page, pageSize);
        }

        Page<Team> teamPage = teamService.findTeams(search, pageable);

        model.addAttribute("teams", teamPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teamPage.getTotalPages());
        model.addAttribute("totalTeams", teamPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "admin_teams";
    }

    @GetMapping("/add")
    public String showAddTeamForm(Model model) {
        model.addAttribute("team", new Team());
        return "add_team";
    }

    @PostMapping("/add")
    public String addTeam(
            @Valid @ModelAttribute("team") Team team,
            BindingResult bindingResult,
            Model model
    ) {
        // Check for validation errors first
        if (bindingResult.hasErrors()) {
            return "add_team";
        }

        // Check if team name already exists
        if (teamService.getTeamByName(team.getName()).isPresent()) {
            model.addAttribute("error", "Team with this name already exists");
            return "add_team";
        }

        Team savedTeam = teamService.saveTeam(team);

        // Create and save audit trail (previous implementation)
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setAction("Team Created");
        auditTrail.setUsername(getCurrentUsername());
        auditTrail.setTimestamp(LocalDateTime.now());
        auditTrail.setDetails("Team " + savedTeam.getName() + " was created.");
        userService.saveAuditTrail(auditTrail);

        return "redirect:/admin/teams";
    }

    @GetMapping("/update/{id}")
    public String showUpdateTeamForm(@PathVariable("id") Long id, Model model) {
        Optional<Team> teamOptional = teamService.getTeamById(id);
        if (teamOptional.isPresent()) {
            model.addAttribute("team", teamOptional.get());
            return "update_team";
        }
        return "redirect:/admin/teams";
    }

    @PostMapping("/update/{id}")
    public String updateTeam(@PathVariable("id") Long id, @ModelAttribute("team") Team team, Model model) {
        Optional<Team> existingTeamOptional = teamService.getTeamById(id);
        if (existingTeamOptional.isEmpty()) {
            model.addAttribute("error", "Team not found");
            return "redirect:/admin/teams";
        }

        Team existingTeam = existingTeamOptional.get();
        existingTeam.setName(team.getName());
        existingTeam.setCountry(team.getCountry());
        existingTeam.setCoach(team.getCoach());
        existingTeam.setDescription(team.getDescription());

        Team updatedTeam = teamService.updateTeam(existingTeam);

        // Create and save audit trail
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setAction("Team Updated");
        auditTrail.setUsername(getCurrentUsername());
        auditTrail.setTimestamp(LocalDateTime.now());
        auditTrail.setDetails("Team " + updatedTeam.getName() + " was updated.");
        userService.saveAuditTrail(auditTrail);

        return "redirect:/admin/teams";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeam(@PathVariable("id") Long id) {
        Optional<Team> teamOptional = teamService.getTeamById(id);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();

            // Create and save audit trail
            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setAction("Team Deleted");
            auditTrail.setUsername(getCurrentUsername());
            auditTrail.setTimestamp(LocalDateTime.now());
            auditTrail.setDetails("Team " + team.getName() + " was deleted.");
            userService.saveAuditTrail(auditTrail);

            teamService.deleteTeam(id);
        }

        return "redirect:/admin/teams";
    }

    // Helper method to get current username (implement based on your authentication mechanism)
    private String getCurrentUsername() {
        // This is a placeholder. Replace with actual authentication context retrieval
        return "Admin";
    }
}