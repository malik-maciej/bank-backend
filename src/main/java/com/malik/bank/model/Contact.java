package com.malik.bank.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Valid
    @NotNull
    @Embedded
    private Address address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact updateContact(Contact contact) {
        email = contact.email;
        address.setCountry(contact.getAddress().getCountry());
        address.setCity(contact.getAddress().getCity());
        address.setPostCode(contact.getAddress().getPostCode());
        address.setHouseNumber(contact.getAddress().getHouseNumber());
        address.setStreet(contact.getAddress().getStreet());
        return this;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", email='" + email + "'" +
                ", address=" + address +
                ", user=" + user +
                "}";
    }
}