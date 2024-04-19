package icu.callay.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (RentalGoods)表实体类
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rental_goods")
public class RentalGoods implements Serializable {

    private String id;

    private Integer type;

    private Integer brand;

    private String info;

    //商品原价+保证金500
    private Double deposit;

    private Double rent;

    private String img;

    private Integer fineness;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date addTime;

    private Integer state;

    private String apid;

    private String antiCounterfeitingCode;

    private String uid;

}

