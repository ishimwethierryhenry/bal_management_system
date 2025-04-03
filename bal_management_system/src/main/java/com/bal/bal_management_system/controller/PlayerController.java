package com.bal.bal_management_system.controller;

import com.bal.bal_management_system.model.Player;
import com.bal.bal_management_system.service.PlayerService;
import com.bal.bal_management_system.service.PositionService;
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
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PositionService positionService;

    // Public endpoint for viewing players
    @GetMapping("/players")
    public String viewPlayers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<Player> players = playerService.findPlayers(search,
                PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));

        addCommonModelAttributes(model, players, search, page, sortBy, sortDir);
        return "public/players/list";
    }

    // Admin endpoint for managing players
    @GetMapping("/admin/players")
    public String managePlayers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<Player> players = playerService.findPlayers(search,
                PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));

        addCommonModelAttributes(model, players, search, page, sortBy, sortDir);
        return "admin/players/list";
    }

    // Helper method to add common model attributes
    private void addCommonModelAttributes(Model model, Page<Player> players, String search,
                                          int page, String sortBy, String sortDir) {
        model.addAttribute("players", players);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    }

    // Admin-only endpoints below
    @GetMapping("/admin/players/add")
    public String showAddForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("positions", positionService.getAllPositions());
        return "admin/players/add";
    }

    @PostMapping("/admin/players/add")
    public String addPlayer(
            @Valid @ModelAttribute Player player,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("positions", positionService.getAllPositions());
            return "admin/players/add";
        }

        playerService.savePlayer(player, imageFile);
        return "redirect:/admin/players";
    }

    @GetMapping("/admin/players/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.getPlayer(id));
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("positions", positionService.getAllPositions());
        return "admin/players/edit";
    }

    @PostMapping("/admin/players/edit/{id}")
    public String updatePlayer(
            @PathVariable Long id,
            @Valid @ModelAttribute Player player,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("teams", teamService.getAllTeams());
            model.addAttribute("positions", positionService.getAllPositions());
            return "admin/players/edit";
        }

        playerService.updatePlayer(player, imageFile);
        return "redirect:/admin/players";
    }

    @GetMapping("/admin/players/delete/{id}")
    public String deletePlayer(@PathVariable Long id) throws IOException {
        playerService.deletePlayer(id);
        return "redirect:/admin/players";
    }
}