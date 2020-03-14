package com.chtc.supervision.entity;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * 角色
 */
@Entity
@Table(name = "role")
public class Role extends BaseEntity implements java.io.Serializable {
    //角色中文名称
    @NotBlank(message = "此字段不能为空！")
    @Column(length = 50)
    private String roleName;

    //角色级别，此值唯一不可重复
    @NotBlank(message = "此字段不能为空！")
    @Column(length = 50)
    private String code;

    //是否可用
    @Type(type = "yes_no")
    private boolean enable = true;

    //说明
    @Column(length = 500)
    private String remark;

    //权限集合
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<Menu> menus = new TreeSet<Menu>();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
}
