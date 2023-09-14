package com.experianhealth.ciam.scimapi.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonPatch;

import com.experianhealth.ciam.exception.CIAMInvalidRequestException;
import com.experianhealth.ciam.scimapi.entity.Address;
import com.experianhealth.ciam.scimapi.entity.Email;
import com.experianhealth.ciam.scimapi.entity.Name;
import com.experianhealth.ciam.scimapi.entity.Operation;
import com.experianhealth.ciam.scimapi.entity.PhoneNumber;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserPatchBuilder {

    private final JsonArrayBuilder operations = Json.createArrayBuilder();
    private final ObjectMapper mapper = new ObjectMapper();

    public UserPatchBuilder replaceAttribute(String path, Object value) {
        if (value != null) {
            JsonObject operation = Json.createObjectBuilder()
                    .add("op", "replace")
                    .add("path", path)
                    .add("value", value.toString())
                    .build();
            operations.add(operation);
        }
        return this;
    }

    public UserPatchBuilder replaceUserName(String userName) {
        return replaceAttribute("/userName", userName);
    }

    public UserPatchBuilder replaceDisplayName(String displayName) {
        return replaceAttribute("/displayName", displayName);
    }

    public UserPatchBuilder replaceName(Name name) {
        if (name != null) {
            replaceAttribute("/givenName", name.getGivenName());
            replaceAttribute("/sn", name.getFamilyName());
        }
        return this;
    }

    public UserPatchBuilder replaceEmails(List<Email> emails) {
        if (emails != null && !emails.isEmpty()) {
            String emailAddressesAsString = emails.stream()
                    .map(Email::getValue)
                    .collect(Collectors.joining(","));
            replaceAttribute("/mail", emailAddressesAsString);
        }
        return this;
    }

    public UserPatchBuilder replacePhoneNumbers(List<PhoneNumber> phoneNumbers) {
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            String phoneNumberStrings = phoneNumbers.stream()
                    .map(PhoneNumber::getValue)
                    .collect(Collectors.joining(","));
            replaceAttribute("/telephoneNumber", phoneNumberStrings);
        }
        return this;
    }

    public UserPatchBuilder replaceAddress(Address address) {
        if (address != null) {
            replaceAttribute("/postalAddress", address.getLocality());
            replaceAttribute("/stateProvince", address.getRegion());
            replaceAttribute("/postalCode", address.getPostalCode());
            replaceAttribute("/country", address.getCountry());
            replaceAttribute("/primary", String.valueOf(address.isPrimary()));
        }
        return this;
    }

    public UserPatchBuilder replaceAddress(Map<String, Object> addressMap) {
        Address address = mapper.convertValue(addressMap, Address.class);
        return replaceAddress(address);
    }

    public void applyReplaceOperation(Operation operation) {
        String path = operation.getPath();
        Object value = operation.getValue();

        if (path.equalsIgnoreCase("name") && value instanceof Map) {
            Map<String, Object> nameMap = (Map<String, Object>) value;
            String givenName = (String) nameMap.get("givenName");
            String familyName = (String) nameMap.get("familyName");
            replaceName(new Name(givenName, familyName, null));
        } else if (path.startsWith("name.")) {
            handleNameAttribute(path, value);
        } else if (path.equals("emails") || path.equals("phoneNumbers")) {
            handleListAttributes(path, value);
        } else if (path.equals("addresses")) {
            handleAddressAttribute(value);
        } else {
            handleSimpleAttributes(path, value);
        }
    }


    private void handleNameAttribute(String path, Object value) {
        if (path.equals("name.givenName")) {
            replaceName(new Name(value.toString(), null, null));
        } else if (path.equals("name.familyName")) {
            replaceName(new Name(null, value.toString(), null));
        } else {
            throw new CIAMInvalidRequestException("Unsupported name attribute path: " + path);
        }
    }

    private void handleListAttributes(String path, Object value) {
        if (path.equals("emails")) {
            replaceEmails(mapper.convertValue(value, new TypeReference<List<Email>>() {}));
        } else if (path.equals("phoneNumbers")) {
            replacePhoneNumbers(mapper.convertValue(value, new TypeReference<List<PhoneNumber>>() {}));
        } else {
            throw new CIAMInvalidRequestException("Unsupported list attribute path: " + path);
        }
    }

    private void handleAddressAttribute(Object value) {
        if (value instanceof Map) {
            Address address = mapper.convertValue(value, Address.class);
            replaceAddress(address);
        } else {
            throw new CIAMInvalidRequestException("The value for 'addresses' is not of type Address");
        }
    }

    private void handleSimpleAttributes(String path, Object value) {
        switch (path) {
            case "userName":
                replaceUserName(value.toString());
                break;
            case "displayName":
                replaceDisplayName(value.toString());
                break;
            default:
                throw new CIAMInvalidRequestException("Unsupported attribute path: " + path);
        }
    }


    public JsonPatch build() {
        return Json.createPatch(operations.build());
    }
}
