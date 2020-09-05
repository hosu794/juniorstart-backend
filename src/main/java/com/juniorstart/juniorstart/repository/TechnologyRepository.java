package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {


    Optional<Technology> findByTitle(String title);

}
