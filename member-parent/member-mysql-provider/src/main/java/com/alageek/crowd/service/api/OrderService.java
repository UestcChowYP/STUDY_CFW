package com.alageek.crowd.service.api;

import com.alageek.crowd.entity.vo.AddressVO;
import com.alageek.crowd.entity.vo.OrderProjectVO;
import com.alageek.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);
    List<AddressVO> getAddressVOList(Integer memberId);
    void saveAddress(AddressVO addressVO);
    void saveOrder(OrderVO orderVO);
}
