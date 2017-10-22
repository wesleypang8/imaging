package imaging;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.awt.image.*;

public class EffectManager {

    private String[] effects;
    private BufferedImage currImage;
    private BufferedImage prevImage;
    private BufferedImage origImage;
    private int width;
    private int height;
    private int bitPixelWidth;

    public EffectManager(BufferedImage img) {
        effects = new String[] { "---", "Vertical Flip", "Horizontal Flip", "Invert", "Negative", "Darken", "Lighten",
                "8-Bit", "Pixel Sort", "Grayscale", "Sharpen", "Add Noise", "Saturate" };
        currImage = img;
        origImage = img;
        width = img.getWidth();
        height = img.getHeight();
        bitPixelWidth = 5;
    }

    public String[] getEffects() {
        return effects;
    }

    public BufferedImage getImage() {
        return currImage;
    }

    public BufferedImage buildImage(String s) {
        if (s.equals("Undo")) {
            BufferedImage temp = prevImage;
            prevImage = currImage;
            currImage = temp;
            return currImage;
        }
        prevImage = currImage;
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
        break;
        case "Pixel Sort":
            currImage = pixelSort();
        break;
        case "Grayscale":
            currImage = grayscale();
        break;
        case "Sharpen":
            currImage = sharpen();
        break;
        case "Add Noise":
            currImage = addNoise();
        break;
        case "Saturate":
            currImage = saturate();
        break;
        case "DEEPFRY":
            currImage = deepfry();
        break;
        case "Reset":
            currImage=origImage;
            bitPixelWidth=5;

        }

        return currImage;

    }

    private BufferedImage deepfry() {

        currImage = sharpen();
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double random = rand.nextDouble();
                if (random <= 0.25) {
                    Color col = new Color(currImage.getRGB(x, y), true);
                    float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                    currImage.setRGB(x, y,
                            Color.HSBtoRGB(hsb[0], hsb[1] + (float) ((0.2) * (1 - hsb[1])), (float) (hsb[2] * 0.9)));
                } else if (random <= 0.5) {
                    Color col = new Color(currImage.getRGB(x, y), true);
                    float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                    currImage.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1] + (float) ((0.2) * (1 - hsb[1])),
                            hsb[2] + (float) (0.15 * (1 - hsb[2]))));
                } else {
                    Color col = new Color(currImage.getRGB(x, y), true);
                    float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                    currImage.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1] + (float) ((0.2) * (1 - hsb[1])), hsb[2]));
                }
            }
        }
        currImage = sharpen();
        return saturate();
    }

    private BufferedImage saturate() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col = new Color(currImage.getRGB(x, y), true);
                float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                result.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1] + (float) ((0.2) * (1 - hsb[1])), hsb[2]));
            }
        }
        return result;
    }

    private BufferedImage addNoise() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double random = rand.nextDouble();
                if (random <= 0.1) {
                    Color col = new Color(currImage.getRGB(x, y), true);
                    float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                    result.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], (float) (hsb[2] * 0.9)));
                } else if (random <= 0.2) {
                    Color col = new Color(currImage.getRGB(x, y), true);
                    float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                    result.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] + (float) (0.15 * (1 - hsb[2]))));
                } else {
                    result.setRGB(x, y, currImage.getRGB(x, y));
                }
            }
        }
        return result;
    }

    private BufferedImage sharpen() {
        float[] data = { (float) -0.4, (float) -0.4, (float) -0.4, (float) -0.4, (float) 4.2, (float) -0.4,
                (float) -0.4, (float) -0.4, (float) -0.4 };
        Kernel kernel = new Kernel(3, 3, data);
        BufferedImageOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolve.filter(currImage, null);
    }

    private BufferedImage grayscale() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col = new Color(currImage.getRGB(x, y), true);
                float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
                result.setRGB(x, y, Color.HSBtoRGB(hsb[0], 0, hsb[2]));
            }
        }
        return result;
    }

    private BufferedImage pixelSort() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        List<Color> colorList = new ArrayList<Color>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colorList.add(new Color(currImage.getRGB(x, y), true));
            }
        }
        Collections.sort(colorList, new Comparator<Color>() {
            @Override
            public int compare(Color c1, Color c2) {
                return Integer.compare((int) (0.21 * c1.getRed() + 0.72 * c1.getGreen() + 0.07 * c1.getBlue()),
                        (int) (0.21 * c2.getRed() + 0.72 * c2.getGreen() + 0.07 * c2.getBlue()));
            }
        });
        System.out.println(colorList.size());
        System.out.println((width - 1) * (height - 1));
        System.out.println((height - 1) * width + height - 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, colorList.get(y * (width - 1) + y).getRGB());
            }
        }
        System.out.println("done");
        return result;
    }

    private BufferedImage eBit() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        int pixelWidth = bitPixelWidth;
        bitPixelWidth *= 2;
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

        int widthBorder = width % pixelWidth;
        int heightBorder = height % pixelWidth;
        if (widthBorder > 0) {
            for (int y = 0; y < height / pixelWidth; y++) {
                int rTotal = 0;
                int gTotal = 0;
                int bTotal = 0;
                int totalArea = 0;
                for (int dx = 0; dx < widthBorder; dx++) {
                    for (int dy = 0; dy < pixelWidth; dy++) {
                        Color col = new Color(currImage.getRGB(width - widthBorder + dx, y * pixelWidth + dy), true);
                        rTotal += col.getRed();
                        gTotal += col.getGreen();
                        bTotal += col.getBlue();
                        totalArea++;
                    }
                }

                Color col = new Color(rTotal / totalArea, gTotal / totalArea, bTotal / totalArea);
                for (int dx = 0; dx < widthBorder; dx++) {
                    for (int dy = 0; dy < pixelWidth; dy++) {
                        result.setRGB(width - widthBorder + dx, y * pixelWidth + dy, col.getRGB());
                    }
                }
            }
        }

        // height border calculations
        if (heightBorder > 0) {

            for (int x = 0; x < width / pixelWidth; x++) {
                int rTotal = 0;
                int gTotal = 0;
                int bTotal = 0;
                int totalArea = 0;
                for (int dx = 0; dx < pixelWidth; dx++) {
                    for (int dy = 0; dy < heightBorder; dy++) {
                        Color col = new Color(currImage.getRGB(x * pixelWidth + dx, height - heightBorder + dy), true);
                        rTotal += col.getRed();
                        gTotal += col.getGreen();
                        bTotal += col.getBlue();
                        totalArea++;
                    }
                }

                Color col = new Color(rTotal / totalArea, gTotal / totalArea, bTotal / totalArea);
                for (int dx = 0; dx < pixelWidth; dx++) {
                    for (int dy = 0; dy < heightBorder; dy++) {
                        result.setRGB(x * pixelWidth + dx, height - heightBorder + dy, col.getRGB());
                    }
                }
            }
        }

        // bottom corner calculations
        if (heightBorder > 0 && widthBorder > 0) {
            int rTotal = 0;
            int gTotal = 0;
            int bTotal = 0;
            int totalArea = 0;
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
        currImage = origImage;
        bitPixelWidth = 5;
        return origImage;
    }

}
