package icu.callay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (PurchaseOrderFormState)表实体类
 *
 * @author Callay
 * @since 2024-02-25 19:30:20
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_order_form_state")
public class PurchaseOrderFormState implements Serializable {

    private Integer id;

    private String state;

}

