package icu.callay.vo;

import lombok.Data;

@Data
public class SearchGoodsVo {
    private String brand;
    private String type;
    private String info;
    private int page;
    private int rows;
    private String id;
}
