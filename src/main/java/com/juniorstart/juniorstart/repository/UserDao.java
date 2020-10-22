package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.User;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByPrivateId(UUID id);

    Optional<User> findByPublicId(Long publicId);

    Optional<User> findByEmail(String email);

    Optional<User> findByPrivateIdAndPassword(UUID privateId, String password);

    Optional<User> findByEmailAndPassword(String email, String password);
    
}
