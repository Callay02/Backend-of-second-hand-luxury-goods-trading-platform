package icu.callay.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OrderFormVo {

    //订单号
    private Long id;

    private String logisticsNumber;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    //用户信息
    private Long uid;

    private String name;

    private String address;

    private String phone;

    //商品信息相关
    private Long gid;

    private String typeName;

    private String brandName;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;

    private String courierCode;

}
