package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Technologies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TechnologiesRepository extends JpaRepository<Technologies, Long> {
	Optional<Technologies> findByTitle(String title);
}
