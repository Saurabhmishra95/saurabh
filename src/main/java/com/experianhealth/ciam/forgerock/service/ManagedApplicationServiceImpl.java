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



    // You can override other methods or add new methods specific to applications here.
}
