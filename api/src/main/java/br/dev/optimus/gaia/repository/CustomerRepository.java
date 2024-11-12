package br.dev.optimus.gaia.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.dev.optimus.gaia.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, UUID> {
    @Query("SELECT c FROM Customer c WHERE c.deletedAt = 0")
    List<Customer> list();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);
}
