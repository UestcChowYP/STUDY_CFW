package com.alageek.crowd.entity.po;

public class TagPO {
    private Integer id;

    private Integer pid;

    private String name;

    public TagPO(Integer id, Integer pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public TagPO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}