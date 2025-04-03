// PositionRepository.java
package com.bal.bal_management_system.repository;

import com.bal.bal_management_system.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}