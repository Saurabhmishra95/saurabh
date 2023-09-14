package com.experianhealth.ciam.scimapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.experianhealth.ciam.forgerock.model.Application;
import com.experianhealth.ciam.forgerock.model.User;
import com.experianhealth.ciam.forgerock.service.ForgeRockAMService;
import com.experianhealth.ciam.forgerock.service.GeneralForgeRockIDMService;
import com.experianhealth.ciam.forgerock.service.ManagedUserService;

@RestController
@RequestMapping("/myapplication")
public class ApplicationController {

    @Autowired
    private ForgeRockAMService forgeRockAMService;

    @Autowired
    @Qualifier("managedUserServiceImpl")
    private GeneralForgeRockIDMService generalForgeRockIDMService;


    @Autowired
    private ManagedUserService userService;

    @GetMapping
    public ResponseEntity<Application> getApplicationDetails(
            @RequestHeader(value = "Authorization") String bearerToken) {
        
        // Fetch the user using the token
        User user = forgeRockAMService.getUserInfo(bearerToken);
        
        // Extract the user ID from the User object
        String userId = user.get_id();
        
        // Fetch the application details using the token and user ID
        Optional<Application> optionalApplication = generalForgeRockIDMService.getById(bearerToken, userId);
        
        if(!optionalApplication.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(optionalApplication.get());
    }




}
