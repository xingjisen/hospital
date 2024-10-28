package com.yygh.oss.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.oss.config.OssConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.oss.utils
 * @Description
 * @date 2024-10-26 14:39
 */
public class OssUtil {

    private static final COSCredentials CRED;
    private static final ClientConfig CLIENT_CONFIG;
    private static final COSClient COS_CLIENT;
    private static final String BUCKET_NAME;

    static {
        // 初始化用户身份信息(secretId, secretKey)
        CRED = new BasicCOSCredentials(OssConfig.SECRET_ID, OssConfig.SECRET_KEY);
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        CLIENT_CONFIG = new ClientConfig(new Region(OssConfig.REGION_NAME));
        // 生成cos客户端
        COS_CLIENT = new COSClient(CRED, CLIENT_CONFIG);
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        BUCKET_NAME = OssConfig.BUCKET_NAME;
    }

    /**
     * 上传
     *
     * @param key       上传键名
     * @param localPath 上传文件路径
     * @return
     */
    public static String put(String key, File localPath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localPath);
        PutObjectResult putObjectResult = COS_CLIENT.putObject(putObjectRequest);
        System.out.println(putObjectResult.getRequestId());
        return putObjectResult.getRequestId();
    }

    /**
     * 下载
     *
     * @param key           获取的键名
     * @param bandwidthInMB 限速
     */
    public static void get(String key, int bandwidthInMB) {
        // 方法1 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, key);
        // 限流使用的单位是 bit/s, 这里设置下载带宽限制为10MB/s
        getObjectRequest.setTrafficLimit(bandwidthInMB * 1024 * 1024 * 8);
        COSObject cosObject = COS_CLIENT.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
        // 下载对象的 CRC64
        String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();
        // 关闭输入流
        try {
            cosObjectInput.close();
        } catch (IOException e) {
            throw new YyghException("输入流关闭失败", ResultCodeEnum.CUSTOM.getCode());
        }
        // 方法2 下载文件到本地
        String outputFilePath = "exampleobject";
        File downFile = new File(outputFilePath);
        getObjectRequest = new GetObjectRequest(BUCKET_NAME, key);
        ObjectMetadata downObjectMeta = COS_CLIENT.getObject(getObjectRequest, downFile);
    }

    /**
     * 删除
     *
     * @param key 需要删除的键名
     * @return
     */
    public static boolean delete(String key) {
        COS_CLIENT.deleteObject(BUCKET_NAME, key);
        return true;
    }
}
