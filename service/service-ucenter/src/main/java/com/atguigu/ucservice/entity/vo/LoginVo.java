package com.atguigu.ucservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: 23091
 * @Date: 2022/9/17 10:35
 * @Description:
 */
@Data
@ApiModel(value="登陆对象",description = "登陆对象")
public class LoginVo {
    @ApiModelProperty(value = "手机号")
    private  String mobile;

    @ApiModelProperty(value = "密码")
   private  String password;



}
