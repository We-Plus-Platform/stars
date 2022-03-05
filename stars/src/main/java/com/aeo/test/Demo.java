package com.aeo.test;
// 根据 github 提供的 maven 集成方法导入 java sts sdk
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import org.json.JSONObject;
import com.tencent.cloud.CosStsClient;

public class Demo {
    public static void main(String[] args) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            // 云 API 密钥 secretId
            config.put("secretId", "AKIDMr9U5TgxMZZJo3i0gKp6HDtKRRDuJgYI");
            // 云 API 密钥 secretKey
            config.put("secretKey", "knkRu8E8xYzA5kALJ2W0HJ5oIWTVP4Vg");
            //若需要设置网络代理，则可以如下设置
//            if (properties.containsKey("https.proxyHost")) {
//                System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
//                System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
//            }

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成你的 bucket
            config.put("bucket", "cquptcywx-1300292366");
            // 换成 bucket 所在地区
            config.put("region", "ap-chongqing");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，
            // 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefix", "exampleobject");

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传
                    "name/cos:PostObject",
                    // 分片上传： 初始化分片
                    "name/cos:InitiateMultipartUpload",
                    // 分片上传： 查询 bucket 中未完成分片上传的UploadId
                    "name/cos:ListMultipartUploads",
                    // 分片上传： 查询已上传的分片
                    "name/cos:ListParts",
                    // 分片上传： 上传分片块
                    "name/cos:UploadPart",
                    // 分片上传： 完成分片上传
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            // 请求临时密钥信息
            JSONObject credential = CosStsClient.getCredential(config);
            // 请求成功：打印对应的临时密钥信息
            System.out.println(credential.toString(4));
        } catch (Exception e) {
            // 请求失败，抛出异常
//            throw new IllegalArgumentException("no valid secret !");
            e.printStackTrace();
        }

    }
}
