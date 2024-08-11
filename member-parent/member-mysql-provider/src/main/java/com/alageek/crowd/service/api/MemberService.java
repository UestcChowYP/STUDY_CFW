package com.alageek.crowd.service.api;

import com.alageek.crowd.entity.po.MemberPO;

import java.util.List;

public interface MemberService {

    public List<MemberPO> getMemberSelective(MemberPO memberPO);

    public void saveMember(MemberPO memberPO);
}
