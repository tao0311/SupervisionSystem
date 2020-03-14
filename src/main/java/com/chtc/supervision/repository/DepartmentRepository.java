package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends PagingAndSortingRepository<Department, String> {

    public Department findByDepartmentName(String departmentName);

    public List<Department> findAll();

    @Query(value="SELECT * FROM department where departmentName = :departmentName", nativeQuery = true)
    Department findOneByDepartmentName(@Param("departmentName") String departmentName);
}
