package com.chtc.supervision.service.impl;


import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.repository.DepartmentRepository;
import com.chtc.supervision.repository.RoleRepository;
import com.chtc.supervision.repository.UserRepository;
import com.chtc.supervision.service.IUserService;
import com.chtc.util.*;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    public DataTableReturnObject getUserPageMode(DataRequest dr, String searchUserInfo, String searchRoleName) {
        try {
            //获取要排序的列的列名
            String fieldName = dr.getSidx();
            //排序
            Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord())? Sort.Direction.ASC:Sort.Direction.DESC,fieldName);
            //分页
            Pageable pageable = new PageRequest(dr.getPage()-1,dr.getRows(),sort);
            List<JSONObject> list = new ArrayList<>();
            //查询
            Page<User> page = userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = new ArrayList<>();
                    //未删除的
                    Predicate p0 = cb.equal(root.get("isDelete"),"0");
                    predicateList.add(p0);
                    //用户信息
                    if(!StringUtils.isEmpty(searchUserInfo)){
                        Predicate p1 = cb.like(root.get("nickName").as(String.class), "%" + searchUserInfo + "%");
                        Predicate p2 = cb.like(root.get("userCode").as(String.class), "%" + searchUserInfo + "%");
                        Predicate p3 = cb.like(root.get("phoneNumber").as(String.class), "%" + searchUserInfo + "%");
                        Predicate p4 = cb.like(root.get("email").as(String.class), "%" + searchUserInfo + "%");
                        Predicate p5 = cb.and(cb.or(p1, p2, p3, p4));
                        predicateList.add(p5);
                    }
                    //角色
                    if (!StringUtils.isEmpty(searchRoleName)){
                        Predicate p6 = cb.equal(root.join("roles").get("roleName"),searchRoleName);
                        predicateList.add(p6);
                    }
                    Predicate [] p = new Predicate[predicateList.size()];
                    query.where(cb.and(predicateList.toArray(p)));
                    return null;
                }
            }, pageable);

            List<User> contentList = page.getContent();
            long counts = page.getTotalElements();
            for (User user:contentList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",user.getId());
                jsonObject.put("userCode",user.getUserCode());
                jsonObject.put("nickName",user.getNickName());
                jsonObject.put("departmentName",user.getDepartment().getDepartmentName());
                if (!StringUtils.isEmpty(user.getPhoneNumber())){
                    jsonObject.put("phoneNumber",user.getPhoneNumber());
                }else {
                    jsonObject.put("phoneNumber","");
                }
                if (!StringUtils.isEmpty(user.getEmail())){
                    jsonObject.put("email",user.getEmail());
                }else {
                    jsonObject.put("email","");
                }
                for (Role role:user.getRoles()){
                    jsonObject.put("roleName",role.getRoleName());
                }
                list.add(jsonObject);
            }
            DataTableReturnObject dro = new DataTableReturnObject();
            dro.setAaData(list);
            dro.setiTotalRecords(counts);
            dro.setiTotalDisplayRecords(counts);
            return dro;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resetUserPassword(String id) {
        User user = getUser(id);
        String md5Password = new Md5PasswordEncoder().encodePassword("123456", "");
        user.setPassWord(md5Password);
        userRepository.save(user);
    }


    @Override
    public User getUser(String id) {
        return userRepository.findOne(id);
    }

    @Transactional
    public void saveOrUpdateUser(User user) {
        User securityUser = SecurityUtil.getUser(userRepository);
        if (StringUtil.isNotNull(user.getId())){
            User old = userRepository.findOne(user.getId());
            user.setCreateDate(old.getCreateDate());
            user.setCreateBy(old.getCreateBy());
            user.setPassWord(old.getPassWord());
        }else {
            user.setId(UUID.randomUUID().toString());
            user.setCreateBy(securityUser.getId());
            user.setCreateDate(new Date());
            String md5Password = new Md5PasswordEncoder().encodePassword("123456","");
            user.setPassWord(md5Password);

        }
        user.setUserName(user.getUserCode());
        //院系
        user.setDepartment(departmentRepository.findOne(user.getDepartment().getId()));
        //角色
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findOne(user.getRoleIDs()));
        user.setRoles(roles);

        user.setUpdateDate(new Date());
        user.setUpdateBy(securityUser.getId());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String[] ids) {
        for (String id:ids){
            User deleteUser = userRepository.findOne(id);
            deleteUser.setIsDelete(1);
            userRepository.save(deleteUser);
        }
    }

    @Override
    public User getUser() {
        return SecurityUtil.getUser(userRepository);
    }


    /**
     * 根据部门id查询该部门所有的教师
     * @param id 部门id
     * @return 封装了老师信息的列表
     */
    @Override
    public List<JSONObject> findUserByDepartmentId(String id) {
        List<User> users = userRepository.findUserByDepartmentId(id);
        List<JSONObject> list = new ArrayList<>();

        for(User user : users){
            JSONObject object = new JSONObject();
            object.put("id", user.getId());
            object.put("code", user.getUserCode());
            object.put("nickName", user.getNickName());
            list.add(object);
        }

        return list;
    }

    @Override
    public User findByUserCode(String supervisionCode) {
        return userRepository.findUserByUserCode(supervisionCode);
    }

    @Override
    public DataTableReturnObject getSupervisionPageMode(DataRequest dr, String keyName, String userCode, String departmentName) {
        try {
            //获取要排序的列的列名
            String fieldName = dr.getSidx();
            //排序
            Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord())? Sort.Direction.ASC:Sort.Direction.DESC,fieldName);
            //分页
            Pageable pageable = new PageRequest(dr.getPage()-1,dr.getRows(),sort);
            List<JSONObject> list = new ArrayList<>();
            Page<User> supervisionpage;
            supervisionpage = userRepository.findAll(new Specification<User>() {
                Predicate p1;
                Predicate p2;
                Predicate p3;
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    Predicate p0 = cb.equal(root.get("isDelete"),"0");
                    predicates.add(p0);
                    if(!StringUtils.isEmpty(keyName)){
                        p1=cb.like(root.get("nickName").as(String.class), "%"+keyName+"%");
                        predicates.add(p1);
                    }
                    if(!StringUtils.isEmpty(userCode)){
                        p2=cb.equal(root.get("userCode").as(String.class), userCode);
                        predicates.add(p2);
                    }
                    if(!StringUtils.isEmpty(departmentName)){
                        p3=cb.equal(root.join("department").get("id").as(String.class),departmentName);
                        predicates.add(p3);
                    }
//                    ListJoin<User, Role> roles1 = root.join(root.getModel().getList("roles", Role.class));
//                    Predicate p4 = cb.equal(roles1.get("roleName").as(String.class), "督导员");
                    Predicate p4 = cb.equal(root.join("roles").get("code").as(String.class), "ROLE_SUPERVISOR");
                    predicates.add(p4);
                    Predicate [] p = new Predicate[predicates.size()];
                    query.where(cb.and(predicates.toArray(p)));
                    return null;
                }
            }, pageable);



            long counts = supervisionpage.getTotalElements();
            for (User user:supervisionpage){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userCode",user.getUserCode());
                jsonObject.put("nickName",user.getNickName());
                jsonObject.put("departmentName",user.getDepartment().getDepartmentName());
                if (!StringUtils.isEmpty(user.getPhoneNumber())){
                    jsonObject.put("phoneNumber",user.getPhoneNumber());
                }else {
                    jsonObject.put("phoneNumber","");
                }
                if (!StringUtils.isEmpty(user.getEmail())){
                    jsonObject.put("email",user.getEmail());
                }else {
                    jsonObject.put("email","");
                }
                for (Role role:user.getRoles()){
                    jsonObject.put("roleName",role.getRoleName());
                }
                list.add(jsonObject);
            }
            DataTableReturnObject dro = new DataTableReturnObject();
            dro.setAaData(list);
            dro.setiTotalRecords(counts);
            dro.setiTotalDisplayRecords(counts);
            return dro;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public User findUserByMobilePhone(String tel, String id) {
        if (!StringUtils.isEmpty(id)){
            return userRepository.findUserByMobilePhone(tel, id);
        }
        return userRepository.findUserByMobilePhone(tel);
    }
    @Override
    public User findUserByUserCode(String userCode, String id) {
        if (!StringUtils.isEmpty(id)){
            return userRepository.findUserByUserCode(userCode, id);
        }
        return userRepository.findUserByUserCode(userCode);
    }

    @Override
    public Json updatePassword(String userName, String newPassWord, String oldPassWord) {
        Json json = new Json();
        newPassWord = new Md5PasswordEncoder().encodePassword(newPassWord,"");
        oldPassWord = new Md5PasswordEncoder().encodePassword(oldPassWord,"");
        User user = userRepository.findByUserName(userName);
        if (user == null || !user.getPassWord().equals(oldPassWord)){
            json.setSuccess(false);
            json.setMsg("用户名或者密码错误");
            return json;
        }
        userRepository.updatePassword(userName,newPassWord,oldPassWord);
        json.setMsg("修改成功,直接跳往登录页面");
        json.setSuccess(true);
        return json;
    }

    @Override
    public String addUserByFile(MultipartFile excel_file) {
        List<Sheet> sheets = new ArrayList<>();
        try {
            sheets = ExcelUtil.getSheets(excel_file.getInputStream(), excel_file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SheetIterator(sheets);
    }

    /**3.对所有工作表进行操作
     * 遍历每一行（第一行为标题，数据从第二行开始），再遍历每一行的每一列
     * 将每一行的数据封装成对象，保存进数据库
     * @param sheets 要操作的工作表
     */
    private String SheetIterator(List<Sheet> sheets) {
        /*
         * 存储要存入的用户
         */
        List<User> userList = new ArrayList<>();
        //记录当前是第几张表
        int i = 0;
        while (i < sheets.size()) {    //循环每一张工作表
            Sheet sheet = sheets.get(i);
            //用两个while循环遍历所有单元格
            for (Row nextRow : sheet) {           //遍历每一行
                if (nextRow.getRowNum() < 1) {
                    continue;
                }

                //从第二行开始是有用的数据，要保存到数据库，第二行：nextRow.getRowNum()=1
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                User user = new User();

                //当前行数
                int rowNum = nextRow.getRowNum() + 1;
                //通用的添加错误提示头
                String errorInfoHead = "添加失败 \n" + "出错原因：\n    " + "第" + (i + 1)+ "张表中的第" + rowNum + "行的";

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()) {  //遍历每一行的每一列
                        case 0:
                            cell.setCellType(CellType.STRING);
                            String userCode = cell.getStringCellValue();

                            if (StringUtils.isEmpty(userCode)){
                                return errorInfoHead + "用户编号不能为空，请仔细核对！";
                            }

                            for(User everyUser: userList){
                                if (everyUser.getUserCode().equals(userCode)){
                                    return errorInfoHead + "编号在Excel表中重复，请仔细核对！";
                                }
                            }

                            User findUser = userRepository.findUserByUserCode(userCode);
                            if (findUser != null){
                                return errorInfoHead + "用户编号已经存在，不可重复添加";
                            }

                            user.setUserCode(userCode);
                            user.setUserName(userCode);
                            break;
                        case 1:
                            cell.setCellType(CellType.STRING);
                            user.setNickName(cell.getStringCellValue());
                            if (StringUtils.isEmpty(cell.getStringCellValue())){
                                return errorInfoHead + "用户姓名不能为空，请仔细核对！";
                            }
                            break;
                        case 2:
                            cell.setCellType(CellType.STRING);
                            //填写的性别，值为男或女
                            String gender =  cell.getStringCellValue();
                            switch (gender) {
                                case "男":
                                    user.setGenner(1);
                                    break;
                                case "女":
                                    user.setGenner(2);
                                case "":
                                    user.setGenner(0);
                                    break;
                                default:
                                    return errorInfoHead + "性别输入错误";
                            }
                            break;
                        case 3:
                            //将输入的日期进行处理
                            //isCellDateFormatted可能会抛出IllegalStateException，如果抛出，说明输入的日期不正确
                            try{
                                if (HSSFDateUtil.isCellDateFormatted(cell)){
                                    Date date = cell.getDateCellValue();
                                    user.setBirthday(date);
                                }
                            } catch (IllegalStateException ex){
                                return errorInfoHead + "生日格式输入错误";
                            }

                            break;
                        case 4:
                            cell.setCellType(CellType.STRING);
                            String phoneNumber = cell.getStringCellValue().trim();
                            if (!StringUtils.isEmpty(phoneNumber)){
                                Pattern phonePattern = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
                                //判断手机号码格式是否正确
                                if (!phonePattern.matcher(phoneNumber).matches())
                                    return errorInfoHead + "手机号码输入错误";

                                for(User everyUser: userList){
                                    if (everyUser.getPhoneNumber().equals(phoneNumber)){
                                        return errorInfoHead + "手机号码在Excel表中重复，请仔细核对！";
                                    }
                                }

                                User userByMobilePhone = userRepository.findUserByMobilePhone(phoneNumber);
                                if (userByMobilePhone != null){
                                    return errorInfoHead + "手机号码已经存在，不可重复添加";
                                }
                                user.setPhoneNumber(phoneNumber);
                            }else {
                                user.setPhoneNumber("");
                            }
                            break;
                        case 5:
                            cell.setCellType(CellType.STRING);
                            String qq = cell.getStringCellValue().trim();
                            if (!StringUtils.isEmpty(qq)){
                                Pattern qqPattern = Pattern.compile("^\\d{1,10}$");
                                //判断QQ是否正确
                                if (!qqPattern.matcher(qq).matches())
                                    return errorInfoHead + "QQ号码输入错误";
                                user.setQq(qq);
                            }else {
                                user.setQq("");
                            }
                            break;
                        case 6:
                            cell.setCellType(CellType.STRING);
                            String email = cell.getStringCellValue().trim();
                            if (!StringUtils.isEmpty(email)){
                                //验证邮箱格式
                                Pattern emailPattern = Pattern.compile("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                                if(!emailPattern.matcher(email).matches())
                                    return errorInfoHead + "邮箱格式不对";

                                user.setEmail(email);
                            }else {
                                user.setEmail("");
                            }

                            break;
                        case 7:
                            cell.setCellType(CellType.STRING);
                            String academicTitle = cell.getStringCellValue().trim();

                            switch (academicTitle){
                                case "助教":
                                    user.setAcademicTitle(1);
                                    break;
                                case "讲师":
                                    user.setAcademicTitle(2);
                                    break;
                                case "副教授":
                                    user.setAcademicTitle(3);
                                    break;
                                case "教授":
                                    user.setAcademicTitle(4);
                                    break;
                                default:
                                    return errorInfoHead + "职称输入错误";
                            }

                            break;
                        case 8:
                            cell.setCellType(CellType.STRING);
                            String role = cell.getStringCellValue().trim();

                            switch (role){
                                case "管理员":
                                    user.setRoleIDs("1");
                                    break;
                                case "督导员":
                                    user.setRoleIDs("2");
                                    break;
                                case "一般教师":
                                    user.setRoleIDs("3");
                                    break;
                                default:
                                    return errorInfoHead + "角色输入错误";
                            }

                            break;
                        case 9:
                            cell.setCellType(CellType.STRING);
                            String departmentName = cell.getStringCellValue();

                            Department department = departmentRepository.findOneByDepartmentName(departmentName);
                            if (department == null){
                                return errorInfoHead + "部门输入错误";
                            }

                            user.setDepartment(department);
                            break;
                        case 10:
                            cell.setCellType(CellType.STRING);
                            user.setRemark(cell.getStringCellValue());
                    }
                }
                user.setId(UUID.randomUUID().toString());
                user.setCreateDate(new Date());
                String md5Password = new Md5PasswordEncoder().encodePassword("123456","");
                user.setPassWord(md5Password);
                //角色
                List<Role> roles = new ArrayList<>();
                roles.add(roleRepository.findOne(user.getRoleIDs()));
                user.setRoles(roles);

                user.setUpdateDate(new Date());
                userList.add(user);
            }
            i++;
        }

        userRepository.save(userList);
        return "用户添加成功";
    }


}
