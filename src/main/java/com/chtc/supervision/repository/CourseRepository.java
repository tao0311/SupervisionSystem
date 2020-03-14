package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, String> {
    Page<Course> findAll(Specification<Course> specification, Pageable pageable);

    @Query(value = "select * from course where courseCode = :myCode", nativeQuery = true)
    List<Course> findCourseByCode(@Param("myCode") String code);

    @Transactional
    @Modifying
    @Query(value = "UPDATE course SET isDelete=1 WHERE id= :id", nativeQuery = true)
    void deleteCourseComment(@Param("id") String id);

    List<Course> findCoursesByUserId(String id);
}
