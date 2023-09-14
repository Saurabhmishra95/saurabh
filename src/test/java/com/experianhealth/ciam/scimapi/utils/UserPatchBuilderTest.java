package com.experianhealth.ciam.scimapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.experianhealth.ciam.scimapi.entity.Address;
import com.experianhealth.ciam.scimapi.entity.Email;
import com.experianhealth.ciam.scimapi.entity.Name;
import com.experianhealth.ciam.scimapi.entity.PhoneNumber;

import javax.json.JsonPatch;

public class UserPatchBuilderTest {

    private UserPatchBuilder patchBuilder;

    @BeforeEach
    public void setUp() {
        patchBuilder = new UserPatchBuilder();
    }

    @Test
    public void testReplaceUserName() {
        patchBuilder.replaceUserName("newUsername");
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(1, jsonPatch.toJsonArray().size());
    }

    @Test
    public void testReplaceDisplayName() {
        patchBuilder.replaceDisplayName("newDisplayName");
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(1, jsonPatch.toJsonArray().size());
    }

    @Test
    public void testReplaceName() {
        Name name = new Name();
        name.setGivenName("John");
        name.setFamilyName("Doe");

        patchBuilder.replaceName(name);
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(2, jsonPatch.toJsonArray().size());
    }

    @Test
    public void testReplaceEmails() {
        List<Email> emails = Arrays.asList(
            new Email("email1@example.com", "work", true),
            new Email("email2@example.com", "home", false)
        );

        patchBuilder.replaceEmails(emails);
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(1, jsonPatch.toJsonArray().size());
    }

    @Test
    public void testReplacePhoneNumbers() {
        List<PhoneNumber> phoneNumbers = Arrays.asList(
            new PhoneNumber("123-456-7890", "mobile"),
            new PhoneNumber("987-654-3210", "work")
        );

        patchBuilder.replacePhoneNumbers(phoneNumbers);
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(1, jsonPatch.toJsonArray().size());
    }

    @Test
    public void testReplaceAddress() {
        Address address = new Address();
        address.setLocality("City");
        address.setRegion("State");
        address.setPostalCode("12345");
        address.setCountry("Country");
        address.setPrimary(true);

        patchBuilder.replaceAddress(address);
        JsonPatch jsonPatch = patchBuilder.build();
        assertEquals(5, jsonPatch.toJsonArray().size());
    }
}
