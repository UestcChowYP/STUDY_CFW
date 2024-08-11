package com.uestc.crowd.mvc.interceptor;

import com.uestc.crowd.entity.Admin;
import com.uestc.crowd.exception.AccessForbiddenException;
import com.uestc.crowd.util.CrowdConstant;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取session
        HttpSession session = request.getSession();
        // 2. 尝试获取admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // 3. 判断admin对象是否为null
        if (admin == null) {
            // 4.抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 5.如果Admin对象不为null，则返回true放行
        return true;
    }
}
