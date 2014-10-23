package com.yshow.shike.utils;

import java.util.ArrayList;

public class PartnerConfig {
    /**
     * *  提示：如何获取安全校验码和合作身份者id
     * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
     * 2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
     * 3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
     */
    // 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
    public static final String DEFAULT_PARTNER = "2088011335000645";
    // 商户收款的支付宝账号
    public static final String DEFAULT_SELLER = "shike303@163.com";
    //商户私钥，自助生成
    public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJiaRIGv4I1h5NAQatl8fJh2iQ8WTLrXao/1NfgmHgcjiX2qU1OaBamkA5OLTk+pm0QGkA27MrquoM2On/SQZbhkPRBKYDnUlf8BulOgbzaqhviw5PlGQ/Y8lU7/Lp4OnjKwFvGxq2+xMXUgfnTzSCQNgHzIc0A+MesfRS7C1f03AgMBAAECgYAqcGyw7Gq0pw7tVkOA7H/yHrULPxZjt9jo5Db4JhGfxqBMFHxL+b+tZ/GerNVAjlih2HljeDYpeLs7r3iImUfQTA+4hC8VKcaf6kikL7F2YvXuFW/jhVCvrzC+L20DwgNakVxRUD2K0CLqGcKdwtvz2uAu4rs5RB5l78o8csRd0QJBAMcSJxk3BtE65QktPwabYEdh6ScOGxFs893EZSsTQfANNP2LIjb0Tzckan8vaa0hxt2CsOyzjojrFTlSpRFdGoUCQQDEPjOqISyYnDjxfgRGXzMOVNR/nUQ3+7YL/uFXq/WgVacxZV8axSE9YGoavUEwfsI4yJgLXN4S/tXsdldIV2uLAkAf+3GeonGM8dpUJBnJFPNd5IQRyzlcDlYLnf7m8bwZNfX6efzwOUX0xPv7HQHsV83cTp/gF2Th6GrLf9SXOo8FAkEAsvEBDT3ou1OgPNwqq7x9ArFpft/5Z473ReLouZfMhqHzrYABA5kDIUM8HRu7SKwdD8ghlCLfQLTsfgy+s/E61wJAI5NHaO/ylOIUfeBvCS+qDpHggy+ms94C6MOA7Iml5eFxi20bqv9ugFSITbqPu4bXFD8V07FD2OyIagukKbNyaQ==";
    //公钥
    public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    // 老师消息引导参数
    public static final String TEA_YD_TOOL = "TEA_YD_TOOL";
    // 老师工具引导参数
    public static final String TEA_TOOL = "TEA_TOOL";
    //邮件 短信分享内容
    public static String CONTEBT = "回家作业不会做怎么办？“师课”来帮你，在职老师一呼即应，做作业容易多了！手机下载地址：www.shikeke.com/ipa";
    //记录老师是否是被关注过
    public static ArrayList<String> list = new ArrayList<String>();
    //用户自动提问没有注册显示的内容
    public static String AUTO_CONTEBT = "该用户需要完成注册后才能使用，完成注册后赠送300学分,是否去完成注册？";
    //学生立即提问 页面进行 与老师交互
    public static String TEATHER_ID = null;
    public static String TEATHER_NAME = null;
    public static String SUBJECT_ID = null;
    public static String SUBJECT_NAME = null;
    public static String TEACHER_IMG = null;
    //存储是删除的图片。indexs下标为0存储的是arg0。
    public static ArrayList<String> three_indexs = new ArrayList<String>();
    //点击第二个界面时的下标
    public static String two_index;
    //题库以及界面
    public static int clickPosition = -1;
    //学生端删除题库2
    public static Boolean start_two = false;
}