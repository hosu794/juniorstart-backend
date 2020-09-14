package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileTechnologyRepository extends JpaRepository<UserTechnology, Long> {
}
