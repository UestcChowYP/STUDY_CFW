package com.uestc.crowd.mvc.config;

import com.google.gson.Gson;
import com.uestc.crowd.exception.LoginAcctAlreadyInUseException;
import com.uestc.crowd.exception.LoginAcctAlreadyInUseUpdateException;
import com.uestc.crowd.exception.LoginFailedException;
import com.uestc.crowd.util.CrowdConstant;
import com.uestc.crowd.util.CrowdUtil;
import com.uestc.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ControllerAdvice基于注解的异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {
    @ExceptionHandler(value = LoginAcctAlreadyInUseUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseUpdateException(LoginAcctAlreadyInUseException exception, HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception, HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(NullPointerException exception, HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveArithmeticException(NullPointerException exception, HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName, exception, request, response);
    }


    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }
    private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        // 请求对象
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        if (judgeResult) {
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // Gson对象处理json
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            response.getWriter().write(json);
            // response 响应不用返回modelAndView了
            return null;
        }
        // 不是ajax请求 返回modelAndView
        ModelAndView modelAndView = new ModelAndView();
        // 存入对象
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 设置视图
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
