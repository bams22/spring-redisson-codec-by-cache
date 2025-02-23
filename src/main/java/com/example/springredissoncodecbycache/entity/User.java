package com.example.springredissoncodecbycache.entity;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Table("users")
@Builder
@Data
public class User implements Persistable<UUID>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Nonnull
    private UUID id;

    @Nonnull
    private String name;

    @Nonnull
    private String email;

    @Nonnull
    private String password;

    @Nonnull
    private Boolean isActive;

    @Nonnull
    private UUID cityUuid;

    @Nonnull
    private Instant dateCreated;

    @Nonnull
    private Instant dateUpdated;

    @Builder.Default
    @Transient
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
