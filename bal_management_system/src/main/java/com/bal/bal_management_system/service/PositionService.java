// PositionService.java
package com.bal.bal_management_system.service;

import com.bal.bal_management_system.model.Position;
import com.bal.bal_management_system.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position savePosition(Position position) {
        return positionRepository.save(position);
    }
}