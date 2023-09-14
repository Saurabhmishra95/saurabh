package com.experianhealth.ciam.portal.controller;

import com.experianhealth.ciam.portal.entity.PasswordUpdateRequest;
import com.experianhealth.ciam.portal.entity.PortalConfiguration;
import com.experianhealth.ciam.portal.service.PortalService;
import com.experianhealth.ciam.scimapi.utils.AuthorizationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(PortalController.PORTAL_PATH)
public class PortalController {

    public static final String CONFIGURATION_PATH = "/configuration";
    public static final String UPDATEPASSWORD_PATH = "/updatepassword";
    public static final String PORTAL_PATH = "/portal";
    private final PortalService portalService;

    @Autowired
    public PortalController(PortalService portalService) {
        this.portalService = portalService;
    }

    @GetMapping(CONFIGURATION_PATH)
    public ResponseEntity<PortalConfiguration> getConfiguration() {
        return ResponseEntity.ok(portalService.getConfiguration());
        }

    @PostMapping(UPDATEPASSWORD_PATH)
    public ResponseEntity<String> updatePassword(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody PasswordUpdateRequest verificationRequest
    ) {
        portalService.updatePassword(
                AuthorizationUtils.validateBearerToken(Optional.ofNullable(bearerToken)),
                verificationRequest.getCurrentPassword(),
                verificationRequest.getNewPassword()
        );

        // Password verification successful
        return ResponseEntity.ok("Password updated successfully.");
    }
}