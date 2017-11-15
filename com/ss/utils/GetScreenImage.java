package com.ss.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GetScreenImage {

    /**
     * 截屏
     * @throws IOException  io 异常
     * @throws AWTException 图形异常
     */
    public static void getScreen() throws AWTException, IOException {

        //此方法仅适用于JdK1.6及以上版本
        Robot robot = new Robot();
        robot.delay(10000);
        Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();
        //最大化浏览器
        robot.keyRelease(KeyEvent.VK_F11);
        robot.delay(1);
        Image image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        //保存图片
        ImageIO.write(bi, "jpg", new File("D:\\screen.jpg"));


    }

    public static void main(String agrs[]) {
        try {
            getScreen();
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }

    }
}
