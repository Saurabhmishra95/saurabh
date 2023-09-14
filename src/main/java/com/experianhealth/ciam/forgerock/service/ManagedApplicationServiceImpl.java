package com.experianhealth.ciam.forgerock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.experianhealth.ciam.forgerock.model.Application;
import com.experianhealth.ciam.forgerock.model.FRQuery;

// Logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ManagedApplicationServiceImpl extends AbstractForgeRockIDMServiceImpl<ApplicationDetails> implements ManagedApplicationService {

    private static final String APPLICATION_PATH = "/openidm/managed/application";

    ManagedApplicationServiceImpl() {
        super(ApplicationDetails.class);
    }

    @Override
    String getBasePath() {
        return APPLICATION_PATH;
    }

    @Override
    public List<ApplicationDetails> search(String token, FRQuery query) {
        List<Application> applications = super.search(token, query);
        
        // Map the Application objects to ApplicationDetails objects
        List<ApplicationDetails> detailsList = applications.stream()
            .map(this::mapToApplicationDetails)
            .collect(Collectors.toList());

        return detailsList;
    }

    private ApplicationDetails mapToApplicationDetails(Application application) {
        ApplicationDetails details = new ApplicationDetails();
        
        details.setAppId(application.get_id());
        details.setAppName(application.getName());
        details.setAppDescription(application.get_ref()); // Adjust this if the description is stored elsewhere in the Application object
        
        return details;
    }

    // You can override other methods or add new methods specific to applications here.
}
