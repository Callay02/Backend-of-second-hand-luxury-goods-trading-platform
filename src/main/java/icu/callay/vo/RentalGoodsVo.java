package icu.callay.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RentalGoodsVo {
    private String id;

    private String typeName;

    private String brandName;

    private String info;

    private Double deposit;

    private Double rent;

    private String img;

    private Integer fineness;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date addTime;

    private Integer state;
}
