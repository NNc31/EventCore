package com.nefodov.eventcore.userservice.service;

import com.nefodov.eventcore.userservice.model.SignInRequest;
import com.nefodov.eventcore.userservice.model.SignUpRequest;
import com.nefodov.eventcore.userservice.model.User;
import com.nefodov.eventcore.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired AuthenticationManager authenticationManager,
                       @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticateAndGetUser(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        return userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean register(SignUpRequest request) {
        userRepository.save(new User(request.username(), passwordEncoder.encode(request.password()), request.email()));
        return true;
    }

    public boolean updateAvatar(String username, MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("image/png")) {
                throw new IllegalArgumentException("Unsupported file type");
            }
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("File is too large.");
            }
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("File is not a valid image.");
            }
            if (image.getWidth() > 512 || image.getHeight() > 512) {
                throw new IllegalArgumentException("Image dimensions are too large.");
            }

            Path uploadPath = Paths.get("uploads/avatars");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
            String filename = username + ".png";
            Path filePath = uploadPath.resolve(filename);

            ImageIO.write(image, "png", filePath.toFile());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }
}
