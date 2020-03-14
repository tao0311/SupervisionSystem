package com.chtc.supervision.service;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.Task;
import com.chtc.supervision.entity.User;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;

import java.util.List;

public interface ITaskService {

    DataTableReturnObject getTaskByUserId(String userId, DataRequest dataRequest);

    Task save(Task task);

    List<Task> findBySemesterId(String id);

    void delete(String id);

    Task findTaskByUserId(String id);

    void updateTaskFinishTimes(String userId);
}
