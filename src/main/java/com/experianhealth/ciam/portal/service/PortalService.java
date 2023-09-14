package com.experianhealth.ciam.portal.service;

import com.experianhealth.ciam.portal.entity.PortalConfiguration;

public interface PortalService {

    void updatePassword(String token, String previousPassword, String newPassword);

    PortalConfiguration getConfiguration();
}
