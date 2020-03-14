package com.chtc.supervision.repository;

import com.chtc.supervision.entity.CourseComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface CourseCommentRepository extends PagingAndSortingRepository<CourseComment, String> {

    public CourseComment findByCourseRecordIdAndCreateBy(String id,String createBy);

    public Page<CourseComment> findAll(Specification<CourseComment> specification, Pageable pageable);

    @Query(value = "select * from course_comment  where createBy = :createBy AND createDate between :startTime and :endTime ", nativeQuery = true)
    List<CourseComment> findByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("createBy") String createBy);

    /**
     * 更改课程评论的状态
     * @param id 课程评论id
     * @param state 要更改的状态
     */
    @Transactional
    @Modifying
    @Query(value = "update course_comment set state = :state where id = :id", nativeQuery = true)
    void updateCheckedState(@Param("id") String id, @Param("state") int state);

    /**
     * 根据id删除课程评论
     * @param id 课程评论id
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE from course_comment where id = :id", nativeQuery = true)
    void deleteCourseComment(@Param("id") String id);

    /**
     * 根据课程评论id查找课程评论
     * @param id 课程评论id
     * @return 查找到的课程评论
     */
    CourseComment findCourseCommentById(String id);

    public List<CourseComment> findCourseCommentByCourseRecordId(String  id);

    List<CourseComment> findByCourseRecordId(String recordId);

}
