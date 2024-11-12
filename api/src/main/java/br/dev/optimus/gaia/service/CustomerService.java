package br.dev.optimus.gaia.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.dev.optimus.gaia.exception.DuplicateRecordException;
import br.dev.optimus.gaia.exception.RecordNotFoundException;
import br.dev.optimus.gaia.model.Customer;
import br.dev.optimus.gaia.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> list() {
        return repository.list();
    }

    public long count() {
        return repository.count();
    }

    public Customer get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("customer not found"));
    }

    public Customer create(Customer data) {
        if (repository.existsByName(data.getName())) {
            throw new DuplicateRecordException("customer name already exists");
        }
        var now = Instant.now().toEpochMilli();
        data.setCreatedAt(now);
        data.setUpdatedAt(now);
        return repository.save(data);
    }

    public Customer update(Customer data) {
        if (repository.existsByNameAndIdNot(data.getName(), data.getId())) {
            throw new DuplicateRecordException("customer name already exists");
        }
        data.setUpdatedAt(Instant.now().toEpochMilli());
        return repository.save(data);
    }

    public Customer create(Customer.DTO dto) {
        return create(Customer.builder()
                .name(dto.name())
                .phone(dto.phone())
                .email(dto.email())
                .document(dto.document())
                .address(dto.address())
                .contacts(dto.contacts())
                .active(dto.active())
                .build());
    }

    public Customer update(UUID id, Customer.DTO dto) {
        var data = get(id);
        data.setName(dto.name());
        data.setPhone(dto.phone());
        data.setEmail(dto.email());
        data.setDocument(dto.document());
        data.setAddress(dto.address());
        data.setContacts(dto.contacts());
        data.setActive(dto.active());
        return update(data);
    }

    public void delete(UUID id) {
        var data = get(id);
        data.setDeletedAt(Instant.now().toEpochMilli());
        repository.save(data);
    }

    public Customer restore(UUID id) {
        var data = get(id);
        data.setDeletedAt(0L);
        return update(data);
    }
}
