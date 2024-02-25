package icu.callay.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderFormPageVo {

    private List<PurchaseOrderFormVo> purchaseOrderFormVoList;
    private Long total;
}
