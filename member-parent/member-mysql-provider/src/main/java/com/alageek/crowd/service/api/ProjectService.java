package com.alageek.crowd.service.api;

import com.alageek.crowd.entity.vo.DetailProjectVO;
import com.alageek.crowd.entity.vo.PortalTypeVO;
import com.alageek.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO,Integer memberId);
    List<PortalTypeVO> getPortalTypeVO();
    DetailProjectVO getDetailProjectVO(Integer projectId);
}
