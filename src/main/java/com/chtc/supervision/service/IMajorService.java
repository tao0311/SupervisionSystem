package com.chtc.supervision.service;

import com.chtc.supervision.entity.MajorDic;

import java.util.Set;

public interface IMajorService {

    Set<MajorDic> findAllMajor();

    void insertMajor();
}
