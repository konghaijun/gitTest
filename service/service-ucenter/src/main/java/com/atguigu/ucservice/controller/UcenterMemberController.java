package com.atguigu.ucservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.ucservice.entity.vo.LoginVo;
import com.atguigu.ucservice.entity.vo.RegisterVo;
import com.atguigu.ucservice.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-17
 */
@RestController
@RequestMapping("/ucenterservice/member")
public class UcenterMemberController {


    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value ="会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token=memberService.login(loginVo);
        return  R.ok().data("token", token);
    }


    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public  R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }


    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            LoginVo loginInfoVo = memberService.getLoginInfo(memberId);
            return R.ok().data("item", loginInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"error");

        }
    }




}

