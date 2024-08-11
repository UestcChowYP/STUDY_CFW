package com.uestc.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.uestc.crowd.entity.Role;
import com.uestc.crowd.service.api.RoleService;
import com.uestc.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleHandler {
    @Autowired
    private RoleService roleService;
    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){
        System.out.println("-------------");
        System.out.println(roleIdList.toString());
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role) {
        System.out.println(role.toString());
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> savaRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/role/get/page/info.json", produces = {"application/json;charset=UTF-8"})
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ){
        // 调用service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
        return ResultEntity.successWithData(pageInfo);
    }

}
