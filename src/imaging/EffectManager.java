package imaging;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class EffectManager {

    private String[] effects;
    private BufferedImage currImage;
    private BufferedImage origImage;
    private int width;
    private int height;

    public EffectManager(BufferedImage img) {
        effects = new String[] { "---", "Vertical Flip", "Horizontal Flip", "Invert", "Negative", "Darken", "Lighten",
                "8-Bit" };
        currImage = img;
        origImage = img;
        width = img.getWidth();
        height = img.getHeight();
    }

    public String[] getEffects() {
        return effects;
    }

    public BufferedImage getImage() {
        return currImage;
    }

    public BufferedImage buildImage(String s) {
        switch (s) {
        case "Vertical Flip":
            currImage = verticalFlip();
        break;
        case "Horizontal Flip":
            currImage = horizontalFlip();
        break;
        case "Invert":
            currImage = invert();
        break;
        case "Negative":
            currImage = negative();
        break;
        case "Darken":
            currImage = darken();
        break;
        case "Lighten":
            currImage = lighten();
        break;
        case "8-Bit":
            currImage = eBit();
        }

        return currImage;

    }

    private BufferedImage eBit() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        int pixelWidth = 20;
        int pixelArea = pixelWidth * pixelWidth;
        for (int x = 0; x < width / pixelWidth; x++) {
            for (int y = 0; y < height / pixelWidth; y++) {
                int rTotal = 0;
                int gTotal = 0;
                int bTotal = 0;
                for (int dx = 0; dx < pixelWidth; dx++) {
                    for (int dy = 0; dy < pixelWidth; dy++) {
                        Color col = new Color(currImage.getRGB(x * pixelWidth + dx, y * pixelWidth + dy), true);
                        rTotal += col.getRed();
                        gTotal += col.getGreen();
                        bTotal += col.getBlue();
                    }
                }

                Color col = new Color(rTotal / pixelArea, gTotal / pixelArea, bTotal / pixelArea);
                for (int dx = 0; dx < pixelWidth; dx++) {
                    for (int dy = 0; dy < pixelWidth; dy++) {
                        result.setRGB(x * pixelWidth + dx, y * pixelWidth + dy, col.getRGB());
                    }
                }

            }
        }

        // width border calculations
        int rTotal = 0;
        int gTotal = 0;
        int bTotal = 0;
        int totalArea = 0;
        int widthBorder = width % pixelWidth;
        int heightBorder = height % pixelWidth;
        if (widthBorder > 0) {

            for (int dx = 0; dx < widthBorder; dx++) {
                for (int dy = 0; dy < heightBorder; dy++) {
                    Color col = new Color(currImage.getRGB(width - widthBorder + dx, height - heightBorder + dy), true);
                    rTotal += col.getRed();
                    gTotal += col.getGreen();
                    bTotal += col.getBlue();
                    totalArea++;
                }
            }

            Color col = new Color(rTotal / totalArea, gTotal / totalArea, bTotal / totalArea);
            for (int dx = 0; dx < widthBorder; dx++) {
                for (int dy = 0; dy < heightBorder; dy++) {
                    result.setRGB(width - widthBorder + dx, height - heightBorder + dy, col.getRGB());
                }
            }
        }
        return result;
    }

    private BufferedImage lighten() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        double tintFactor = 0.25;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col = new Color(currImage.getRGB(x, y), true);
                col = new Color(col.getRed() + (int) ((255 - col.getRed()) * tintFactor),
                        col.getGreen() + (int) ((255 - col.getGreen()) * tintFactor),
                        col.getBlue() + (int) ((255 - col.getBlue()) * tintFactor));
                result.setRGB(x, y, col.getRGB());
            }
        }
        return result;
    }

    private BufferedImage darken() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        double shadeFactor = 1 - 0.25;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col = new Color(currImage.getRGB(x, y), true);
                col = new Color((int) (col.getRed() * shadeFactor), (int) (col.getGreen() * shadeFactor),
                        (int) (col.getBlue() * shadeFactor));
                result.setRGB(x, y, col.getRGB());
            }
        }
        return result;
    }

    private BufferedImage negative() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col = new Color(currImage.getRGB(x, y), true);
                col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
                result.setRGB(x, y, col.getRGB());
            }
        }
        return result;
    }

    private BufferedImage invert() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, currImage.getRGB(width - x - 1, height - y - 1));
            }
        }
        return result;
    }

    private BufferedImage horizontalFlip() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, currImage.getRGB(width - x - 1, y));
            }
        }
        return result;
    }

    private BufferedImage verticalFlip() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, currImage.getRGB(x, height - y - 1));
            }
        }
        return result;
    }

    public BufferedImage reset() {
        // TODO Auto-generated method stub
        currImage = origImage;

        return origImage;
    }

}
