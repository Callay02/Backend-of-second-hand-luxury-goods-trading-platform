package icu.callay.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (Goods)表实体类
 *
 * @author makejava
 * @since 2024-02-01 16:05:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods implements Serializable {

    private Integer id;

    private Integer type;

    private Integer brand;

    private String info;

    private Double price;

    private String img;

    private Integer fineness;
//来自用户
    private Long userId;

    private Date addTime;

    private Integer state;

}

