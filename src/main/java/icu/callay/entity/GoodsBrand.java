package icu.callay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (GoodsBrand)表实体类
 *
 * @author makejava
 * @since 2024-01-30 19:07:20
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_brand")
public class GoodsBrand implements Serializable {

    private Integer id;

    private String name;

}

