// MatchService.java
package com.bal.bal_management_system.service;

import com.bal.bal_management_system.model.Match;
import com.bal.bal_management_system.repository.MatchRepository;
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
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    private final Path uploadPath = Paths.get("/home/ec2-user/Uploads/matches");

    public MatchService() {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public Page<Match> findMatches(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return matchRepository.searchMatches(search, pageable);
        }
        return matchRepository.findAll(pageable);
    }

    public Match saveMatch(Match match, MultipartFile flyer) throws IOException {
        if (flyer != null && !flyer.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + flyer.getOriginalFilename();
            Path filepath = uploadPath.resolve(filename);
            Files.copy(flyer.getInputStream(), filepath);
            match.setFlyerImage("/uploads/matches/" + filename);
        }
        return matchRepository.save(match);
    }

    public Match updateMatch(Match match, MultipartFile flyer) throws IOException {
        if (flyer != null && !flyer.isEmpty()) {
            if (match.getFlyerImage() != null) {
                Path oldFlyer = uploadPath.resolve(match.getFlyerImage().substring("/uploads/matches/".length()));
                Files.deleteIfExists(oldFlyer);
            }
            String filename = UUID.randomUUID().toString() + "_" + flyer.getOriginalFilename();
            Path filepath = uploadPath.resolve(filename);
            Files.copy(flyer.getInputStream(), filepath);
            match.setFlyerImage("/uploads/matches/" + filename);
        }
        return matchRepository.save(match);
    }

    public void deleteMatch(Long id) throws IOException {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        if (match.getFlyerImage() != null) {
            Path flyerPath = uploadPath.resolve(match.getFlyerImage().substring("/uploads/matches/".length()));
            Files.deleteIfExists(flyerPath);
        }
        matchRepository.deleteById(id);
    }

    public Match getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }
}
