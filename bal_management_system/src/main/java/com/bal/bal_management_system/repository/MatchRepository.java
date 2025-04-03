// MatchRepository.java
package com.bal.bal_management_system.repository;

import com.bal.bal_management_system.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE " +
            "LOWER(m.venue) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.homeTeam.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.awayTeam.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Match> searchMatches(@Param("search") String search, Pageable pageable);

    Page<Match> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId, Pageable pageable);
}
