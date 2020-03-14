package com.chtc.supervision.repository;

import com.chtc.supervision.entity.Role;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {
     List<Role> findAll();
 }
