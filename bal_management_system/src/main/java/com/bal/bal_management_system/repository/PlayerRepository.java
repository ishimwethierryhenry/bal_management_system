// PlayerRepository.java
package com.bal.bal_management_system.repository;

import com.bal.bal_management_system.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT p FROM Player p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.nationality) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.position) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Player> searchPlayers(@Param("search") String search, Pageable pageable);

    Page<Player> findByTeamId(Long teamId, Pageable pageable);
}