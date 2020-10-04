package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Page<UserProfile> findAll(Pageable pageable);
    Page<UserProfile> findByUserRoleIn(List<UserRole> userRole, Pageable pageable);
    Page<UserProfile> findByUserTechnology_technologyNameIn(List<String> technologyName, Pageable pageable);
    Page<UserProfile> findByUserTechnology_technologyNameInAndUserRoleIn(List<String>technologyName, List<UserRole> userRole, Pageable pageable);

}
