package icu.callay.controller;

import cn.dev33.satoken.util.SaResult;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/oss")
public class OSSController {

    /**
     * @return SaResult
     * @author Callay
     * &#064;description 后端签名
     * &#064;2024/4/5 14:28
     */
    @RequestMapping("/policy")
    public SaResult policy(){
        String accessId ="LTAI5tDqLBMKXMws6CkuGZNh";
        String accessKey="8sxryFQXWDFSoseavjzIPxID1gjA6J";
        String endpoint="oss-cn-hangzhou.aliyuncs.com";
        String bucket="callay-spdb";
        String host="https://"+bucket+"."+endpoint;

        String dir="goods-img/"+ LocalDate.now()+"/";

        OSS ossClient = new OSSClientBuilder().build(endpoint,accessId,accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            //String accessId = credentialsProvider.getCredentials().getAccessKeyId();
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
            return SaResult.data(respMap).setMsg("获取成功");

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
