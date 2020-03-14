package com.chtc.supervision.repository;

import com.chtc.supervision.entity.MajorDic;
import com.chtc.supervision.entity.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MajorRepository extends CrudRepository<MajorDic, String>{

    Set<MajorDic> findAll();
}
