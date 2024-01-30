package icu.callay.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (Goods)表实体类
 *
 * @author makejava
 * @since 2024-01-30 19:05:04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods implements Serializable {

    private Integer id;

    private Integer type;

    private Integer brand;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;

    private String from;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date addTime;

    private Integer state;

}

