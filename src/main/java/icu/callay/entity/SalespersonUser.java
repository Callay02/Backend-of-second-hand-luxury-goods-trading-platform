package icu.callay.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (SalespersonUser)表实体类
 *
 * @author Callay
 * @since 2024-03-13 15:35:26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("salesperson_user")
public class SalespersonUser implements Serializable {

    private String id;

    private String phone;

    private Double money;

    private Date updateTime;

}

