package icu.callay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (GoodsBrand)表实体类
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_brand")
public class GoodsBrand implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    private Integer id;

    private String name;

}

