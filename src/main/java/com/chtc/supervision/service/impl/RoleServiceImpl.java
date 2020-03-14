package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Role;
import com.chtc.supervision.repository.RoleRepository;
import com.chtc.supervision.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<Role> getRoleNames() {
        return roleRepository.findAll();
    }
}
