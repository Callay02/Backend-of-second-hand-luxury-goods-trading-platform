package icu.callay.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (PlatformRevenueFlowForm)表实体类
 *
 * @author Callay
 * @since 2024-04-10 14:33:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("platform_revenue_flow_form")
public class PlatformRevenueFlowForm implements Serializable {

    private Long id;

    private String userId;

    private String subject;

    private String tradeNo;

    private String outTradeNo;

    private String totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String source;
}

