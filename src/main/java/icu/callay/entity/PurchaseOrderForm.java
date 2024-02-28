package icu.callay.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String apid;

    private Double acquisitionPrice;

    private Double sellingPrice;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer fineness;

    private String img;

}

