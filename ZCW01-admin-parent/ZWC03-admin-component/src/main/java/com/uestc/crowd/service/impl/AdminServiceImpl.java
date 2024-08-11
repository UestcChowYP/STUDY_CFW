package com.uestc.crowd.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uestc.crowd.entity.Admin;
import com.uestc.crowd.entity.AdminExample;
import com.uestc.crowd.exception.LoginAcctAlreadyInUseException;
import com.uestc.crowd.exception.LoginAcctAlreadyInUseUpdateException;
import com.uestc.crowd.exception.LoginFailedException;
import com.uestc.crowd.mapper.AdminMapper;
import com.uestc.crowd.service.api.AdminService;
import com.uestc.crowd.util.CrowdConstant;
import com.uestc.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 为了简化操作：先根据 adminId 删除旧的数据，再根据 roleIdList 保存全部新的数据
        // 1.根据 adminId 删除旧的关联关系数据
        adminMapper.deleteOLdRelationship(adminId);
        // 2.根据 roleIdList 和 adminId 保存新的关联关系
        if (roleIdList != null && !roleIdList.isEmpty()) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public void update(Admin admin) {
        // 有选择的更新， 对于带有null值的字段不更新

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            logger.info(e.getClass().getName());
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }

        }
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void removeById(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }


    @Override
//    @Transactional
    public void saveAdmin(Admin admin) {
        // 1. 密码加密
        String userPswd = admin.getUserPswd();
        userPswd = bCryptPasswordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);
        // 2.生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);
        // 3. 保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            logger.info(e.getClass().getName());
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }

        }
//        throw new RuntimeException();
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录账号查询Admin对象
        // 创建条件对象
        AdminExample adminExample = new AdminExample();
        // 获取criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 设置查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if (admins == null || admins.isEmpty()) { // 没有查找到
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (admins.size() > 1) {
            throw  new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 2.判断Admin对象是否为null
        // 3.如果Admin对象为null则抛出异常
        Admin admin = admins.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();
        // 5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);
        // 6.对密码进行比较
        // 7.如果比较结果是不一致则抛出异常
        if (!Objects.equals(userPswdDB, userPswdForm)) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        Admin admin = list.get(0);
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用PageHelper的静态方法开启分页功能
        // PageHelper非侵入式设计
        PageHelper.startPage(pageNum, pageSize);
        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
        // 3.封装到PageInfo对象中
        return new PageInfo<>(list);
    }
}
