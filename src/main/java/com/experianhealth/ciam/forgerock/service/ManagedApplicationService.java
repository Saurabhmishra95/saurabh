package com.experianhealth.ciam.forgerock.service;

import java.util.List;

import com.experianhealth.ciam.forgerock.model.FRQuery;

public interface ManagedApplicationService {
    List<ApplicationDetails> search(String token, FRQuery query);
}
