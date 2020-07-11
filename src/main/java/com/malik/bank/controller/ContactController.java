package com.malik.bank.controller;

import com.malik.bank.model.Contact;
import com.malik.bank.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@IllegalExceptionControllerProcessing
@RequestMapping("/api/contact")
class ContactController {

    private ContactRepository contactRepository;

    ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PutMapping("/{id}/update")
    ResponseEntity<?> updateContact(@PathVariable long id, @RequestBody @Valid Contact toUpdate, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalStateException(UserController.getErrorMessage(result));
        }

        return contactRepository.findById(id)
                .map(contact -> {
                    contactRepository.save(contact.updateContact(toUpdate));
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new IllegalArgumentException("Could not find contact - id: " + id));
    }
}