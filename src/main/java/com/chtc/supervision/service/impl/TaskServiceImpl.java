package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.Semester;
import com.chtc.supervision.entity.Task;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.repository.DepartmentRepository;
import com.chtc.supervision.repository.SemesterRepository;
import com.chtc.supervision.repository.TaskRepository;
import com.chtc.supervision.service.ITaskService;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SemesterRepository semesterRepository;

    @Override
    public DataTableReturnObject getTaskByUserId(String userId, DataRequest dr) {
//        //获取要排序的列的列名
//        String fieldName = dr.getSidx();
        ////排序
        Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC, "id");
        //分页
        Pageable pageable = new PageRequest(dr.getPage() - 1, dr.getRows(), sort);
        List<JSONObject> list = new ArrayList<>();
        Page<Task> bySemesterId = taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate equal = cb.equal(root.get("user").get("id").as(String.class), userId);
                query.where(equal);
                return null;
            }
        }, pageable);
        Long counts = bySemesterId.getTotalElements();
        for (Task task : bySemesterId) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickName", task.getUser().getNickName());
            jsonObject.put("courseTimes", task.getCourseTimes());
            jsonObject.put("finishTimes", task.getFinishTimes());
            jsonObject.put("startYear", task.getSemester().getStartYear());
            jsonObject.put("endYear", task.getSemester().getEndYear());
            jsonObject.put("semesterNum", task.getSemester().getSemesterNum());
            jsonObject.put("id", task.getId());
            list.add(jsonObject);
        }
        DataTableReturnObject dro = new DataTableReturnObject();
        dro.setAaData(list);
        dro.setiTotalRecords(counts);
        dro.setiTotalDisplayRecords(counts);
        return dro;
    }

    @Override
    public Task save(Task task) {

        Task save = taskRepository.saveAndFlush(task);
        return save;
    }

    @Override
    public List<Task> findBySemesterId(String id) {
        List<Task> bySemesterId = taskRepository.findBySemesterId(id);
        return bySemesterId;
    }

    @Override
    public void delete(String id) {
        taskRepository.delete(id);
    }

    @Override
    public Task findTaskByUserId(String id) {
        String start ;
        String end;
        int num;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        String[] split = format.split("-");
        String s = split[1];
        int i = Integer.parseInt(s);
        List<Integer> secondSemester = new ArrayList<>();
        secondSemester.add(3);
        secondSemester.add(4);
        secondSemester.add(5);
        secondSemester.add(6);
        secondSemester.add(7);
        secondSemester.add(8);
        boolean contains = secondSemester.contains(i);
        if(contains){
            String temp = split[0];
            Integer integer = Integer.valueOf(temp)-1;
            start=integer.toString();
            end=split[0];
            num=2;
        }else {
            String temp = split[0];
            Integer integer = Integer.valueOf(temp)+1;
            end=integer.toString();
            start=split[0];
            num=1;
        }
        Semester semester = semesterRepository.findOneSemester(start, end, num);
        return taskRepository.findTaskByUserId(id,semester.getId());
    }

    @Override
    public void updateTaskFinishTimes(String userId) {
        Task task = findTaskByUserId(userId);
        if (task != null){
            int finishTimes = task.getFinishTimes();
            task.setFinishTimes(++finishTimes);
            taskRepository.save(task);
        }
    }
}
