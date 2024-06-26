package icu.callay.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderFormPageVo {
    private List<OrderFormVo> orderFormVoList;
    private Long total;
}
