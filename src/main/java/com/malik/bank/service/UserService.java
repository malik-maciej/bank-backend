package com.malik.bank.service;

import com.malik.bank.UserRole;
import com.malik.bank.model.Address;
import com.malik.bank.model.Contact;
import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addAdminToDb() {
        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        User user = getUser();
        userRepository.save(user);

        LOGGER.info("Created user - " + user.getUsername());
    }

    private User getUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode(("password")));
        user.setName("Name");
        user.setSurname("Surname");
        user.setIdNumber("ABC123456");
        user.setRole(UserRole.ADMIN.toString());
        user.setContact(getContact(user));
        return user;
    }

    private Contact getContact(User user) {
        Contact contact = new Contact();
        contact.setEmail("admin@bank.com");
        contact.setPhoneNumber("48666333999");
        contact.setAddress(getAddress());
        contact.setUser(user);
        return contact;
    }

    private Address getAddress() {
        Address address = new Address();
        address.setStreet("Piłsudskiego");
        address.setHouseNumber("105");
        address.setPostCode("50-085");
        address.setCity("Wrocław");
        address.setCountry("Poland");
        return address;
    }
}