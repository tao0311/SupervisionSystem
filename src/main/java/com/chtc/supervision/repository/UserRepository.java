package com.chtc.supervision.repository;

import com.chtc.supervision.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    @Query("from User where userName=:userName and isDelete = 0")
    User findByUserName(@Param("userName") String userName);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @Query(value = "select * from user where department_id = :department_id", nativeQuery = true)
    List<User> findUserByDepartmentId(@Param("department_id") String id);

    @Query(value = "select * from user where phoneNumber = :tel and id != :id",nativeQuery = true)
    User findUserByMobilePhone(@Param("tel") String tel, @Param("id") String id);

    @Query(value = "select * from user where phoneNumber = :tel",nativeQuery = true)
    User findUserByMobilePhone(@Param("tel") String tel);

    @Query(value = "select * from user where userCode = :userCode and id != :id",nativeQuery = true)
    User findUserByUserCode(@Param("userCode") String userCode, @Param("id") String id);

    @Query(value = "select * from user where userCode = :userCode ",nativeQuery = true)
    User findUserByUserCode(@Param("userCode") String userCode);

    @Transactional
    @Modifying
    @Query(value = "update user SET passWord=:newPassWord where userName=:userName and passWord=:oldPassword",nativeQuery = true)
    void updatePassword(@Param("userName") String userName, @Param("newPassWord") String newPassWord, @Param("oldPassword") String
            oldPassword);

}