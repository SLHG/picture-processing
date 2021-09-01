package com.cn.web;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;

/**
 * 加载模块对象
 *
 * @author 小帅丶
 */
public class BDFactory {
    private static AipBodyAnalysis aipBodyAnalysis;

    //设置APPID/AK/SK
    public static final String APP_ID = "24780118";
    public static final String API_KEY = "dKWDpTchDBLyUxlbT0LtbsX5";
    public static final String SECRET_KEY = "CvYFCGANIVIt987jGrE0AnvzmnWEBopT";

    public static AipBodyAnalysis getAipBodyAnalysis() {
        if (aipBodyAnalysis == null) {
            synchronized (AipBodyAnalysis.class) {
                if (aipBodyAnalysis == null) {
                    aipBodyAnalysis = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
                }
            }
        }
        return aipBodyAnalysis;
    }
}
