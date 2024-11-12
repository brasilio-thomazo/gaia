package br.dev.optimus.gaia.model;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "apps", uniqueConstraints = { @UniqueConstraint(columnNames = { "domain", "port" }) })
@Entity
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Type type;
    @ManyToOne
    private Customer customer;
    @JdbcTypeCode(SqlTypes.JSON)
    private Database database;
    private int replicas;
    private String domain;
    private int port;
    @Column(unique = true)
    private String path;
    @JdbcTypeCode(SqlTypes.JSON)
    private Container container;
    @Column(name = "started_at")
    @JsonProperty(value = "started_at")
    private long startedAt;
    @Column(name = "created_at")
    @JsonProperty(value = "created_at")
    private long createdAt;
    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at")
    private long updatedAt;
    @Column(name = "deleted_at")
    @JsonProperty(value = "deleted_at")
    private long deletedAt;

    public enum Type {
        CLIENT, MANAGER
    }

    public static class Database {
        private String host;
        private String port;
        private String user;
        private String password;
        private String name;

        public Database() {
        }

        public Database(String host, String port, String user, String password, String name) {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.name = name;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Container {
        private double cpu;
        private double memory;

        public Container() {
            this.cpu = 1.0;
            this.memory = 0.5;
        }

        public Container(double cpu, double memory) {
            this.cpu = cpu;
            this.memory = memory;
        }

        public double getCpu() {
            return cpu;
        }

        public void setCpu(double cpu) {
            this.cpu = cpu;
        }

        public double getMemory() {
            return memory;
        }

        public void setMemory(double memory) {
            this.memory = memory;
        }
    }

    public static class Builder {
        private UUID id;
        private Type type;
        private Customer customer;
        private Database database;
        private int replicas;
        private String domain;
        private int port;
        private String path;
        private Container container;
        private long startedAt;
        private long createdAt;
        private long updatedAt;
        private long deletedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder database(Database database) {
            this.database = database;
            return this;
        }

        public Builder replicas(int replicas) {
            this.replicas = replicas;
            return this;
        }

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder container(Container container) {
            this.container = container;
            return this;
        }

        public Builder startedAt(long startedAt) {
            this.startedAt = startedAt;
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

        public App build() {
            return new App(this);
        }
    }

    public App() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public App(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.customer = builder.customer;
        this.database = builder.database;
        this.replicas = builder.replicas;
        this.domain = builder.domain;
        this.port = builder.port;
        this.path = builder.path;
        this.container = builder.container;
        this.startedAt = builder.startedAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
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
