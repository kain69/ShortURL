package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.karmazin.shorturl.config.jwt.JwtUtils;
import ru.karmazin.shorturl.model.ERole;
import ru.karmazin.shorturl.model.Role;
import ru.karmazin.shorturl.model.User;
import ru.karmazin.shorturl.pojo.JwtResponse;
import ru.karmazin.shorturl.pojo.LoginRequest;
import ru.karmazin.shorturl.pojo.MessageResponse;
import ru.karmazin.shorturl.pojo.SignupRequest;
import ru.karmazin.shorturl.repository.RoleRepository;
import ru.karmazin.shorturl.repository.UserRepository;
import ru.karmazin.shorturl.service.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Авторизация", description = "Методы для авторизации и регистрации")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRespository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "Авторизация")
    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @Operation(summary = "Регистрация")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        if (userRespository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }

        User user = new User(signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()));

//        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

//        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found"));
            roles.add(userRole);
//        } else {
//            reqRoles.forEach(r -> {
//                switch (r) {
//                    case "user" -> {
//                        Role userRole = roleRepository
//                                .findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found"));
//                        roles.add(userRole);
//                    }
//                    default -> {
//                        throw new RuntimeException("Error: Role not found");
//                    }
//                }
//
//            });
//        }
        user.setRoles(roles);
        userRespository.save(user);
        return ResponseEntity.ok(new MessageResponse("User CREATED with id: " + user.getId()));
    }
}
