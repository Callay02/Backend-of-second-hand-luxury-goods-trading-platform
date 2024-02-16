package icu.callay.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OrderFormVo {

    //订单号
    private Long id;

    //商品信息相关
    private Long gid;

    private String typeName;

    private String brandName;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;

    private Integer logisticsNumber;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
