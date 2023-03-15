package com.project.trackfit.customer;

import com.project.trackfit.core.model.BaseUser;
import com.project.trackfit.subscription.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String address;


    @OneToMany(mappedBy ="customer")
    private Set<Subscription> subscriptions;

    public Customer(
            String firstName,
            String lastName,
            String address,
            int age,
            String email,
            byte[]salt,
            byte[]hashedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address=address;
        this.age=age;
        this.setEmail(email);
        this.setStoredSalt(salt);
        this.setStoredHash(hashedPassword);
    }


    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public boolean isCustomer(){return true;}
}
