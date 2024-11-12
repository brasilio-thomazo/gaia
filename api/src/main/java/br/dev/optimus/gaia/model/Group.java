package br.dev.optimus.gaia.model;

import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> permissions;
    private boolean visible;
    private boolean editable;
    private boolean locked;
    @Column(name = "created_at")
    @JsonProperty("created_at")
    private long createdAt;
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private long updatedAt;
    @Column(name = "deleted_at")
    @JsonProperty("deleted_at")
    private long deletedAt;

    public record DTO(
            @NotBlank String name,
            Set<String> permissions,
            boolean locked) {
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Set<String> permissions;
        private boolean visible;
        private boolean editable;
        private boolean locked;
        private long createdAt;
        private long updatedAt;
        private long deletedAt;

        public Builder() {
            var now = Instant.now().toEpochMilli();
            this.visible = true;
            this.editable = true;
            this.locked = false;
            this.createdAt = now;
            this.updatedAt = now;
            this.deletedAt = 0L;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name.toLowerCase();
            return this;
        }

        public Builder permissions(Set<String> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder editable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public Builder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder deletedAt(long deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }

    public Group() {
    }

    public Group(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.permissions = builder.permissions;
        this.visible = builder.visible;
        this.editable = builder.editable;
        this.locked = builder.locked;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }
}
