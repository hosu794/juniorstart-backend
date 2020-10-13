package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Long> {
}
