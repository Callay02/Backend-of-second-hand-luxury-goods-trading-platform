package icu.callay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OrderFormPageVo {
    private List<OrderFormVo> orderFormVoList;
    private Long total;
}
