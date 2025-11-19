package com.karan.village_milk_app.model;

import com.karan.village_milk_app.model.Type.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String name;
    private String password;

    private  String pincode;


    @Column(unique = true, nullable = false)
    private String phone;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Role role = Role.ROLE_USER;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
