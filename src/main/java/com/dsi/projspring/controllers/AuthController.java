package com.dsi.projspring.controllers;

import com.dsi.projspring.dtos.ChangePasswordRequest;
import com.dsi.projspring.dtos.LoginRequest;
import com.dsi.projspring.dtos.RegisterRequest;
import com.dsi.projspring.dtos.UserResponseDTO;
import com.dsi.projspring.entities.*;
import com.dsi.projspring.services.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequest request) {
       return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getMe(userDetails.getUsername()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal User user, // Spring matches the logged-in user here
            @RequestBody ChangePasswordRequest request) {

        // Now user.getId() actually gives us the Long ID needed by the service
        authService.changePassword(user.getId(), request);
        return ResponseEntity.ok().build();
    }
}
