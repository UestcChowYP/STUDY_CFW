package com.alageek.crowd.service.impl;

import com.alageek.crowd.entity.po.MemberConfirmInfoPO;
import com.alageek.crowd.entity.po.MemberLaunchInfoPO;
import com.alageek.crowd.entity.po.ProjectPO;
import com.alageek.crowd.entity.po.ReturnPO;
import com.alageek.crowd.entity.vo.*;
import com.alageek.crowd.mapper.*;
import com.alageek.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        // 保存ProjectPO，获取自增ID
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO, projectPO);
        projectPO.setMemberid(memberId);
        String createdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        projectPO.setCreatedate(createdate);
        projectPO.setStatus(0);
        projectPOMapper.insertSelective(projectPO);
        Integer projectId = projectPO.getId();

        // 保存项目分类的关联关系信息
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        // 保存项目标签的关联关系信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        // 保存项目中详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(detailPicturePathList, projectId);

        // 保存项目发起人信息
        MemberLaunchInfoVO memberLaunchInfoVO = projectVO.getMemberLaunchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        // 保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);

    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查询得到 DetailProjectVO 对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        // 2.根据 status 确定 statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        // 3.根据 deployeDate 计算 lastDay
        // 2020-10-15
        String deployDate = detailProjectVO.getDeployDate();
        // 获取当前日期
        Date currentDay = new Date();
        // 把众筹日期解析成 Date 类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);
            // 获取当前当前日期的时间戳
            long currentTimeStamp = currentDay.getTime();
            // 获取众筹日期的时间戳
            long deployTimeStamp = deployDay.getTime();
            // 两个时间戳相减计算当前已经过去的时间
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
            // 获取总的众筹天数
            Integer totalDays = detailProjectVO.getDay();
            // 使用总的众筹天数减去已经过去的天数得到剩余天数
            Integer lastDay = (int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}
