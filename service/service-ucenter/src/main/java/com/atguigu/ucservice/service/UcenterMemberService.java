package com.atguigu.ucservice.service;

import com.atguigu.ucservice.entity.UcenterMember;
import com.atguigu.ucservice.entity.vo.LoginVo;
import com.atguigu.ucservice.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-17
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

  void register(RegisterVo registerVo);

    LoginVo getLoginInfo(String memberId);

}
