package icu.callay.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (PurchaseOrderForm)表实体类
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_order_form")
public class PurchaseOrderForm implements Serializable {

    @TableId(type = IdType.AUTO)
    private String id;

    private Long uid;

    private String logisticsNumber;

    private String info;

    private Integer type;

    private Integer brand;

    private Double price;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private Integer fineness;

}

