package icu.callay.vo;

import icu.callay.entity.GoodsBrand;
import lombok.Data;

import java.util.List;

@Data
public class GoodsBrandPageVo {

    private List<GoodsBrand> goodsBrandList;
    private Long total;
}
