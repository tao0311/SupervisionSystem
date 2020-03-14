package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Course;
import com.chtc.supervision.entity.CourseRecord;
import com.chtc.supervision.entity.Department;
import com.chtc.supervision.repository.CourseRecordRepository;
import com.chtc.supervision.repository.CourseRepository;
import com.chtc.supervision.repository.DepartmentRepository;
import com.chtc.supervision.repository.UserRepository;
import com.chtc.supervision.service.ICourseRecordService;
import com.chtc.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CourseRecordServiceImpl implements ICourseRecordService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRecordRepository courseRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Transactional
    public void createCourseRecord(String id) {
        CourseRecord record = courseRecordRepository.findByCourseIdAndRecordTime(id,DateUtil.getStringDate());
        //如果已经存在，则不用创建新的课程记录
        if (record != null && DateUtil.getStringDate().equals(record.getRecordTime())){
            return;
        }
        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setId(UUID.randomUUID().toString());
        courseRecord.setCourse(courseRepository.findOne(id));
        courseRecord.setCreateDate(new Date());
        courseRecord.setRecordTime(DateUtil.getStringDate());
        courseRecordRepository.save(courseRecord);
    }

    @Override
    public DataTableReturnObject getCourseRecordPageMode(DataRequest dr, String keyName, String code, String department) {
        //获取要排序的列的列名
        String fieldName = dr.getSidx();
        //排序
        Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
        //分页
        Pageable pageable = new PageRequest(dr.getPage() - 1, dr.getRows(), sort);
        List<JSONObject> list = new ArrayList<>();
        Page<CourseRecord> page = courseRecordRepository.findAll(new Specification<CourseRecord>() {
            Predicate p1;
            Predicate p2;
            Predicate p3;

            @Override
            public Predicate toPredicate(Root<CourseRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(keyName)) {
                    p1 = cb.like(root.get("course").get("user").get("nickName").as(String.class), "%" + keyName + "%");
                    predicates.add(p1);
                }
                if (!StringUtils.isEmpty(code)) {
                    p2 = cb.equal(root.get("course").get("courseName").as(String.class), code);
                    predicates.add(p2);
                }
                if (!StringUtils.isEmpty(department)) {
                    p3 = cb.equal(root.get("course").get("user").get("department").get("id").as(String.class),
                            department);
                    predicates.add(p3);
                }

                Predicate[] p = new Predicate[predicates.size()];
                query.where(cb.and(predicates.toArray(p)));
                return null;
            }
        }, pageable);
        long counts = page.getTotalElements();
        for (CourseRecord comment : page) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startYear", comment.getCourse().getSemester().getStartYear());
            jsonObject.put("endYear", comment.getCourse().getSemester().getEndYear());
            jsonObject.put("semesterNum", comment.getCourse().getSemester().getSemesterNum());
            jsonObject.put("teacherName", comment.getCourse().getUser().getNickName());
            jsonObject.put("courseName", comment.getCourse().getCourseName());
            jsonObject.put("departmentName", comment.getCourse().getUser().getDepartment()
                    .getDepartmentName());
            jsonObject.put("score", comment.getScore());
            jsonObject.put("totalCount", comment.getTotalCount());
            jsonObject.put("presentCount", comment.getPresentCount());

            list.add(jsonObject);
        }
        DataTableReturnObject dro = new DataTableReturnObject();
        dro.setAaData(list);
        dro.setiTotalRecords(counts);
        dro.setiTotalDisplayRecords(counts);
        return dro;
    }


    public List<CourseRecord> findAll(String department, String start, String end, String seNumber) {
        List<CourseRecord> all = courseRecordRepository.findAll(new Specification<CourseRecord>() {
            Predicate p1;
            Predicate p2;
            Predicate p3;
            Predicate p4;
            @Override
            public Predicate toPredicate(Root<CourseRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(department)) {
                    p1 = cb.equal(root.get("course").get("user").get("department").get("id"), department);
                    predicates.add(p1);
                }
                if (!StringUtils.isEmpty(start)) {
                    p2 = cb.equal(root.get("course").get("semester").get("startYear").as(String.class), start);
                    predicates.add(p2);
                }
                if (!StringUtils.isEmpty(end)) {
                    p3 = cb.equal(root.get("course").get("semester").get("endYear").as(String.class),
                            end);
                    predicates.add(p3);
                }
                if (!StringUtils.isEmpty(seNumber)) {
                    p4 = cb.equal(root.get("course").get("semester").get("semesterNum").as(Integer.class),
                            seNumber);
                    predicates.add(p4);
                }
                if (StringUtils.isEmpty(department) && start == null && end == null && StringUtils.isEmpty(seNumber)) {
                    p1 = cb.equal(root.get("course").get("user").get("department").get("id"), "1");
                    predicates.add(p1);
                }

                Predicate[] p = new Predicate[predicates.size()];
                query.where(cb.and(predicates.toArray(p)));
                return null;
            }
        });

        return all;
    }

    public List<CourseRecord> findAll() {
        List<CourseRecord> all = courseRecordRepository.findAll();
        return all;
    }

    @Override
    public String findDepartmentNameById(String id) {
        Department byID = departmentRepository.findOne(id);
        return byID.getDepartmentName();
    }

    @Override
    public List<CourseRecord> findByTeacherName(String teacherName) {
        List<CourseRecord> all = courseRecordRepository.findAll((root, query, cb) -> {
            Predicate like = cb.equal(root.get("course").get("user").get("nickName").as(String.class),
                    teacherName );
            query.where(cb.and(like));
            return null;
        });
        return all;
    }

    @Override
    public DataTableReturnObject findAllCourseRecordsById(DataRequest dr, String searchCode,String searchName,String searchMajor) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            //获取要排序的列的列名
            String fieldName = dr.getSidx();
            //排序
            if (fieldName.equals("courseCode") || fieldName.equals("courseName") || fieldName.equals("grade") || fieldName.equals("major") || fieldName.equals("classes") || fieldName.equals("score")) {
                fieldName = "recordCount";
            }
            Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
            //分页
            Pageable pageable = new PageRequest(dr.getPage() - 1, dr.getRows(), sort);
            List<JSONObject> list = new ArrayList<>();
            //首先根据用户id查询到相关的课程
            String id = SecurityUtil.getUser(userRepository).getId();
            List<Course> courseList = courseRepository.findCoursesByUserId(id);

            Predicate predicate = null;
            Page<CourseRecord> page = null;
            int count = 0;
            //根据课程的id去查询相关的记录 先要判断couesrList是否大于0
            if (courseList.size() > 0) {
                for (Course courses : courseList) {
                    //根据条件查询
                    page = courseRecordRepository.findAll(new Specification<CourseRecord>() {
                        @Override
                        public Predicate toPredicate(Root<CourseRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            List<Predicate> predicateList = new ArrayList<Predicate>();

                            if (StringUtil.isNotNull(searchCode) && !("".equals(searchCode))) {
                                predicateList.add(cb.like(root.get("course").get("courseCode").as(String.class), "%" + searchCode + "%"));
                            }
                            if (StringUtil.isNotNull(searchName) && !("".equals(searchName))) {
                                predicateList.add(cb.like(root.get("course").get("courseName").as(String.class), "%" + searchName + "%"));
                            }
                            if (StringUtil.isNotNull(searchMajor) && !("".equals(searchMajor))) {
                                predicateList.add(cb.like(root.get("course").get("major").as(String.class), "%" + searchMajor + "%"));
                            }

                            predicateList.add(cb.equal(root.get("course").get("id"), courses.getId()));
                            //TODO 查询已审核完的听课汇总记录
                            predicateList.add(cb.equal(root.get("state").as(int.class),1));


                            Predicate[] arrayPredicates = new Predicate[predicateList.size()];
                            query.where(cb.and(predicateList.toArray(arrayPredicates)));
                            return null;
                        }
                    }, pageable);

                    //根据课程获得课程记录
                    List<CourseRecord> contentList = page.getContent();
                    long counts = page.getTotalElements();

                    for (CourseRecord courseRecords : contentList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("recordCount", courseRecords.getRecordCount());//听课人数
                        jsonObject.put("id", courseRecords.getId());
                        jsonObject.put("courseDate",courseRecords.getCourse().getCourseDate());
                        jsonObject.put("recordTime",courseRecords.getRecordTime());
                        jsonObject.put("courseCode", courseRecords.getCourse().getCourseCode());//课程编号
                        jsonObject.put("courseName", courseRecords.getCourse().getCourseName());//课程名称
                        jsonObject.put("score", courseRecords.getScore());//得分
                        //专业，班级
                        Course course = courseRecords.getCourse();
                        StringBuilder classes = new StringBuilder();
                        switch (course.getClasses()){
                            case 1:
                                classes.append(course.getGrade()).append(course.getMajor()).append("1班");
                                break;
                            case 2:
                                classes.append(course.getGrade()).append(course.getMajor()).append("2班");
                                break;
                            case 3:
                                classes.append(course.getGrade()).append(course.getMajor()).append("1班").append(",")
                                        .append(course.getGrade()).append(course.getMajor()).append("2班");
                                break;
                        }
                        jsonObject.put("classes", classes.toString());


                        list.add(jsonObject);
                    }

//
                    count++;
                    dro.setAaData(list);
                }
                    dro.setiTotalRecords(count);
                     dro.setiTotalDisplayRecords(count);
                    return dro;
                }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
