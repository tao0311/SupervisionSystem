package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Menu;
import com.chtc.supervision.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends PagingAndSortingRepository<Menu, String> {
    List<Menu> findAll();
}
