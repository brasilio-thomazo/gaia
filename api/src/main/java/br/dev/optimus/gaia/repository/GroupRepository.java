package br.dev.optimus.gaia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.dev.optimus.gaia.model.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE g.visible and g.deletedAt = 0")
    List<Group> list();

    Optional<Group> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
