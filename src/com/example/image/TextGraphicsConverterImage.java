package com.example.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImage implements TextGraphicsConverter {
    private double maxRatio;
    private int maxHeight;
    private int maxWidth;
    private char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};
    private TextColorSchema schema = new TextColorSchemaImage(symbols);

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        double ratio = (double) img.getWidth() / img.getHeight();

        if (maxRatio != 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth;
        int newHeight;

        if ((maxWidth == 0 || img.getWidth() <= maxWidth) && (maxHeight == 0 || img.getHeight() <= maxHeight)) {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        } else if ((maxWidth == 0 || img.getWidth() <= maxWidth) && (maxHeight != 0 && img.getHeight() > maxHeight)) {
            newHeight = maxHeight;
            newWidth = (img.getWidth() * maxHeight / img.getHeight());
        } else if ((maxWidth != 0 && img.getWidth() > maxWidth) && (maxHeight == 0 || img.getHeight() <= maxHeight)) {
            newWidth = maxWidth;
            newHeight = (img.getHeight() * maxWidth / img.getWidth());
        } else {
            if ((double) img.getHeight() / maxHeight > (double) img.getWidth() / maxWidth) {
                newHeight = maxHeight;
                newWidth = (img.getWidth() * maxHeight / img.getHeight());
            } else {
                newWidth = maxWidth;
                newHeight = (img.getHeight() * maxWidth / img.getWidth());
            }
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        ImageIO.write(bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();


        StringBuilder strImage = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                strImage.append(c);
                strImage.append(c);
            }
            strImage.append("\n");
        }

        return strImage.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;

    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;

    }
}
