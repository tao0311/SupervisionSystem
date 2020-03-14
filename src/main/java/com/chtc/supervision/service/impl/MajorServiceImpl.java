package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.MajorDic;
import com.chtc.supervision.repository.MajorRepository;
import com.chtc.supervision.service.IMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class MajorServiceImpl implements IMajorService {

    @Autowired
    MajorRepository majorRepository;

    /**
     * 查找所有专业
     * @return 专业列表
     */
    @Override
    public Set<MajorDic> findAllMajor() {
        return majorRepository.findAll();
    }

    /**
     * 初始化专业表
     */
    @Override
    public void insertMajor() {
        String[] arr = {          "软件工程",
                "金融工程",
                "国际经济与贸易",
                "法学",
                "教育技术学",
                "学前教育",
                "体育教育",
                "汉语言文学",
                "英语",
                "商务英语",
                "广告学",
                "历史学",
                "数学与应用数学",
                "信息与计算科学",
                "物理学",
                "应用化学",
                "应用心理学",
                "统计学",
                "机械设计制造及其自动化",
                "无机非金属材料工程",
                "电气工程及其自动化",
                "电子信息工程",
                "电子科学与技术",
                "微电子科学与工程",
                "计算机科学与技术",
                "网络工程",
                "化学工程与工艺",
                "生物工程",
                "市场营销",
                "财务管理",
                "公共事业管理",
                "电子商务",
                "旅游管理",
                "酒店管理",
                "动画",
                "美术学",
                "视觉传达设计",
                "环境设计"};

        for (String s : arr){
            MajorDic majorDic = new MajorDic();
            majorDic.setId(UUID.randomUUID().toString());
            majorDic.setCreateDate(new Date());
            majorDic.setMajorName(s);
            majorRepository.save(majorDic);
        }

    }
}
