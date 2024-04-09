package icu.callay.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.configure
 * &#064;className:      AliPayConfig
 * &#064;author:     Callay
 * &#064;description:  获取阿里pay信息
 * &#064;date:    2024/4/9 11:47
 * &#064;version:    1.0
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig {

    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;


}
