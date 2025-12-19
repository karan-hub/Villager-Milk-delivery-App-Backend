package com.karan.village_milk_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karan.village_milk_app.model.Type.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id" , columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    @JsonIgnore
    private String password;

    @Column(unique = true, nullable = false)
    private String phone;


    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt =Instant.now();
    private Instant updatedAt = Instant.now();

    private  boolean  enable = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> orders=new ArrayList<>();;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subscriptions> subscriptions =new ArrayList<>(); ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payments> payments =new ArrayList<>();


    @PrePersist
    protected  void  onCreate(){
        Instant now  = Instant.now();
        if(createdAt == null ) this.createdAt =now ;
        this.updatedAt = now;
    }

    @PreUpdate
    protected  void  onUpdate(){
        this.updatedAt  = Instant.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return  this.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
