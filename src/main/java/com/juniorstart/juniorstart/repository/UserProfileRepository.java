package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    List<UserProfile> findAll();
    List<UserProfile> findByUserRoleIn(List<UserRole> userRole);
    List<UserProfile> findByUserTechnology_technologyNameIn(List<String> technologyName);
    List<UserProfile> findByUserTechnology_technologyNameInAndUserRoleIn(List<String>technologyName, List<UserRole> userRole);

}
