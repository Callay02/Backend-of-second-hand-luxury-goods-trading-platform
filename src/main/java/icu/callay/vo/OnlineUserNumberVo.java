package icu.callay.vo;

import lombok.Data;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.vo
 * &#064;className:      OnlineUserCountVo
 * &#064;author:     Callay
 * &#064;description:  在线用户数量统计
 * &#064;date:    2024/4/26 22:07
 * &#064;version:    1.0
 */
@Data
public class OnlineUserNumberVo {

    public OnlineUserNumberVo(){
        regularUser=0;
        salesperson=0;
        appraiser=0;
        admin=0;
    }

    private Integer regularUser;
    private Integer salesperson;
    private Integer appraiser;
    private Integer admin;
}
