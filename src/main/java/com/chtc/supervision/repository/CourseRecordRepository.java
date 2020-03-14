package com.chtc.supervision.repository;

import com.chtc.supervision.entity.CourseRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRecordRepository extends PagingAndSortingRepository<CourseRecord, String> {
    CourseRecord findByCourseIdAndRecordTime(String id,String recordTime);

    Page<CourseRecord> findAll(Specification<CourseRecord> specification, Pageable pageable);

    List<CourseRecord> findAll(Specification<CourseRecord> specification);

    List<CourseRecord> findAll();
}
