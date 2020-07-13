package com.malik.bank.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void shouldReturnUpdatedContact() {
        // given
        User user = new User();
        user.setName("David");

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setAddress(new Address());
        contact.setEmail("contact@bank.com");
        contact.setPhoneNumber("48111222333");

        Address address = new Address();
        address.setStreet("Street");
        address.setHouseNumber("100");
        address.setPostCode("66-100");
        address.setCity("City");
        address.setCountry("Country");

        Contact toUpdate = new Contact();
        toUpdate.setAddress(address);
        toUpdate.setEmail("newcontact@bank.com");
        toUpdate.setPhoneNumber("48333444555");

        // when
        Contact updatedContact = contact.updateContact(toUpdate);

        // then
        assertAll(
                () -> assertEquals(toUpdate.getEmail(), updatedContact.getEmail()),
                () -> assertEquals(toUpdate.getPhoneNumber(), updatedContact.getPhoneNumber()),
                () -> assertTrue(updatedContact.toString().contains("email='newcontact@bank.com'")),
                () -> assertNotNull(updatedContact.getAddress()),
                () -> assertEquals(address.getCity(), updatedContact.getAddress().getCity()),
                () -> assertEquals(address.getCountry(), updatedContact.getAddress().getCountry()),
                () -> assertTrue(updatedContact.getAddress().toString().contains("city='City'")),
                () -> assertEquals(contact.getUser().getName(), user.getName()));
    }
}