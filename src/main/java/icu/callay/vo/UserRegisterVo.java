package icu.callay.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * &#064;projectName:  springboot
 * &#064;package:  icu.callay.vo
 * &#064;className:  UserRegisterVo
 * &#064;author:  Callay
 * &#064;description:  用户注册接收类
 * &#064;date:  2024/4/5 13:32
 * &#064;version:  1.0
 */
@Data
public class UserRegisterVo {
    private Long id;

    private String name;

    private String password;

    private String email;

    private String idCard;

    private String realName;

    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer isDeleted;

    private String verificationCode;
}
