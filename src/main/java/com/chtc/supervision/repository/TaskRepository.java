package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.Task;
import com.chtc.supervision.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, String> {

    List<Task> findByUserId(String id);

    Task save(Task entity);

    List<Task> findBySemesterId(String id);

    void delete(String s);

    Task saveAndFlush(Task task);

    Page<Task> findAll(Specification<Task> specification, Pageable pageable);
    @Query(value = "select task from Task task where task.user.id=:id and task.semester.id=:semesterId")
    Task findTaskByUserId(@Param("id") String id,@Param("semesterId") String semesterId);
}
