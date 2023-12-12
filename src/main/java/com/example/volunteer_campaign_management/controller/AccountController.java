package com.example.volunteer_campaign_management.controller;

import com.example.volunteer_campaign_management.dtos.AccountDTO;
import com.example.volunteer_campaign_management.dtos.LoginDTO;
import com.example.volunteer_campaign_management.dtos.ProfileDTO;
import com.example.volunteer_campaign_management.entities.AccountEntity;
import com.example.volunteer_campaign_management.jwts.AccountDetails;
import com.example.volunteer_campaign_management.jwts.JwtResponse;
import com.example.volunteer_campaign_management.jwts.JwtUtils;
import com.example.volunteer_campaign_management.services.AccountService;
import com.example.volunteer_campaign_management.services.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer-campaign-management/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {
    private final JwtUtils jwtUtils;
    private final AccountService accountService;
    private final ProfileService profileService;
    private static String imageDirectory = System.getProperty("user.dir" + "/images/");
    @Autowired
    private final AuthenticationManager authenticationManager;
    @PostMapping("/Login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        AccountDetails userDetails = (AccountDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(jwt);
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setUserId(userDetails.getAccountId());
        jwtResponse.setUserId(userDetails.getAccountId());
        jwtResponse.setStatus(userDetails.getStatus());
        jwtResponse.setRole(userDetails.getAuthority().getAuthority());
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/accounts")
    public AccountDTO createNewAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.createNewAccount(accountDTO);
    }

    @GetMapping("/account/{id}")
    public AccountDTO getAccountById(@PathVariable(value = "id") int accountId) {
        return accountService.getAccountById(accountId);
    }

    @PutMapping("/account/{id}")
    public AccountDTO updateAccount(@PathVariable(value = "id") int accountId,
                                    @RequestBody AccountDTO updatedAccountDTO) {
        return accountService.updateAccount(accountId, updatedAccountDTO);
    }

    @PutMapping("{email}/enable")
    public ResponseEntity<String> enableAccount(@PathVariable String email) {
        accountService.enableAccount(email);
        return ResponseEntity.ok("Account enabled");
    }

    @PutMapping("{email}/disable")
    public ResponseEntity<String> disableAccount(@PathVariable String email) {
        accountService.disableAccount(email);
        return ResponseEntity.ok("Account disabled");
    }

    @GetMapping("/account/profile/{id}")
    public ProfileDTO getAccountProfileById(@PathVariable(value = "id") int profileId) {
        return profileService.getProfileById(profileId);
    }

    @PutMapping("/account/updateProfile/{id}")
    public AccountDTO updateProfile(@PathVariable(value = "id") int accountId, @RequestBody AccountDTO accountDTO) {
        return profileService.updateProfile(accountId,accountDTO);
    }

    @GetMapping (value = {"account/searchAccount","account/searchAccount/{query}"})
    public java.util.List<AccountDTO> searchAccount(@PathVariable(value = "query") Optional<String> query){
        return accountService.searchAccount(query);
    }

}
