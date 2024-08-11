package com.uestc.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.uestc.crowd.entity.Admin;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminService {
//    @Transactional
    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);
    Admin getAdminByLoginAcct(String username);
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void removeById(Integer id);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
