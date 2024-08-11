package com.uestc.crowd.mvc.handler;

import com.uestc.crowd.util.CrowdUtil;
import com.uestc.crowd.util.ResultEntity;
import com.uestc.crowd.entity.Admin;
import com.uestc.crowd.entity.ParamArray;
import com.uestc.crowd.entity.Student;
import com.uestc.crowd.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;
    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request) {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
//        logger.debug("judgeResult=" + judgeResult);
        logger.info("info");
        System.out.println("judgeResult=" + judgeResult);
        List<Admin> admins = adminService.getAll();
        modelMap.addAttribute("adminList", admins);
//        String str = null;
//        System.out.println(str.length());
        System.out.println(10/0);
        return "target";
    }
    @ResponseBody
    @RequestMapping("/send/array.html")
    public String testReceiverArray1(@RequestParam("array[]") List<Integer> array) {
        for (Integer num: array) {
            System.out.println(num);
        }
        return "success";
    }
    @ResponseBody
    @RequestMapping("/send/array/two.html")
    public String testReceiverArray2(ParamArray array) {
        List<Integer> list = array.getArray();
        for (Integer num: list) {
            System.out.println(num);
        }
        System.out.println(array);
        return "success";
    }
    @ResponseBody
    @RequestMapping("send/array/three.html")
    public String testReceiverArray3(@RequestBody List<Integer> array) {
        for (Integer num: array) {
            System.out.println(num);
        }
        System.out.println(array);

        return "success";
    }
    @ResponseBody
    @RequestMapping("/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody  Student student) {
        System.out.println(student);
//        if (student != null) {
            return ResultEntity.successWithData(student);

//
    }
}
