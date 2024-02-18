package icu.callay;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;

@SpringBootTest
class SpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void upLoad() throws ClientException, FileNotFoundException {

        // Endpoint
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";


        // 用户的访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = "LTAI5tDqLBMKXMws6CkuGZNh";
        String accessKeySecret = "8sxryFQXWDFSoseavjzIPxID1gjA6J";
        // 使用代码嵌入的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);


        // 填写Bucket名称
        String bucketName = "callay-spdb";
        // 填写本地文件的完整路径
        String filePath= "C:\\Users\\Callay\\Pictures\\Camera Roll\\tx.jpg";
        // 填写Object完整路径
        String objectName = LocalDateTime.now()+"/"+"tx.jpg";


        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
