package icu.callay.vo;

import icu.callay.entity.GoodsType;
import lombok.Data;

import java.util.List;

@Data
public class GoodsTypePageVo {
    private List<GoodsType> goodsTypeList;
    private Long total;
}
