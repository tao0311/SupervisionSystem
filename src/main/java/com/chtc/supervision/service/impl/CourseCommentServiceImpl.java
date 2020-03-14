package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Course;
import com.chtc.supervision.entity.CourseComment;
import com.chtc.supervision.entity.CourseRecord;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.repository.CourseCommentRepository;
import com.chtc.supervision.repository.CourseRecordRepository;
import com.chtc.supervision.repository.CourseRepository;
import com.chtc.supervision.repository.UserRepository;
import com.chtc.supervision.service.ICourseCommentService;
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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class CourseCommentServiceImpl implements ICourseCommentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRecordRepository courseRecordRepository;
    @Autowired
    private CourseCommentRepository courseCommentRepository;


    @Transactional
    public Json createCourseComment(String id,HttpServletRequest request) {
        Json json = new Json();
        User user = null;
        user = (User) request.getSession().getAttribute("user");
        if (StringUtils.isEmpty(user)){
            user = SecurityUtil.getUser(userRepository);
        }
        CourseRecord record= courseRecordRepository.findByCourseIdAndRecordTime(id,DateUtil.getStringDate());
        if (record != null){
            CourseComment comment = courseCommentRepository.findByCourseRecordIdAndCreateBy(record.getId(),user.getId());
            if (comment != null && DateUtil.getStringDate().equals(comment.getRecordTime())){
                json.setSuccess(false);
                json.setMsg("您已经点击过听课了");
                json.setObj(comment.getId());
                return json;
            }
        }
        CourseComment courseComment = new CourseComment();
        courseComment.setId(UUID.randomUUID().toString());
        courseComment.setCreateDate(new Date());
        courseComment.setCreateBy(user.getId());
        courseComment.setUpdateBy(user.getId());
        courseComment.setUpdateDate(new Date());
        courseComment.setRecordTime(DateUtil.getStringDate());
        CourseRecord courseRecord = courseRecordRepository.findByCourseIdAndRecordTime(id,DateUtil.getStringDate());
        int count = courseRecord.getRecordCount();
        courseRecord.setRecordCount(++count);
        courseComment.setCourseRecord(courseRecord);
        courseCommentRepository.save(courseComment);
        json.setCode(user.getId());
        json.setSuccess(true);
        return json;
    }

    public DataTableReturnObject getCourseCommentPage(DataRequest dr, String searchCourseInfo,int state) {
        //获取要排序的列的列名
        String fieldName = dr.getSidx();
        //排序
        Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord())? Sort.Direction.ASC:Sort.Direction.DESC,fieldName);
        //分页
        Pageable pageable = new PageRequest(dr.getPage()-1,dr.getRows(),sort);
        User user = SecurityUtil.getUser(userRepository);
        String code =SecurityUtil.getCode(user);
        List<JSONObject> list = new ArrayList<>();
        Page<CourseComment> page = courseCommentRepository.findAll(new Specification<CourseComment>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //状态
                if (!StringUtils.isEmpty(state)){
                    Predicate p1 = cb.equal(root.get("state").as(Integer.class),state);
                    predicateList.add(p1);
                }
                //课程信息
                if (!StringUtils.isEmpty(searchCourseInfo)){
                    Predicate p2 = cb.like(root.get("courseRecord").get("course").get("courseName"),"%"+searchCourseInfo+"%");
                    Predicate p3 = cb.like(root.get("courseRecord").get("course").get("user").get("nickName"),"%"+searchCourseInfo+"%");
                    Predicate p4 = cb.and(cb.or(p2,p3));
                    predicateList.add(p4);
                }
                //创建者
                if (code.equals("ROLE_SUPERVISOR")) {
                    Predicate p5 = cb.equal(root.get("createBy").as(String.class), user.getId());
                    predicateList.add(p5);
                }
                Predicate [] p = new Predicate[predicateList.size()];
                query.where(cb.and(predicateList.toArray(p)));
                return null;
            }
        },pageable);
        List<CourseComment> contentList = page.getContent();
        long counts = page.getTotalElements();
        for (CourseComment courseComment:contentList){
                JSONObject jsonObject = new JSONObject();
            if (code.equals("ROLE_SUPERVISOR")) {
                jsonObject.put("id", courseComment.getId());
                jsonObject.put("courseName", courseComment.getCourseRecord().getCourse().getCourseName());
                jsonObject.put("courseDate", courseComment.getCourseRecord().getCourse().getCourseDate());
                jsonObject.put("courseTeacher", courseComment.getCourseRecord().getCourse().getUser().getNickName());
                if (!StringUtils.isEmpty(courseComment.getCreateDate())) {
                    jsonObject.put("createDate", courseComment.getCreateDate());
                } else {
                    jsonObject.put("createDate", "");
                }
                if (!StringUtils.isEmpty(courseComment.getTotalCount())) {
                    jsonObject.put("totalCount", courseComment.getTotalCount());
                } else {
                    jsonObject.put("totalCount", "未填写");
                }
                if (!StringUtils.isEmpty(courseComment.getPresentCount())) {
                    jsonObject.put("presentCount", courseComment.getPresentCount());
                } else {
                    jsonObject.put("presentCount", "未填写");
                }
                if (!StringUtils.isEmpty(courseComment.getScore())) {
                    jsonObject.put("score", courseComment.getScore());
                } else {
                    jsonObject.put("score", "未填写");
                }
                if (state == 0){
                    if (courseComment.getIsEdit() == 0){
                        jsonObject.put("isEdit","<span style=\"color: red\">未填写</span>");
                    }else {
                        jsonObject.put("isEdit", "<span style=\"color: limegreen\">已填写</span>");
                    }
                }

                list.add(jsonObject);
            }
            if (code.equals("ROLE_ADMIN")){
                String nickName = userRepository.findOne(courseComment.getCreateBy()).getNickName();
                jsonObject.put("id", courseComment.getId());
                jsonObject.put("updateDate",courseComment.getUpdateDate());
                jsonObject.put("commentTeacherName", nickName);
                jsonObject.put("state",courseComment.getState());
                if (!StringUtils.isEmpty(courseComment.getTotalCount())) {
                    jsonObject.put("totalCount", courseComment.getTotalCount());
                } else {
                    jsonObject.put("totalCount", "");
                }
                if (!StringUtils.isEmpty(courseComment.getPresentCount())) {
                    jsonObject.put("presentCount", courseComment.getPresentCount());
                } else {
                    jsonObject.put("presentCount", "");
                }
                if (!StringUtils.isEmpty(courseComment.getScore())) {
                    jsonObject.put("score", courseComment.getScore());
                } else {
                    jsonObject.put("score", "");
                }
                jsonObject.put("comment", courseComment.getComment());
                jsonObject.put("proposal", courseComment.getProposal());
                jsonObject.put("courseName", courseComment.getCourseRecord().getCourse().getCourseName());
                jsonObject.put("teacherName", courseComment.getCourseRecord().getCourse().getUser().getNickName());
                list.add(jsonObject);
            }
        }
        DataTableReturnObject dro = new DataTableReturnObject();
        dro.setiTotalRecords(counts);
        dro.setiTotalDisplayRecords(counts);
        dro.setAaData(list);
        return dro;
    }

    @Override
    public List<CourseComment> findByTime(Date startTime, Date endTime,String createBy) {
        List<CourseComment> list = courseCommentRepository.findByTime(startTime,endTime,createBy);
        return list;
    }

    @Override
    public CourseComment getCourseComment(String id) {
        return courseCommentRepository.findOne(id);
    }

    @Transactional
    public void saveCourseComment(CourseComment courseComment) {
        CourseComment comment = courseCommentRepository.findOne(courseComment.getId());
        if (comment != null){
            comment.setTotalCount(courseComment.getTotalCount());
            comment.setPresentCount(courseComment.getPresentCount());
            comment.setScore(courseComment.getScore());
            comment.setComment(courseComment.getComment());
            comment.setProposal(courseComment.getProposal());
            comment.setIsEdit(1);
        }
        courseCommentRepository.save(comment);
    }

    /**
     * 0.未完成/未审核
     * 1.正在审核
     * 2.未通过
     * 3.已审核/已通过
     * @param id
     */
    public void submitComment(String id) {
        CourseComment comment = courseCommentRepository.findOne(id);
        if (comment !=null){
            comment.setState(1);
            comment.setUpdateDate(new Date());
        }
        courseCommentRepository.save(comment);
    }

    /**
     * 使某条记录通过或不通过
     * @param arr 多条记录数组
     * @param state 要更改后的状态
     * @return 成功与否信息
     */
    @Override
    public Json updateCheckedState(String[] arr, int state) {
        Json json = new Json();
        try {
            for (String id : arr) {
                courseCommentRepository.updateCheckedState(id, state);
                if (state == 2){
                    CourseComment comment = courseCommentRepository.findOne(id);
                    int count = comment.getCourseRecord().getRecordCount();
                    int checkedCount = comment.getCourseRecord().getCheckedCount();
                    CourseRecord courseRecord = comment.getCourseRecord();
                    if (checkedCount == 0){
                        courseRecord.setCheckedCount(count);
                    }else {
                        courseRecord.setCheckedCount(--checkedCount);
                    }
                   courseRecordRepository.save(courseRecord);
                    if (--count == 0 || --checkedCount == 0){
                        String recordId = comment.getCourseRecord().getId();
                        List<CourseComment> courseComments = courseCommentRepository.findByCourseRecordId(recordId);
                        float scoreTotal = 0,scoreAverage=0;
                        for (CourseComment courseComment:courseComments){

                            scoreTotal += courseComment.getScore();
                        }
                        scoreAverage = scoreTotal/courseComments.size();
                        courseRecord.setScore(scoreAverage);
                        courseRecord.setTotalCount(courseComments.get(0).getTotalCount());
                        courseRecord.setPresentCount(courseComments.get(0).getPresentCount());
                        courseRecord.setState(1);
                    }
                    courseRecordRepository.save(courseRecord);
                }
            }
        } catch (Exception e){
            json.setMsg("操作失败，请重试");
            return json;
        }
        //TODO 审核完之后要汇总
        json.setMsg("操作成功");
        return json;
    }

    /**
     * 删除课程评论
     * @param ids 记录列表
     * @return 成功与否信息
     */
    @Override
    public String deleteCourseComment(String[] ids) {
        String msg;
        try {
            for (String id : ids) {
                courseCommentRepository.deleteCourseComment(id);
            }
        } catch (Exception e){
            msg = "操作失败，请重试";
            return msg;
        }
        msg = "操作成功";
        return msg;
    }

    /**
     * 根据id查找课程评论，并将需要传回的数据封装进map
     * @param id 课程评论id
     * @return 封装数据后的map
     */
    @Override
    public Map<String, String> findCourseCommentByID(String id) {
        CourseComment courseComment = courseCommentRepository.findCourseCommentById(String.valueOf(id));
        String user_id = courseComment.getCreateBy();
        String commentTeacherName = userRepository.findOne(user_id).getNickName();

        Map map = new HashMap<String, String>();
        map.put("id", courseComment.getId());
        map.put("commentTeacherName", commentTeacherName);
        map.put("totalCount", courseComment.getTotalCount());
        map.put("presentCount", courseComment.getPresentCount());
        map.put("state", courseComment.getState());
        map.put("score", courseComment.getScore());
        map.put("comment", courseComment.getComment());
        map.put("proposal", courseComment.getProposal());
        map.put("courseName", courseComment.getCourseRecord().getCourse().getCourseName());
        map.put("teacherName", courseComment.getCourseRecord().getCourse().getUser().getNickName());

        return map;
    }

    @Override
    public DataTableReturnObject findAllCourseCommentsById(DataRequest dr, String id) {
        DataTableReturnObject dro = new DataTableReturnObject();
        try {
            //获取要排序的列的列名
            String fieldName = dr.getSidx();
            //排序 在判断的时候将列名转换成查的表中的信息 不然会有问题
            if (fieldName.equals("courseCode")||fieldName.equals("courseName")||fieldName.equals("grade")||fieldName.equals("major")||fieldName.equals("classes")){
                fieldName="score";
            }
            Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
            //分页
            Pageable pageable = new PageRequest(dr.getPage() - 1, dr.getRows(), sort);
            List<JSONObject> list = new ArrayList<>();
            Page<CourseComment> page = null;
            List<CourseComment> commentList = courseCommentRepository.findCourseCommentByCourseRecordId(id);
            int count = 0;
            for (CourseComment courseComments : commentList) {
                //state的值为2代表为审核通过的
                if (courseComments.getState() == 2) {
                    page = courseCommentRepository.findAll(new Specification<CourseComment>() {
                        @Override
                        public Predicate toPredicate(Root<CourseComment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate predicate = cb.and(cb.equal(root.get("state"),courseComments.getState()),
                                    cb.equal(root.get("id"),courseComments.getId()));
                            return predicate;
                        }
                    }, pageable);
                    List<CourseComment> contentList = page.getContent();
                    long counts = page.getTotalElements();

                    for (CourseComment comments : contentList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", comments.getId());
                        jsonObject.put("courseName", comments.getCourseRecord().getCourse().getCourseName());
                        jsonObject.put("classes", comments.getCourseRecord().getCourse().getClasses());
                        jsonObject.put("major", comments.getCourseRecord().getCourse().getMajor());
                        jsonObject.put("grade", comments.getCourseRecord().getCourse().getGrade());
                        jsonObject.put("classes", comments.getCourseRecord().getCourse().getClasses());
                        jsonObject.put("courseDate", comments.getCourseRecord().getCourse().getCourseDate());
                        jsonObject.put("totalCount", comments.getTotalCount());
                        jsonObject.put("presentCount", comments.getPresentCount());
                        jsonObject.put("createBy", comments.getCreateBy());
                        jsonObject.put("score", comments.getScore());
                        list.add(jsonObject);
                    }
                    count++;
                    dro.setAaData(list);
                }
            }
                    dro.setiTotalRecords(count);
                    dro.setiTotalDisplayRecords(count);
                    return dro;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
