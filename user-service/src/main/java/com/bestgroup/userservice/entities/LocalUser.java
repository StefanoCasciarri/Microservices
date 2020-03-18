package com.bestgroup.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "login")
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocalUser {

    @Id
    @NotNull
    @Size(min=2, max=30)
    private String login;
    @NotNull
    private String password;
}
