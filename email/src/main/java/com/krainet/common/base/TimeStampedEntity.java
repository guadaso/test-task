package com.krainet.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@ToString(exclude = {"createdAt"}, callSuper = true)
public class TimeStampedEntity<ID extends Serializable> extends BaseEntity<ID> {

    @JsonIgnore
    private LocalDateTime createdAt;


    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}