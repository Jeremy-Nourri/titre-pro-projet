package org.example.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "token_blacklist")
@Data
public class TokenBlacklist {
    @Id
    private String token;
    private Date expiryDate;
}
