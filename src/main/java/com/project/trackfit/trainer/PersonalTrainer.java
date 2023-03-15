package com.project.trackfit.trainer;

import com.project.trackfit.core.model.BaseUser;
import com.project.trackfit.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PersonalTrainer extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isActivated;

    @OneToMany(mappedBy = "personalTrainer")


    private Set<Subscription> subscribers;
    private boolean isSuspended;
    private boolean isExpired;


    public PersonalTrainer(String email, String firstName, String lastName, String phoneNumber,byte[]salt,byte[]hashedPassword) {
        this.setEmail(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.setStoredSalt(salt);
        this.setStoredHash(hashedPassword);
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
}
