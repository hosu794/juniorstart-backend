package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalDao extends JpaRepository<Goal, UUID> {

    List<Goal> findAllByUser(User user);

    Optional<Goal> findByNameAndUser(String name, User user);
}
