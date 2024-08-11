package com.uestc.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.uestc.crowd.entity.Admin;
import com.uestc.crowd.service.api.AdminService;
import com.uestc.crowd.util.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    @RequestMapping("admin/update.html")
    public String update(@RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword,
                         Admin admin
                         ) {
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }
    @RequestMapping("admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             ModelMap modelMap
                             ) {
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_CUR_ADMIN, admin);
        return "admin-edit";
    }

    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?page=" + Integer.MAX_VALUE;
    }
    @RequestMapping("/admin/remove/{id}/{pageNum}/{keyword}.html")
    public String removeAdmin(@PathVariable("id") Integer id,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable("keyword") String keyword
                              ) {
        // 删除
        adminService.removeById(id);
        // 页面跳转
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              ModelMap modelMap
                              ) {
        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam(value = "loginAcct") String loginAcct,
            @RequestParam(value = "userPswd") String userPswd,
            HttpSession session
    ) {
        // 调用Service方法执行登录检查
        // 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        // 将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }
}
