package com.alageek.crowd.api;

import com.alageek.crowd.entity.po.MemberPO;
import com.alageek.crowd.entity.vo.*;
import com.alageek.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("alageek-crowd-mysql")
public interface MySQLRemoteService {

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);

    @RequestMapping("/get/member/selective/remote")
    public ResultEntity<List<MemberPO>> getMemberSelectiveRemote(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/project/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);
}
