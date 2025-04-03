package com.bal.bal_management_system.repository;

import com.bal.bal_management_system.model.Team;
import com.google.api.gax.paging.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    // Custom query methods
    Optional<Team> findByName(String name);

    @Query("SELECT t FROM Team t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.coach) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Team> searchTeams(@Param("searchTerm") String searchTerm);

}