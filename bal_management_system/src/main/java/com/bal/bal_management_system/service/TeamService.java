package com.bal.bal_management_system.service;

import com.bal.bal_management_system.model.Team;
import com.bal.bal_management_system.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    // Create
    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    // Read
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Paginated and Searchable Teams
    public Page<Team> findTeams(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return teamRepository.findAll(pageable);
        }
        // Use a custom method or modify repository to support search
        List<Team> searchResults = teamRepository.searchTeams(search);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), searchResults.size());

        // Create a Page manually
        return new PageImpl<>(searchResults.subList(start, end), pageable, searchResults.size());
    }

    // Update
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }

    // Delete
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    // Count total teams
    public long countTotalTeams() {
        return teamRepository.count();
    }
}