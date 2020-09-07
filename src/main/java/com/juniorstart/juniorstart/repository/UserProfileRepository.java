package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ListUserRole;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.model.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findAllByUserRole(ListUserRole userRole);

    List<UserProfile> findAll();

    List<UserProfile> findAllByUserTechnologyIn(Set<String> technology);
      //List<UserProfile> findAllByUserTechnology_valueIn(List<String> technology);



    //@Query(value = "SELECT public.users_profile.* FROM public.users_profile WHERE public.users_profile.userTechnology = ?1", nativeQuery = true)
   // @Query(value = "SELECT o from UserProfile  o where ?1 member of o.userTechnology")


    List<UserProfile> findByUserTechnology_technologyName(String technologyName);
    List<UserProfile> findByUserTechnology_technologyNameInAndUserRole(List<String>technologyName, ListUserRole userRole);










//    @Query(value = "SELECT UserProfile , UserTechnology FROM UserProfile, UserTechnology WHERE UserTechnology.technology =?1 ", nativeQuery = true)
}
