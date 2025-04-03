// PlayerService.java
package com.bal.bal_management_system.service;

import com.bal.bal_management_system.model.Player;
import com.bal.bal_management_system.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    private final Path uploadPath = Paths.get("/home/ec2-user/Uploads/players");

    public PlayerService() {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public Page<Player> findPlayers(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return playerRepository.searchPlayers(search, pageable);
        }
        return playerRepository.findAll(pageable);
    }

    public Player savePlayer(Player player, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filepath = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), filepath);
            player.setProfileImage("/uploads/players/" + filename);
        }
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player player, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (player.getProfileImage() != null) {
                Path oldImage = uploadPath.resolve(player.getProfileImage().substring("/uploads/players/".length()));
                Files.deleteIfExists(oldImage);
            }
            // Save new image
            String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filepath = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), filepath);
            player.setProfileImage("/uploads/players/" + filename);
        }
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) throws IOException {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        if (player.getProfileImage() != null) {
            Path imagePath = uploadPath.resolve(player.getProfileImage().substring("/uploads/players/".length()));
            Files.deleteIfExists(imagePath);
        }
        playerRepository.deleteById(id);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public Page<Player> getPlayersByTeam(Long teamId, Pageable pageable) {
        return playerRepository.findByTeamId(teamId, pageable);
    }
}