package com.alageek.crowd.handler;

import com.alageek.crowd.api.MySQLRemoteService;
import com.alageek.crowd.api.RedisRemoteService;
import com.alageek.crowd.constant.CrowdConstant;
import com.alageek.crowd.entity.po.MemberPO;
import com.alageek.crowd.entity.vo.MemberLoginVO;
import com.alageek.crowd.entity.vo.MemberVO;
import com.alageek.crowd.util.CrowdUtil;
import com.alageek.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/member")
public class MemberHandler {

    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/do/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:"+ CrowdConstant.ATTR_NAME_ZUUL +"/member/auth/to/login/page";
    }

    @RequestMapping("/auth/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession httpSession
    ){
        MemberPO memberPO = new MemberPO();
        memberPO.setLoginacct(loginacct);
        ResultEntity<List<MemberPO>> result = mySQLRemoteService.getMemberSelectiveRemote(memberPO);
        if(ResultEntity.FAILED.equals(result.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_DATABASE_CAN_NOT_CONNECT);
            return "member-login";
        }
        List<MemberPO> list = result.getData();
        if(list==null || list.size()==0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberPO = list.get(0);
        if(!encoder.matches(userpswd, memberPO.getUserpswd())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());
        httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:"+ CrowdConstant.ATTR_NAME_ZUUL +"/member/auth/to/center/page";
    }

    @RequestMapping("/auth/do/register")
    public String register(MemberVO memberVO, ModelMap modelMap){
        String phone = memberVO.getPhone();
        String key = CrowdConstant.REDIS_CODE_PREFIX + phone;
        ResultEntity<String> result = redisRemoteService.getRedisValueByKeyRemote(key);
        if(ResultEntity.FAILED.equals(result.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_NOSQL_CAN_NOT_CONNECT);
            return "member-reg";
        }
        String code = result.getData();
        if(code!=null && code.equals(memberVO.getCode())){
            result = redisRemoteService.removeRedisKeyRemote(key);
            if(ResultEntity.FAILED.equals(result.getResult())){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_NOSQL_CAN_NOT_CONNECT);
                return "member-reg";
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            memberVO.setUserpswd(encoder.encode(memberVO.getUserpswd()));
            MemberPO memberPO = new MemberPO();
            BeanUtils.copyProperties(memberVO, memberPO);
            ResultEntity<List<MemberPO>> memberPOResultEntity = mySQLRemoteService.getMemberSelectiveRemote(memberPO);
            if(ResultEntity.FAILED.equals(memberPOResultEntity.getResult())){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_DATABASE_CAN_NOT_CONNECT);
                return "member-reg";
            }
            if(memberPOResultEntity.getData()==null || memberPOResultEntity.getData().size() == 0){
                result = mySQLRemoteService.saveMemberRemote(memberPO);
                if(ResultEntity.FAILED.equals(result.getResult())){
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_DATABASE_CAN_NOT_CONNECT);
                    return "member-reg";
                }
                return "redirect:"+ CrowdConstant.ATTR_NAME_ZUUL +"/member/auth/to/login/page";
            }
            else{
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGINACCT_DUPLICATE);
                return "member-reg";
            }
        }
        else{
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_VERIFICATION_CODE_IS_NOT_EQUAL);
            return "member-reg";
        }
    }

    @ResponseBody
    @RequestMapping("/auth/send/short/message.json")
    public ResultEntity<String> sendShortMessage(@RequestParam("phone") String phone){
        ResultEntity<String> result = CrowdUtil.sendCodeByShortMessage(phone);
        if(ResultEntity.FAILED.equals(result.getResult())){
            return result;
        }
        String code = result.getData();
        String key = CrowdConstant.REDIS_CODE_PREFIX + phone;
        result = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);
        if(ResultEntity.SUCCESS.equals(result.getResult())){
            return ResultEntity.successWithoutData();
        } else {
            return result;
        }
    }

}
