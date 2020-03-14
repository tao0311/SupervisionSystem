package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Semester;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends PagingAndSortingRepository<Semester, String> {

    /**
     * 根据开始年份，结束年份，第几学期去数据库中查询某个学期
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @param se_num 学期
     * @return 查询到的学期
     */
    @Query(value = "select * from semester where startYear = :startYear and endYear = :endYear and semesterNum = :se_num", nativeQuery = true)
    Semester findOneSemester(@Param("startYear") String startYear, @Param("endYear") String endYear, @Param("se_num") int se_num);

    /**
     * 查询所有的学期并按开始年份排序
     */
    @Query(value="select * from semester ORDER BY startYear", nativeQuery = true)
    List<Semester> findAll();

    @Query("from Semester where startYear=:startYear and endYear=:endYear and semesterNum=:semesterNum")
    Semester findSemster(@Param("startYear") String startYear, @Param("endYear") String endYear,@Param("semesterNum") int
            semesterNum);

    Semester save(Semester entity);
}
