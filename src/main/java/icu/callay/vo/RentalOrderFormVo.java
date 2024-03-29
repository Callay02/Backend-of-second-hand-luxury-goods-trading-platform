package icu.callay.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RentalOrderFormVo {
    private String id;

    private String uid;

    private String name;

    private String gid;

    private Integer logisticsNumber;

    private Integer state;

    private String typeName;

    private String brandName;

    private String info;

    private Double deposit;

    private Double rent;

    private String img;

    private Integer fineness;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

}
