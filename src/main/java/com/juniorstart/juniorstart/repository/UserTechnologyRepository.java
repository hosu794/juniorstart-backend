package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Long> {

    //@Query("SELECT new UserProfile(u.user) FROM UserProfile u WHERE ?1 member of u.userTechnology")
    List<UserProfile> findAllByTechnologyName(String name);

}
