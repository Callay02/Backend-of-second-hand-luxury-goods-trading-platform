package icu.callay.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (Goods)表实体类
 *
 * @author Callay
 * @since 2024-02-01 16:05:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer type;

    private Integer brand;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;
    //来自用户
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date addTime;

    private Integer state;

    private String apid;

    private String antiCounterfeitingCode;

}

