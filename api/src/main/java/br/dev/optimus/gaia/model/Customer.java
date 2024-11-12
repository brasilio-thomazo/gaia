package br.dev.optimus.gaia.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String name;
    private String phone;
    private String email;
    private String document;
    private String address;
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<Contact> contacts;
    private boolean active;
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
            @NotBlank String phone,
            @Email String email,
            @NotBlank String document,
            String address,
            @NotNull List<Contact> contacts,
            boolean active) {
    }

    public static class Contact {
        private String name;
        private String phone;
        private String email;
        @JsonProperty(value = "job_title")
        private String jobTitle;

        public Contact() {
        }

        public Contact(String name, String phone, String email, String jobTitle) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.jobTitle = jobTitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name.toUpperCase();
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

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }
    }

    public static class Builder {
        private String name;
        private String phone;
        private String email;
        private String document;
        private String address;
        private Set<Contact> contacts;
        private boolean active;

        public Builder name(String name) {
            this.name = name.toUpperCase();
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

        public Builder document(String document) {
            this.document = document;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder contacts(Set<Contact> sponsor) {
            this.contacts = sponsor;
            return this;
        }

        public Builder contacts(Collection<Contact> sponsor) {
            this.contacts = sponsor.stream().collect(Collectors.toSet());
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public Customer() {
    }

    public Customer(Builder builder) {
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
        this.document = builder.document;
        this.address = builder.address;
        this.contacts = builder.contacts;
        this.active = builder.active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> sponsor) {
        this.contacts = sponsor;
    }

    public void setContacts(Collection<Contact> contacts) {
        this.contacts = Set.copyOf(contacts);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
