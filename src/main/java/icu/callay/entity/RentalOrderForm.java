package icu.callay.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (RentalOrderForm)表实体类
 *
 * @author Callay
 * @since 2024-03-26 18:09:36
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rental_order_form")
public class RentalOrderForm implements Serializable {

    private String id;

    private String uid;

    private String gid;

    private Integer logisticsNumber;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private String address;

}
