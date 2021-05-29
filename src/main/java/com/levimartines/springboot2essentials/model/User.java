package com.levimartines.springboot2essentials.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "\"USER\"")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    @Column(name = "USER_NAME", nullable = false)
    private String name;
    @Column(name = "USER_LOGIN", nullable = false)
    private String login;
    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;
    @Column(name = "USER_AUTHORITIES", nullable = false)
    private String authorities;

}
