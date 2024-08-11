package com.uestc.crowd.entity;

import java.util.List;

public class ParamArray {
    private List<Integer> array;

    public List<Integer> getArray() {
        return array;
    }

    public void setArray(List<Integer> array) {
        this.array = array;
    }

    public ParamArray(List<Integer> array) {
        this.array = array;
    }

    public ParamArray() {
    }

    @Override
    public String toString() {
        return "ParamArray{" +
                "array=" + array +
                '}';
    }
}
