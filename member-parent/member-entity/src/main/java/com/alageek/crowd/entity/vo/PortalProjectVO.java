package com.alageek.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalProjectVO {
    private Integer projectid;
    private String projectName;
    private String headerPicturePath;
    private Long money;
    private String deployDate;
    private Integer percentage;
    private Integer supporter;
}
