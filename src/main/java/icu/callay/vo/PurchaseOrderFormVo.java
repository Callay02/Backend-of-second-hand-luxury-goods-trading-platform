package icu.callay.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PurchaseOrderFormVo {

    private String id;

    private Long uid;

    private String logisticsNumber;

    private String info;

    private String typeName;

    private String brandName;

    private String apid;

    private String address;

    private Double acquisitionPrice;

    private Double sellingPrice;

    private String stateName;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer fineness;

    private String img;
}
