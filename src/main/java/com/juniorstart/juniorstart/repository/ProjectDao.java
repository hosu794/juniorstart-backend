package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

}
