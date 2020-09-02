package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.User;

import java.util.Optional;
import java.util.UUID;

/** Represents an user service.
 * @author Adrian
 * @author Dawid Wit
 * @version 1.1
 * @since 1.0
 */
public interface UserDao {

    User save(User var1);

    Iterable<User> saveAll(Iterable<User> var);

    Optional<User> findById(UUID id);

    Iterable<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findByPublicId(Long publicId);

    Optional<User> findByNameAndPassword(String name,String password);
}
