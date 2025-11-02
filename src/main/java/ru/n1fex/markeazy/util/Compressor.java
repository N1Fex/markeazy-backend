package ru.n1fex.markeazy.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class Compressor {

    private static BufferedImage removeAlphaChannel(BufferedImage img) {
        if (!img.getColorModel().hasAlpha()) {
            return img;
        }

        BufferedImage target = createImage(img.getWidth(), img.getHeight(), false);
        Graphics2D g = target.createGraphics();
        // g.setColor(new Color(color, false));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return target;
    }
    private static BufferedImage createImage(int width, int height, boolean hasAlpha) {
        return new BufferedImage(width, height, hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    }

    public static byte[] getCompressedImageAsByteArray(String path) throws IOException {

        File input = new File(path);

        BufferedImage loadedImage = ImageIO.read(input);

        if (loadedImage == null) {
            return new byte[0];
        }

        BufferedImage noAlpha = removeAlphaChannel(loadedImage);
        BufferedImage image = normalizeScale(noAlpha);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String extension = input.getName().substring(input.getName().lastIndexOf(".") + 1);
        if (extension.equalsIgnoreCase("webp")) {
            return new byte[0];
        }
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
        //log.info(extension);
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        //param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        //param.setCompressionQuality(1f);  // Change the quality value you prefer
        writer.write(null, new IIOImage(image, null, null), param);

        ios.close();
        byteArrayOutputStream.close();
        writer.dispose();

        return byteArrayOutputStream.toByteArray();
    }

    private static BufferedImage normalizeScale(BufferedImage img) throws IOException {

        float scale = 1.0f;
        int max = Math.max(img.getWidth(), img.getHeight());
        if (max > 1000) {
            scale = max / 500.0f;

            int targetW = (int) (img.getWidth() / scale);
            int targetH = (int) (img.getHeight() / scale);;

            BufferedImage resizedImage = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(img, 0, 0, targetW, targetH, null);
            graphics2D.dispose();
            return resizedImage;
        }

        return img;
    }
}
