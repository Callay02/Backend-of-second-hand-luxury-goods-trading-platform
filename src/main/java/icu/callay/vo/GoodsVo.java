package icu.callay.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GoodsVo {
    private Integer id;

    private String typeName;

    private String brandName;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;

    private Date addTime;

    private Integer state;
}
