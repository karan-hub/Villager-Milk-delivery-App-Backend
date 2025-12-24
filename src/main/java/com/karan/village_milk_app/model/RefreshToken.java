package com.karan.village_milk_app.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token", indexes = {
        @Index(name = "token_index"  ,columnList = "jti" , unique = true),
        @Index(name = "user_index" , columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false , columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "jti" , nullable = false , updatable = false , unique = true)
    private  String jti ;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , updatable = false ,columnDefinition = "BINARY(16)")
    private User user ;

    @Column(updatable = false , nullable = false)
    private Instant createdAt ;
    @Column(updatable = false, nullable = false)
    private Instant expiresAt ;

    @Column(name = "replaced_by_token")
    private  boolean revoked;

    @Column(name = "revoked")
    private  String  replacedByToken;

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }


    public static class RefreshTokenBuilder {
        private UUID id;
        private String jti;
        private User user;
        private Instant createdAt;
        private Instant expiresAt;
        private boolean revoked;
        private String replacedByToken;

        public RefreshTokenBuilder jti(String jti) {
            this.jti = jti;
            return this;
        }

        public RefreshTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public RefreshTokenBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RefreshTokenBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public RefreshTokenBuilder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }

        public RefreshToken build() {
            RefreshToken token = new RefreshToken();
            token.id = this.id;
            token.jti = this.jti;
            token.user = this.user;
            token.createdAt = this.createdAt;
            token.expiresAt = this.expiresAt;
            token.revoked = this.revoked;
            token.replacedByToken = this.replacedByToken;
            return token;
        }
    }
}
