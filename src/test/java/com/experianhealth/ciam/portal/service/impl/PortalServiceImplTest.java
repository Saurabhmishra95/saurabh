package com.experianhealth.ciam.portal.service.impl;

import com.experianhealth.ciam.CIAMTestBase;
import com.experianhealth.ciam.exception.CIAMPasswordException;
import com.experianhealth.ciam.forgerock.model.User;
import com.experianhealth.ciam.forgerock.service.ForgeRockAMService;
import com.experianhealth.ciam.forgerock.service.ManagedUserService;
import com.experianhealth.ciam.portal.service.PortalServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortalServiceImplTest extends CIAMTestBase {

    @Mock
    private ForgeRockAMService amService;

    @Mock
    private ManagedUserService managedUserService;

    private PortalServiceImpl portalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        portalService = new PortalServiceImpl(amService, managedUserService);
    }

    @Test
    void testUpdatePasswordSuccess() {
        String token = "fakeToken";
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";

        User userInfo = new User();
        userInfo.setUserName("username");
        userInfo.set_id("userId");

        when(amService.getUserInfo(token)).thenReturn(userInfo);
        when(amService.getAccessToken(any(), any(), any(), any())).thenReturn("fakeAccessToken");

        portalService.updatePassword(token, currentPassword, newPassword);

        
    }

    @Test
    void testUpdatePasswordInvalidCurrentPassword() {
        String token = "fakeToken";
        String currentPassword = "invalidPassword";
        String newPassword = "newPassword";

        User userInfo = new User();
        userInfo.setUserName("username");
        userInfo.set_id("userId");

        when(amService.getUserInfo(token)).thenReturn(userInfo);
        when(amService.getAccessToken(any(), any(), any(), any())).thenThrow(new RuntimeException("invalid_grant"));

        assertThrows(CIAMPasswordException.class, () -> portalService.updatePassword(token, currentPassword, newPassword));
    }

    @Test
    void testUpdatePasswordMatchingNewAndCurrent() {
        String token = "fakeToken";
        String currentPassword = "oldPassword";
        String newPassword = "oldPassword";

        User userInfo = new User();
        userInfo.setUserName("username");
        userInfo.set_id("userId");

        when(amService.getUserInfo(token)).thenReturn(userInfo);
        when(amService.getAccessToken(any(), any(), any(), any())).thenReturn("fakeAccessToken");

        assertThrows(CIAMPasswordException.class, () -> portalService.updatePassword(token, currentPassword, newPassword));
    }


}
