package com.malik.bank.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void shouldReturnUpdatedContact() {
        // given
        Contact contact = new Contact();
        contact.setAddress(new Address());
        contact.setEmail("contact@bank.com");

        Address address = new Address();
        address.setStreet("Street");
        address.setHouseNumber("100");
        address.setPostCode("66-100");
        address.setCity("City");
        address.setCountry("Country");

        Contact toUpdate = new Contact();
        toUpdate.setAddress(address);
        toUpdate.setEmail("newcontact@bank.com");

        // when
        Contact updatedContact = contact.updateContact(toUpdate);

        // then
        assertAll(
                () -> assertEquals(toUpdate.getEmail(), updatedContact.getEmail()),
                () -> assertNotNull(updatedContact.getAddress()),
                () -> assertEquals(address.getCity(), updatedContact.getAddress().getCity()),
                () -> assertEquals(address.getCountry(), updatedContact.getAddress().getCountry()));
    }
}