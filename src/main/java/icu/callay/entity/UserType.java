package icu.callay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (UserType)表实体类
 *
 * @author Callay
 * @since 2024-04-19 09:40:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_type")
public class UserType implements Serializable {

    private Integer type;

    private String name;

}

