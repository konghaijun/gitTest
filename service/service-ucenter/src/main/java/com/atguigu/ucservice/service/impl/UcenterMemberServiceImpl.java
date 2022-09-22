package com.atguigu.ucservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.ucservice.entity.UcenterMember;
import com.atguigu.ucservice.entity.vo.LoginVo;
import com.atguigu.ucservice.entity.vo.RegisterVo;
import com.atguigu.ucservice.mapper.UcenterMemberMapper;
import com.atguigu.ucservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-17
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


          //会员登录
    @Override
    public String login(LoginVo loginVo) {
        String mobile =loginVo.getMobile();
        String password=loginVo.getPassword();

        //检验参数
        if(StringUtils.isEmpty(mobile)||
        StringUtils.isEmpty(password)) {throw  new GuliException(20001,"error");

        }

        //获取会员
        UcenterMember member=baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile",mobile));
          if(null==member){
              throw  new GuliException(20001,"error");
          }

          //检验密码
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(20001,"error");

        }

        //检验是否被禁用
        if(member.getIsDisabled()){
            throw new GuliException(20001,"error");
        }


        String token= JwtUtils.getJwtToken(member.getId(),member.getNickname());
        return  token;
    }





    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息进行校验
        String nickname=registerVo.getNickname();
        String mobile=registerVo.getMobilel();
        String password=registerVo.getPassword();
        String code = registerVo.getCode();

        //检验参数
        if(StringUtils.isEmpty(mobile)||
        StringUtils.isEmpty(password)||
                StringUtils.isEmpty(code))
        {throw new GuliException(20001,"error");}


        //校验校验证码
        //从redis获取发送的验证码
        String moblieCode=redisTemplate.opsForValue().get(mobile);
        if(!code.equals(moblieCode)){
            throw new GuliException(20001,"error");
        }

        //查询数据库中是否存在相同的手机号码
        Integer count=baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile",mobile));
        if(count.intValue()>0)
            throw new GuliException(20001,"error");

        //添加注册信息到数据库
        UcenterMember member=new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobilel());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("Http");
        this.save(member);
    }



    @Override
    public LoginVo getLoginInfo(String memberId) {
        UcenterMember member = baseMapper.selectById(memberId);
        LoginVo loginInfoVo = new LoginVo();
        BeanUtils.copyProperties(member, loginInfoVo);
        return loginInfoVo;
    }





}
