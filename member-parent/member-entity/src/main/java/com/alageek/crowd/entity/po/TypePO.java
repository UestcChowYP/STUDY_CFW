package com.alageek.crowd.entity.po;

public class TypePO {
    private Integer id;

    private String name;

    private String remark;

    public TypePO(Integer id, String name, String remark) {
        this.id = id;
        this.name = name;
        this.remark = remark;
    }

    public TypePO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}