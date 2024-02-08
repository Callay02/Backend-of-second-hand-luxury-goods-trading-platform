package icu.callay.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (OrderForm)表实体类
 *
 * @author Callay
 * @since 2024-02-08 22:59:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_form")
public class OrderForm implements Serializable {

    private Long id;

    private Long uid;

    private Integer gid;

    private Integer logisticsNumber;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

}

