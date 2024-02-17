package icu.callay.entity;

import cn.dev33.satoken.util.SaResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (GoodsType)表实体类
 *
 * @author Callay
 * @since 2024-01-30 19:07:47
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_type")
public class GoodsType implements Serializable {

    private Integer type;

    private String name;

}

