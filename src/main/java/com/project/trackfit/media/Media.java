package com.project.trackfit.media;

import com.project.trackfit.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
