package com.krainet.api.users.entity;

import com.krainet.common.enums.UserRole;
import com.krainet.common.model.entity.base.TimeStampedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Users extends TimeStampedEntity<Long> {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}
