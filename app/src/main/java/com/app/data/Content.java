package com.app.data;


/**
 * Created by ningd on 2015/12/1.
 */
public interface Content {
//    订阅所有
    String SY = "fes/M_/#";

//    控制开关
    String KZ = "fes/C^/Sck-10-{0}/{1}";

//    订阅的内容
    String DY = "fes/M_/Sck-10-{0}/{1}";

    //使用占位符拼接字符串
    String domain = "www.ykmaiz.com";

    int iVisit = 0;

    //获取地址信息   121.43.233.84
    String JGCX = "http://web.beepower.com.cn/guangzhou/query/jgcx/jgcx!getOrgList.do";

    //上传 MAC信息
    String SJSC = "http://web.beepower.com.cn/guangzhou/query/jgcx/mac!record.do";

    //拆回插座信息
    String dropDa = "http://web.beepower.com.cn/guangzhou/query/jgcx/mac!dropDa.do";

    //碳晶
    String TJ = "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!recrod_TJ_Files.do";

    //网关
    String WG = "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!recrod_WG_Files.do";

    //网关
    String PDX = "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!recrod_PDX_Files.do";

    //网关
    String CK = "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!recrod_CK_Files.do";
    //网关
    String HW = "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!record_HW_Files.do";
//    String HW = "http://192.168.1.124:8080/web/mobilePortal/files/recordFiles!record_HW_Files.do";
    //网关
    String KT= "http://web.beepower.com.cn/guangzhou/mobilePortal/files/recordFiles!record_KT_Files.do";
//    String KT = "http://192.168.1.124:8080/web/mobilePortal/files/recordFiles!record_KT_Files.do";

}
