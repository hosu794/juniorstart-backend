package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.juniorstart.juniorstart.model.audit.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByPrivateId(UUID id);

    Optional<User> findByPublicId(Long publicId);

    Page<User> findByName(String username, Pageable pageable);

    Optional<User> findByEmail(String email);

    boolean existsByName(String name);

    Optional<User> findByNameAndPassword(String name, String password);

    List<User> findByPublicIdIn(List<Long> userIds);


    Optional<User> findByPrivateIdAndPassword(UUID privateId, String password);

}
