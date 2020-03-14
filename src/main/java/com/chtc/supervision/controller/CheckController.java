package com.chtc.supervision.controller;

import com.alibaba.fastjson.JSON;
import com.chtc.supervision.service.ICourseCommentService;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DataTableUtil;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 审核管理
 */

@Controller
@RequestMapping("/check")
public class CheckController {

    @Autowired
    ICourseCommentService commentService;

    /**
     * 返回到未审核的课堂记录的页面
     * @return 未审核的课堂记录的页面
     */
    @RequestMapping(value = "/uncheckedPage",method = RequestMethod.GET)
    public String uncheckedPage(){
        return "course.uncheckedPage";
    }

    /**
     * 返回到已审核的课堂记录的页面
     * @return 已审核的课堂记录的页面
     */
    @RequestMapping(value = "/checkedPage",method = RequestMethod.GET)
    public String checkedPage(){
        return "course.checkedPage";
    }

    /**
     * 根据request传来的信息,查找符合条件的课程评论
     * @param request 页面传来的request信息
     * @return 尚未审核的听课记录信息（督导员提交）
     */
    @ResponseBody
    @RequestMapping(value = "/checkedManage", produces = "text/html;charset=utf-8")
    public String unchecked(HttpServletRequest request) throws Exception {
        //获取sEcho，sEcho表示请求的次数 需要原封不动的传回页面
        String sEcho = request.getParameter("sEcho");
        //将页面传来的参数进行处理，其中主要是分页所需的信息
        DataRequest dr = DataTableUtil.trans(request);
        //获取查询条件的值
        String searchRecordInfo = request.getParameter("searchRecordInfo");
        //根据状态筛选需要返回的信息
        int state = Integer.parseInt(request.getParameter("state"));
        //进行查询
        DataTableReturnObject dro = commentService.getCourseCommentPage(dr,searchRecordInfo, state);
        //转为json返回
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }

    /**
     * 根据传递的包含所有需要操作的课程评论的id数组和需要修改为的状态，将该数组中所有的课程评论的状态修改为state（2代表审核通过，3代表审核不通过）
     * @param ids 需要更新的课程评论的id数组
     * @param state 课程评论修改后的状态
     * @return 操作反馈信息
     */
    @ResponseBody
    @RequestMapping(value = "/updateCheckedState", produces = "text/html;charset=utf-8")
    public String  updateCheckedState(String[] ids, int state){
        Json json = commentService.updateCheckedState(ids, state);
        return JSON.toJSONString(json, true);
    }

    /**
     * 删除ids中所有id对应的课程评论
     * @param ids 包含所有删除的课程评论id的数组
     * @return 操作反馈信息
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCourseComment", method = RequestMethod.GET)
    public String deleteCourseComment(String[] ids){
        return commentService.deleteCourseComment(ids);
    }

    /**
     * 根据id查找课程评论
     * @param id 课程评论的id
     * @return 操作反馈信息
     */
    @ResponseBody
    @RequestMapping(value = "/findCourseCommentByID",produces = "text/html;charset=utf-8")
    public String findCourseCommentByID(String id){
        return JSON.toJSONString(commentService.findCourseCommentByID(id));
    }

}
