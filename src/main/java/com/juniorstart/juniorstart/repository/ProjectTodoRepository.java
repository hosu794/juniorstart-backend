package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ProjectTodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTodoRepository extends JpaRepository<ProjectTodo, Long> {

    @Query("select u from ProjectTodo u where lower(u.title) like lower(concat('%', :title,'%'))")
    public Page<ProjectTodo> findByTitle(@Param("title") String title, Pageable pageable);


    public Page<ProjectTodo> findByCreatedBy(long userId, Pageable pageable);

}
