package com.malik.bank.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 30)
    private String name;

    @Size(min = 26, max = 26)
    private String number;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotNull
    @Size(min = 7, max = 8)
    private String type;

    private BigDecimal balance = BigDecimal.ZERO;

    private boolean active;

    @Embedded
    private Audit audit = new Audit();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addMoney(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void subtractMoney(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", number='" + number + "'" +
                ", owner=" + owner +
                ", type='" + type + "'" +
                ", balance=" + balance +
                ", active=" + active +
                "}";
    }
}