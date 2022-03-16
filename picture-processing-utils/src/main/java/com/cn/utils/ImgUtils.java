package com.cn.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImgUtils {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static double[] a_list = new double[]{
            1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 31, 33, 35, 37, 39,
            41, 43, 44, 46, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 66, 67, 69, 71, 73, 74,
            76, 78, 79, 81, 83, 84, 86, 87, 89, 91, 92, 94, 95, 97, 99, 100, 102, 103, 105,
            106, 108, 109, 111, 112, 114, 115, 117, 118, 120, 121, 123, 124, 126, 127, 128,
            130, 131, 133, 134, 135, 137, 138, 139, 141, 142, 143, 145, 146, 147, 149, 150,
            151, 153, 154, 155, 156, 158, 159, 160, 161, 162, 164, 165, 166, 167, 168, 170,
            171, 172, 173, 174, 175, 176, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187,
            188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203,
            204, 205, 205, 206, 207, 208, 209, 210, 211, 211, 212, 213, 214, 215, 215, 216,
            217, 218, 219, 219, 220, 221, 222, 222, 223, 224, 224, 225, 226, 226, 227, 228,
            228, 229, 230, 230, 231, 232, 232, 233, 233, 234, 235, 235, 236, 236, 237, 237,
            238, 238, 239, 239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 244, 245,
            245, 246, 246, 246, 247, 247, 248, 248, 248, 249, 249, 249, 250, 250, 250, 250,
            251, 251, 251, 251, 252, 252, 252, 252, 253, 253, 253, 253, 253, 254, 254, 254,
            254, 254, 254, 254, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 256};

    private static final int d = 18;
    private static final double SIGMA_COLOR = d * 2;
    private static final double SIGMA_SPACE = d / 2D;

    public static void beauty(String imgPath) {
        Mat src = Imgcodecs.imread(imgPath);
        Size size = src.size();
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                double[] doubles = src.get(i, j);
                int b = (int) doubles[0];
                int g = (int) doubles[1];
                int r = (int) doubles[2];
                src.put(i, j, a_list[b], a_list[g], a_list[r]);
            }
        }
        Mat g1 = new Mat();
        Imgproc.GaussianBlur(src, g1, new Size(9, 9), 0, 0);
        Mat b1 = new Mat();
        Imgproc.bilateralFilter(g1, b1, d, SIGMA_COLOR, SIGMA_SPACE);
        Mat g2 = new Mat();
        Imgproc.GaussianBlur(b1, g2, new Size(0, 0), 3);
        Mat finalM = new Mat();
        Core.addWeighted(b1, 1.5, g2, -0.5, 0, finalM);
        Imgcodecs.imwrite(imgPath, finalM);
    }

}
