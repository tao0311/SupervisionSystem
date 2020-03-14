package com.chtc.supervision.service.impl;

import com.chtc.supervision.dto.CourseDTO;
import com.chtc.supervision.dto.SemesterDto;
import com.chtc.supervision.entity.*;
import com.chtc.supervision.repository.*;
import com.chtc.supervision.service.ICourseService;
import com.chtc.util.*;
import com.chtc.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MajorRepository majorRepository;


    //TODO 重构
    @Override
    public DataTableReturnObject getCoursePageMode(DataRequest dr, CourseQueryUtil courseQuery) {
        //获取要排序的列的列名
        String fieldName = dr.getSidx();
        //排序
        Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord())? Sort.Direction.ASC:Sort.Direction.DESC,fieldName);
        //分页
        Pageable pageable = new PageRequest(dr.getPage()-1,dr.getRows(),sort);
        List<JSONObject> list = new ArrayList<>();
        Semester semester = null;
        String semesterId = "";
        User user = SecurityUtil.getUser(userRepository);
            if (SecurityUtil.getCode(user).equals("ROLE_ADMIN")){
                /**判断是否选定了学期
                 * 选定了：查询相关信息
                 * 未选定：返回空信息
                 */
//                Semester semester = null;
                String semesterYear = courseQuery.getSemesterYear();
                String semesterNum = courseQuery.getSemesterNum();
                if (!StringUtils.isEmpty(semesterYear)&&!StringUtils.isEmpty(semesterNum)) {
                    String start_year = semesterYear.substring(0, 4);
                    String end_year = semesterYear.substring(5, 9);
                    semester = semesterRepository.findOneSemester(start_year, end_year, Integer.parseInt(semesterNum));
                }else {
                    return getNullDataTable();
                }

                if (semester == null){
                    return getNullDataTable();
                }else {
                     semesterId = semester.getId();
                }
            }
        String finalSemesterId = semesterId;
        Page<Course> page = courseRepository.findAll(new Specification<Course>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                String searchCourseInfo = courseQuery.getSearchCourseInfo();
                String searchCourseDate = DateUtil.dateToWeek(DateUtil.getStringDate());

                if (!StringUtils.isEmpty(searchCourseInfo)){
                    Predicate p1 = cb.like(root.get("courseName").as(String.class), "%" + searchCourseInfo + "%");
                    Predicate p2 = cb.like(root.get("courseRoom").as(String.class), "%" + searchCourseInfo + "%");
                    Predicate p3 = cb.like(root.get("user").get("nickName").as(String.class), "%" + searchCourseInfo + "%");
                    Predicate p4 = cb.like(root.get("courseCode").as(String.class), "%" + searchCourseInfo + "%");
                    Predicate p5 = cb.like(root.get("major").as(String.class), "%" + searchCourseInfo + "%");
                    Predicate p6 = cb.and(cb.or(p1,p2,p3,p4,p5));
                    predicateList.add(p6);
                }
                if (!StringUtils.isEmpty(finalSemesterId)){
                    Predicate p7 = cb.equal(root.join("semester").get("id").as(String.class), finalSemesterId);
                    predicateList.add(p7);
                }
                if (SecurityUtil.getCode(user).equals("ROLE_SUPERVISOR")){
                    Predicate p8 = cb.equal(root.get("courseDate").as(String.class), searchCourseDate);
                    predicateList.add(p8);
                }
                Predicate p9 = cb.equal(root.get("isDelete").as(Integer.class),0);
                predicateList.add(p9);

                Predicate [] p = new Predicate[predicateList.size()];
                query.where(cb.and(predicateList.toArray(p)));
                return null;
            }
        },pageable);
        List<Course> contentList = page.getContent();
        long counts = page.getTotalElements();
        putDataToList(contentList,list);

        DataTableReturnObject dro = new DataTableReturnObject();
        dro.setiTotalRecords(counts);
        dro.setiTotalDisplayRecords(counts);
        dro.setAaData(list);
        return dro;
    }

    @Override
    public CourseDTO queryCourseById(String id) {
        Course queryCourse = courseRepository.findOne(id);
        CourseDTO course = new CourseDTO();
        //封装基本属性
        course.setId(queryCourse.getId());
        course.setCourseCode(queryCourse.getCourseCode());
        course.setCourseName(queryCourse.getCourseName());
        course.setCourseRoom(queryCourse.getCourseRoom());
        course.setOddEven(queryCourse.getOddEven());
        course.setCourseDate(queryCourse.getCourseDate());
        course.setCourseNode(queryCourse.getCourseNode());
        course.setCourseNum(queryCourse.getCourseNum());
        course.setStartWeek(queryCourse.getStartWeek());
        course.setEndWeek(queryCourse.getEndWeek());
        course.setGrade(queryCourse.getGrade());
        course.setMajor(queryCourse.getMajor());
        course.setClasses(queryCourse.getClasses());

        //封装所有部门
        List<Department> departmentList = departmentRepository.findAll();
        course.setDepartments(departmentList);

        User teacher = queryCourse.getUser();
        String departmentId = teacher.getDepartment().getId();

        //封装目前学院的教师列表
        List<User> teacherList = userRepository.findUserByDepartmentId(departmentId);
        course.setTeachers(teacherList);

        //封装所有学年
        List<Semester> semesterList = semesterRepository.findAll();
        //set防止重复
        Set<SemesterDto> semesterDtoSet = new TreeSet<>();
        for (Semester semester : semesterList) {
            SemesterDto dto = new SemesterDto();
            String semester_year = semester.getStartYear() + "-" + semester.getEndYear() + "学年";
            dto.setSemesterInfo(semester_year);
            dto.setId(semester.getId());
            dto.setStartYear(semester.getStartYear());
            semesterDtoSet.add(dto);
        }
        course.setSemesters(semesterDtoSet);

        //封装当前学期
        Semester semester = queryCourse.getSemester();
        course.setSemesterYear(semester.getStartYear() + "-" + semester.getEndYear() + "学年");
        course.setSemesterNum(semester.getSemesterNum());

        //封装当前部门
        course.setDepartmentId(departmentId);
        //封装当前教师
        course.setTeacherId(teacher.getId());

        //封装所有专业
        course.setMajorDicSet(majorRepository.findAll());

        return course;
    }

    /**
     * 手动添加课程
     * @param course 要添加的课程信息
     */
    @Override
    public String addCourseManual(CourseDTO course) {
        //将DTO中的数据封装进实体类中
        Course dbCourse = new Course();
        dbCourse.setId(UUID.randomUUID().toString());

        String info = packetDtoToDbEntity(course, dbCourse);
        if (!info.equals("")) {
            return info;
        }

        dbCourse.setVersion(1);
        dbCourse.setCreateDate(new Date());
        User user = SecurityUtil.getUser(userRepository);
        dbCourse.setCreateBy(user.getNickName());

        //课程保存到数据库
        courseRepository.save(dbCourse);
        return "添加课程成功";
    }

    /**
     * 通用的将课程dto类封装成存入数据库的实体类
     *
     * @param dto      dto类
     * @param dbCourse 数据库实体类
     * @return 返回的信息，可能是错误信息，成功则返回""
     */
    private String packetDtoToDbEntity(CourseDTO dto, Course dbCourse) {
        //封装基本属性
        dbCourse.setCourseCode(dto.getCourseCode());
        dbCourse.setCourseName(dto.getCourseName());
        dbCourse.setCourseRoom(dto.getCourseRoom());
        dbCourse.setOddEven(dto.getOddEven());
        dbCourse.setCourseDate(dto.getCourseDate());
        dbCourse.setCourseNode(dto.getCourseNode());
        dbCourse.setCourseNum(dto.getCourseNum());
        dbCourse.setStartWeek(dto.getStartWeek());
        dbCourse.setEndWeek(dto.getEndWeek());
        dbCourse.setGrade(dto.getGrade());
        dbCourse.setMajor(dto.getMajor());
        dbCourse.setClasses(dto.getClasses());

        //封装外键user_id
        User user = new User();
        user.setId(dto.getTeacherId());
        dbCourse.setUser(user);

        /*
         * 封装外键semester_id
         */

        //传来的学期对象，我们要从其中获取学年信息
        Semester semester = new Semester();
        String semesterDetails = dto.getSemesterYear();
        String startYear = semesterDetails.substring(0, 4);
        String endYear = semesterDetails.substring(5, 9);
        int semesterNum = dto.getSemesterNum();

        //根据学年和学期数从数据库查询某个学期信息
        Semester querySemester = semesterRepository.findOneSemester(startYear, endYear, semesterNum);
        if (querySemester == null) {
            return "选择的学期不存在，请联系管理员添加学期";
        }
        dbCourse.setSemester(querySemester);
        return "";
    }

    /**
     * 根据Excel文件添加课程
     * @param excel_file Excel文件
     * @return 成功与否信息
     */
    @Override
    public String addCourseByFile(MultipartFile excel_file) {

        Workbook book = null;   //1.获取工作簿
        try {
            book = getWorkBook(excel_file.getInputStream(), excel_file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Sheet> sheets = getSheets(book);   //2.获取所有工作表

        return SheetIterator(sheets);
    }


    //1.获取工作簿
    private Workbook getWorkBook(InputStream in, String path) throws IOException {
        return path.endsWith(".xls") ? (new HSSFWorkbook(in))
                : (path.endsWith(".xlsx") ? (new XSSFWorkbook(in)) : (null));
    }

    //2.获取所有工作表
    private List<Sheet> getSheets(Workbook book) {
        int numberOfSheets = book.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int i = 0; i < numberOfSheets; i++) {
            sheets.add(book.getSheetAt(i));
        }
        return sheets;
    }

    /**3.对所有工作表进行操作
     * 遍历每一行（第一行为标题，数据从第二行开始），再遍历每一行的每一列
     * 将每一行的数据封装成对象，保存进数据库
     * @param sheets 要操作的工作表
     */
    private String SheetIterator(List<Sheet> sheets) {
        /*
         * 存储要存入的课程
         */
        List<Course> courseList = new ArrayList<>();
        //记录当前是第几张表
        int i = 0;
        while (i < sheets.size()) {    //循环每一张工作表
            Sheet sheet = sheets.get(i);
            //用两个while循环遍历所有单元格
            for (Row nextRow : sheet) {           //遍历每一行
                if (nextRow.getRowNum() < 1) {
                    continue;
                    //nextRow.getRowNum()就是获取行数，由表中看出第一行(getRowNum()=0)为表头，直接跳过
                }

                //从第二行开始是有用的数据，要保存到数据库，第二行：nextRow.getRowNum()=1
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Course course = new Course();
                int startYear = 0;
                int endYear = 0;

                //当前行数
                int rowNum = nextRow.getRowNum() + 1;
                //通用的添加错误提示头
                String errorInfoHead = "添加失败 \n" + "出错原因：\n    " + "第" + (i + 1)+ "张表中的第" + rowNum + "行的";

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()) {  //遍历每一行的每一列
                        case 0:
                            cell.setCellType(CellType.STRING);
                            course.setCourseCode(cell.getStringCellValue());
                            break;
                        case 1:
                            cell.setCellType(CellType.STRING);
                            course.setCourseName(cell.getStringCellValue());
                            break;
                        case 2:
                            cell.setCellType(CellType.STRING);
                            course.setCourseRoom(cell.getStringCellValue());
                            break;
                        case 3:
                            cell.setCellType(CellType.STRING);
                            course.setOddEven(cell.getStringCellValue());
                            break;
                        case 4:
                            cell.setCellType(CellType.STRING);
                            course.setCourseDate(cell.getStringCellValue());
                            break;
                        case 5:
                            try {
                                course.setCourseNode((int) (cell.getNumericCellValue()));
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "课程开始节数列的值必须为纯数字";
                            }
                            break;
                        case 6:
                            try {
                                course.setCourseNum((int) cell.getNumericCellValue());
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "课程节数列的值必须为纯数字";
                            }
                            break;
                        case 7:
                            try {
                                course.setStartWeek((int) cell.getNumericCellValue());
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "起始周列的值必须为纯数字";
                            }
                            break;
                        case 8:
                            try {
                                course.setEndWeek((int) cell.getNumericCellValue());
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "结束周列的值必须为纯数字";
                            }
                            break;
                        //年级
                        case 9:
                            try {
                                course.setGrade((int) cell.getNumericCellValue());
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "年级列的值必须为纯数字";
                            }
                            break;
                        case 10:
                            cell.setCellType(CellType.STRING);
                            course.setMajor(cell.getStringCellValue());
                            break;
                        //几班
                        case 11:
                            try {
                                int classes = (int) cell.getNumericCellValue();
                                if (classes != 1 && classes != 2 && classes != 3)
                                    return errorInfoHead + "班级列的值只能为1或2或3";
                                course.setClasses((int) cell.getNumericCellValue());
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "班级列的值必须为纯数字";
                            }
                            break;
                        //授课教师工号
                        case 12:
                            cell.setCellType(CellType.STRING);
                            User user = userRepository.findUserByUserCode(cell.getStringCellValue());
                            if (user == null) {
                                return errorInfoHead + "工号输入错误，所对应的教师不存在";
                            }
                            course.setUser(user);
                            break;

                        //学年
                        case 13:

                            try {
                                startYear = (int) cell.getNumericCellValue();
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "起始学年列的值必须为纯数字";
                            }
                            endYear = startYear + 1;
                            break;

                        //学期
                        case 14:
                            int semesterNum;
                            try {
                                semesterNum = (int) cell.getNumericCellValue();
                                if (semesterNum != 1 && semesterNum != 2)
                                    return errorInfoHead + "学期列的值只能为1或2";
                            } catch (IllegalStateException illegal) {
                                return errorInfoHead + "学期列的值必须为纯数字";
                            }

                            Semester querySemester = semesterRepository.findOneSemester(String.valueOf(startYear),
                                    String.valueOf(endYear), semesterNum);
                            if (querySemester == null) {
                                return errorInfoHead + "学期信息输入错误，数据库不存在该学期";
                            }
                            course.setSemester(querySemester);
                    }
                }
                course.setVersion(1);
                course.setCreateDate(new Date());
                User user = SecurityUtil.getUser(userRepository);
                course.setCreateBy(user.getNickName());
                course.setId(UUID.randomUUID().toString());

                courseList.add(course);
            }
            i++;
        }

        courseRepository.save(courseList);
        return "课表添加成功";
    }

    /**
     * 根据课程编号查询课程
     * @param code 课程编号
     * @return 查询到课程名
     */
    @Override
    public String queryCourseByCode(String code) {
        //用List的原因是：数据库course表有冗余，根据code会查到多条记录（id和一些属性不同，但课程名和编号是一样的）
        List<Course> course = courseRepository.findCourseByCode(code);
        if (course.size() != 0) {
            return course.get(0).getCourseName();
        } else {
            return "";
        }
    }


    /**
     * 封装一个空的DataTableReturnObject，并返回
     * @return 一个空的DataTableReturnObject
     */
    private DataTableReturnObject getNullDataTable(){
        DataTableReturnObject dro = new DataTableReturnObject();
        dro.setAaData(new ArrayList());
        dro.setiTotalRecords(0);
        dro.setiTotalDisplayRecords(0);
        return dro;
    }

    //处理向前台传递的数据
    private void putDataToList(List<Course> contentList, List<JSONObject> list) {
        User user = SecurityUtil.getUser(userRepository);
        for (Course course:contentList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",course.getId());
            jsonObject.put("courseName",course.getCourseName());
            jsonObject.put("courseRoom",course.getCourseRoom());
            jsonObject.put("courseTeacher",course.getUser().getNickName());
            StringBuilder courseWeek = new StringBuilder();
            jsonObject.put("courseDate",course.getCourseDate());
            //课程及单双周
            courseWeek.append(course.getStartWeek()).append("-").append(course.getEndWeek());
            if (!"全周".equals(course.getOddEven()))
                 courseWeek.append(" ").append(course.getOddEven());
                 jsonObject.put("courseWeek", courseWeek.toString());
                 jsonObject.put("courseNode", course.getCourseNode() + "-" + (course.getCourseNode() + course.getCourseNum() - 1) + "节");

                    //专业，班级
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
                 jsonObject.put("courseClass", classes.toString());
                 if (SecurityUtil.getCode(user).equals("ROLE_SUPERVISOR")){
                     jsonObject.put("courseTime",DateUtil.getStringDate());
                 }
                 if (SecurityUtil.getCode(user).equals("ROLE_ADMIN")){
                     jsonObject.put("courseCode", course.getCourseCode());
                 }
                 list.add(jsonObject);
        }

    }

    /**
     * 更新课程信息
     * @param course 要更新的课程的dto类
     * @return 返回信息
     */
    @Override
    public String updateCourse(CourseDTO course) {
        Course queryCourse = courseRepository.findOne(course.getId());
        String info = packetDtoToDbEntity(course, queryCourse);
        if (!info.equals("")) {
            return info;
        }
        queryCourse.setVersion(queryCourse.getVersion() + 1);
        queryCourse.setUpdateDate(new Date());
        User user = SecurityUtil.getUser(userRepository);
        queryCourse.setUpdateBy(user.getNickName());

        courseRepository.save(queryCourse);
        return "修改课表成功";
    }

    /**
     * 删除课程
     * @param ids 要删除的课程数组
     * @return 反馈信息
     */
    @Override
    public String deleteCourse(String[] ids) {
        String msg;
        try {
            for (String id : ids)
                courseRepository.deleteCourseComment(id);
        } catch (Exception e) {
            msg = "操作失败，请重试";
            return msg;
        }
        msg = "操作成功";
        return msg;
    }

    @Override
    public DataTableReturnObject getCourseToMobile(DataRequest dr, String searchCourseInfo) {
        try {
            //获取要排序的列的列名
            String fieldName = dr.getSidx();
            //排序
            Sort sort = new Sort("asc".equalsIgnoreCase(dr.getSord())? Sort.Direction.ASC:Sort.Direction.DESC,fieldName);
            //分页
            Pageable pageable = new PageRequest(dr.getPage()-1,dr.getRows(),sort);
            List<JSONObject> list = new ArrayList<>();
            //查询
            Page<Course> page = courseRepository.findAll(new Specification<Course>() {
                @Override
                public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = new ArrayList<>();
                    //用户信息
                    if(!StringUtils.isEmpty(searchCourseInfo)){
                        Predicate p1 = cb.like(root.get("courseName").as(String.class), "%" + searchCourseInfo + "%");
                        Predicate p2 = cb.like(root.get("courseRoom").as(String.class), "%" + searchCourseInfo + "%");
                        Predicate p3 = cb.like(root.get("major").as(String.class), "%" + searchCourseInfo + "%");
                        Predicate p4 = cb.like(root.get("user").get("nickName").as(String.class), "%" + searchCourseInfo + "%");
                        Predicate p5 = cb.and(cb.or(p1, p2, p3, p4));
                        predicateList.add(p5);
                    }
                    Predicate p6 = cb.equal(root.get("courseDate").as(String.class), DateUtil.dateToWeek(DateUtil.getStringDate()));
                    predicateList.add(p6);
                    Predicate p7 = cb.equal(root.get("isDelete").as(Integer.class),0);
                    predicateList.add(p7);
                    Predicate [] p = new Predicate[predicateList.size()];
                    query.where(cb.and(predicateList.toArray(p)));
                    return null;
                }
            }, pageable);

            List<Course> contentList = page.getContent();
            long counts = page.getTotalElements();
            for (Course course:contentList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",course.getId());
                jsonObject.put("courseName",course.getCourseName());
                jsonObject.put("courseRoom",course.getCourseRoom());
                jsonObject.put("courseDate",course.getCourseDate());
                jsonObject.put("courseNode", course.getCourseNode() + "-" + (course.getCourseNode() + course.getCourseNum() - 1) + "节");
                jsonObject.put("courseTeacher",course.getUser().getNickName());
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
}
