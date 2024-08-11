package com.alageek.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginVO implements Serializable {

    private static final long serialVersionUID = 5011911581658105130L;

    private Integer id;
    private String username;
    private String email;

}
