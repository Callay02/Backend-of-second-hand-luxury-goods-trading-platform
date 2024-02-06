package icu.callay.vo;

import lombok.Data;

import java.util.List;

@Data
public class GoodsPageVo {
    private List<GoodsVo> goodsVoList;
    private Long total;
}
