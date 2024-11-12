package br.dev.optimus.gaia.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.dev.optimus.gaia.exception.DuplicateRecordException;
import br.dev.optimus.gaia.exception.RecordNotFoundException;
import br.dev.optimus.gaia.model.Group;
import br.dev.optimus.gaia.repository.GroupRepository;

@Service
public class GroupService {
    private final GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public boolean exists(Integer id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }

    public Group findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException("group not found"));
    }

    public List<Group> list() {
        return repository.list();
    }

    public Group get(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("group not found"));
    }

    public Group create(Group data) {
        if (repository.existsByName(data.getName())) {
            throw new DuplicateRecordException("group name already exists");
        }
        return repository.save(data);
    }

    public Group update(Group data) {
        if (repository.existsByNameAndIdNot(data.getName(), data.getId())) {
            throw new DuplicateRecordException("group name already exists");
        }
        data.setUpdatedAt(Instant.now().toEpochMilli());
        return repository.save(data);
    }

    public Group create(Group.DTO dto) {
        var regex = "^(?!(root|admin|nobody|app:delete)$)[a-zA-Z]+:(list|create|update|delete)$";
        var permissions = dto.permissions().stream()
                .map(String::toLowerCase)
                .filter(p -> p.matches(regex))
                .toList();
        return create(Group.builder()
                .name(dto.name())
                .permissions(Set.copyOf(permissions))
                .locked(dto.locked())
                .build());
    }

    public Group update(Integer id, Group.DTO dto) {
        var data = get(id);
        var regex = "^(?!(root|admin|nobody|app:delete)$)[a-zA-Z]+:(list|create|update|delete)$";
        var permissions = dto.permissions().stream()
                .map(String::toLowerCase)
                .filter(p -> p.matches(regex))
                .toList();
        data.setName(dto.name());
        data.setPermissions(Set.copyOf(permissions));
        return update(data);
    }

    public void delete(Integer id) {
        var data = get(id);
        data.setDeletedAt(Instant.now().toEpochMilli());
        repository.save(data);
    }

    public Group restore(Integer id) {
        var data = get(id);
        data.setDeletedAt(0L);
        return update(data);
    }
}
