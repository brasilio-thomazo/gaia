package br.dev.optimus.gaia.model;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Group group;
    private String name;
    @Column(name = "job_title")
    @JsonProperty(value = "job_title")
    private String jobTitle;
    private String phone;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean visible;
    private boolean editable;
    private boolean locked;
    @Column(name = "created_at")
    @JsonProperty(value = "created_at")
    private long createdAt;
    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at")
    private long updatedAt;
    @Column(name = "deleted_at")
    @JsonProperty(value = "deleted_at")
    private long deletedAt;

    public record DTO(@NotBlank String name,
            @JsonProperty(value = "group_id") @NotNull Integer groupId,
            @JsonProperty(value = "job_title") String jobTitle,
            String phone,
            @NotBlank String email,
            @NotBlank String username,
            String password,
            @JsonProperty(value = "password_confirm") String passwordConfirm,
            boolean locked) {
    }

    public record Credentials(
            @NotBlank String username,
            @NotBlank String password) {
    }

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.group = builder.group;
        this.name = builder.name;
        this.jobTitle = builder.jobTitle;
        this.phone = builder.phone;
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.visible = builder.visible;
        this.editable = builder.editable;
        this.locked = builder.locked;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
    }

    public static class Builder {
        private UUID id;
        private Group group;
        private String name;
        private String jobTitle;
        private String phone;
        private String email;
        private String username;
        private String password;
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

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder group(Group group) {
            this.group = group;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email.toLowerCase();
            return this;
        }

        public Builder username(String username) {
            this.username = username.toLowerCase();
            return this;
        }

        public Builder password(String password) {
            this.password = password;
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

        public User build() {
            return new User(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return group.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
