package br.dev.optimus.gaia.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.dev.optimus.gaia.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    @Query("SELECT u FROM User u JOIN u.group g WHERE u.visible and u.deletedAt = 0")
    List<User> list();

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("SELECT u FROM User u JOIN u.group g WHERE u.visible and u.deletedAt = 0 and u.username = ?1 or u.email = ?1")
    Optional<User> findByUsername(String username);
}
