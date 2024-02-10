package icu.callay.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * (RegularUser)表实体类
 *
 * @author Callay
 * @since 2024-02-07 19:45:58
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("regular_user")
public class RegularUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String address;

    private String phone;

    private Double money;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

}

