package br.dev.optimus.gaia.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.dev.optimus.gaia.exception.DuplicateRecordException;
import br.dev.optimus.gaia.exception.PasswordMismatchException;
import br.dev.optimus.gaia.exception.RecordNotFoundException;
import br.dev.optimus.gaia.exception.RequiredExcepetion;
import br.dev.optimus.gaia.model.User;
import br.dev.optimus.gaia.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;
    private final GroupService groupService;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, GroupService groupService, PasswordEncoder encoder) {
        this.repository = repository;
        this.groupService = groupService;
        this.encoder = encoder;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public List<User> list() {
        return repository.list();
    }

    public User get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("user not found"));
    }

    public User create(User data) {
        if (data.getPassword().isBlank()) {
            throw new RequiredExcepetion("password is required");
        }
        if (data.getPassword().length() < 6) {
            throw new RequiredExcepetion("password must be at least 6 characters");
        }
        if (repository.existsByUsername(data.getUsername())) {
            throw new DuplicateRecordException("username already exists");
        }
        if (repository.existsByEmail(data.getEmail())) {
            throw new DuplicateRecordException("email already exists");
        }
        if (!data.getGroup().getId().equals(0)) {
            groupService.get(data.getGroup().getId());
        }
        return repository.save(data);
    }

    public User update(User data) {
        if (repository.existsByUsernameAndIdNot(data.getUsername(), data.getId())) {
            throw new DuplicateRecordException("username already exists");
        }
        if (repository.existsByEmailAndIdNot(data.getEmail(), data.getId())) {
            throw new DuplicateRecordException("email already exists");
        }
        if (data.getGroup() != null && !data.getGroup().getId().equals(0)) {
            groupService.get(data.getGroup().getId());
        }
        data.setUpdatedAt(Instant.now().toEpochMilli());
        return repository.save(data);
    }

    public User create(User.DTO dto) {
        if (!dto.password().equals(dto.passwordConfirm())) {
            throw new PasswordMismatchException("password mismatch");
        }
        if (dto.groupId() == null || dto.groupId() == 0) {
            throw new RequiredExcepetion("group is required");
        }
        var group = groupService.get(dto.groupId());
        return create(User.builder()
                .name(dto.name())
                .jobTitle(dto.jobTitle())
                .phone(dto.phone())
                .email(dto.email())
                .username(dto.username())
                .password(encoder.encode(dto.password()))
                .group(group)
                .build());
    }

    public User update(UUID id, User.DTO dto) {
        if (dto.password() != null && !dto.password().equals(dto.passwordConfirm())) {
            throw new PasswordMismatchException("password mismatch");
        }
        if (dto.password() != null && dto.password().length() < 6) {
            throw new RequiredExcepetion("password must be at least 6 characters");
        }
        if (dto.groupId() == null || dto.groupId() == 0) {
            throw new RequiredExcepetion("group is required");
        }
        var group = groupService.get(dto.groupId());
        var user = get(id);
        user.setName(dto.name());
        user.setJobTitle(dto.jobTitle());
        user.setPhone(dto.phone());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        if (dto.password() != null) {
            user.setPassword(encoder.encode(dto.password()));
        }
        user.setGroup(group);
        return update(user);
    }

    public void delete(UUID id) {
        var user = get(id);
        user.setDeletedAt(Instant.now().toEpochMilli());
        repository.save(user);
    }

    public User restore(UUID id) {
        var user = get(id);
        user.setDeletedAt(0L);
        return repository.save(user);
    }

}
