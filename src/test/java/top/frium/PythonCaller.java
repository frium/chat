package top.frium;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TemplateMatching {

    static {
        // 加载 OpenCV 库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static int calculateOffset(String notchImagePath, String fullImagePath) {
        // 1. 读取图片
        Mat notchImage = Imgcodecs.imread(notchImagePath, Imgcodecs.IMREAD_GRAYSCALE);
        Mat fullImage = Imgcodecs.imread(fullImagePath, Imgcodecs.IMREAD_GRAYSCALE);

        if (notchImage.empty() || fullImage.empty()) {
            throw new IllegalArgumentException("图片加载失败，请检查路径！");
        }

        // 2. 边缘检测
        Mat notchEdges = new Mat();
        Mat fullEdges = new Mat();
        Imgproc.Canny(notchImage, notchEdges, 100, 200);
        Imgproc.Canny(fullImage, fullEdges, 100, 200);

        // 3. 模板匹配
        Mat result = new Mat();
        Imgproc.matchTemplate(fullEdges, notchEdges, result, Imgproc.TM_CCOEFF_NORMED);

        // 4. 获取最大值位置
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point maxLoc = mmr.maxLoc;

        // 5. 计算偏移量
        return (int) maxLoc.x;
    }

    public static void main(String[] args) {
        String notchImagePath = "D:/t5.png";  // 缺口图路径
        String fullImagePath = "D:/t6.png";  // 原图路径

        int offset = calculateOffset(notchImagePath, fullImagePath);
        System.out.println("需要移动的距离: " + offset + " 像素");
    }
}
