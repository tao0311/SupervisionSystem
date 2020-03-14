package com.chtc.supervision.service;

import com.chtc.supervision.entity.User;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.Json;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    public User findByUserName(String username);

    public DataTableReturnObject getUserPageMode(DataRequest dr, String searchUserInfo,String searchRoleName);

    public void resetUserPassword(String id);

    public User getUser(String id);

    public void saveOrUpdateUser(User user);

    public void deleteUser(String[] ids);

    User getUser();
    //======================新增（胡钢）==================
    List<JSONObject> findUserByDepartmentId(String id);
    //======================新增（宣洪剑）==================
    User findByUserCode(String supervisionCode);

    DataTableReturnObject getSupervisionPageMode(DataRequest dr, String keyName, String userCode, String departmentName);


    User findUserByMobilePhone(String tel, String id);

    User findUserByUserCode(String userCode, String id);

    Json updatePassword(String userName, String newPassWord, String oldPassWord);

    String addUserByFile(MultipartFile excel_file);
}
