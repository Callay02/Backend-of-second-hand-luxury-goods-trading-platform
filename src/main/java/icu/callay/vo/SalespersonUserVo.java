package icu.callay.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SalespersonUserVo {

    private Long id;

    private String name;

    private String password;

    private String email;

    private String idCard;

    private String realName;

    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String phone;

    private Double money;

    private Date updateTime;
}
