package icu.callay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (OrderFormState)表实体类
 *
 * @author Callay
 * @since 2024-02-08 23:02:33
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_form_state")
public class OrderFormState implements Serializable {

    private Integer id;

    private String state;

}

