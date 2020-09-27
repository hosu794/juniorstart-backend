package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Goal;
import com.juniorstart.juniorstart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface GoalDao extends JpaRepository<Goal, UUID> {

    Set<Goal> findAllByUser(User user);
}
