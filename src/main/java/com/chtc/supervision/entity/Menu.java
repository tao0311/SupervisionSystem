package com.chtc.supervision.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 */
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity implements java.io.Serializable,Comparable<Menu> {

    //菜单名称
    @NotBlank(message = "此字段不能为空！")
    @Column(length = 50)
    private String menuName;

    //角色对应的资源,也就是要访问的url路径
    @NotBlank(message = "此字段不能为空！")
    private String menuUrl;

    //角色级别
    @NotBlank(message = "此字段不能为空！")
    @Column(length = 50)
    private String menuKey;

    //菜单排序
    private int sequence;

    //是否可用
    @Type(type = "yes_no")
    private boolean enable = true;

    //上级菜单
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    //指定menu和role两个都是维护端，即增加任何一方都会级联到中间表
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int compareTo(Menu o) {
        return this.getSequence() - o.getSequence();
    }
}
