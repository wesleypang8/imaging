package imaging;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.awt.Color;
import java.awt.image.*;

/**
 * This class manages the effects and how they work. Works with ImagingView to
 * manipulate the current image.
 * 
 * @author WP
 *
 */
public class EffectManager {

    private String[] effects;
    private BufferedImage currImage;
    private Stack<BufferedImage> prevImages;
    private int width;
    private int height;
    private int bitPixelWidth;

    /**
     * constructor, loads in pre given list of effects.
     * 
     * @param img
     *            the image to initialize to if any
     */
    public EffectManager(BufferedImage img) {
        effects = new String[] { "---", "Vertical Flip", "Horizontal Flip", "Invert", "Negative", "Darken", "Lighten",
                "8-Bit", "Pixel Sort", "Grayscale", "Sharpen", "Add Noise", "Saturate", "Light Only" };
        currImage = img;
        prevImages = new Stack<BufferedImage>();
        bitPixelWidth = 5;
    }

    /**
     * overrides old image with new image
     * 
     * @param img
     *            new image
     * @return the same image
     */
    public BufferedImage setImage(BufferedImage img) {
        currImage = img;
        prevImages.clear();
        width = img.getWidth();
        height = img.getHeight();
        bitPixelWidth = 5;
        return img;
    }

    /**
     * returns array of all effects
     * 
     * @return array of all effects
     */
    public String[] getEffects() {
        return effects;
    }

    /**
     * manipulates the image with the chosen effect/action. Note that pixel
     * width for the 8-bit effect will only reset when "Reset" is chosen
     * 
     * @param s
     *            the effect/action to perform on the image
     * @return the post-op version of the image.
     */
    public BufferedImage buildImage(String s) {
        if (s.equals("Undo")) {

            if (!prevImages.isEmpty()) {
                currImage = prevImages.pop();
            }

            return currImage;
        }
        prevImages.push(currImage);
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
        case "Light Only":
            currImage = lightOnly();
        break;
        case "DEEPFRY":
            currImage = deepfry();
        break;
        case "Reset":
            while (!prevImages.isEmpty()) {
                currImage = prevImages.pop();
            }
            bitPixelWidth = 5;

        }

        return currImage;

    }

    private BufferedImage lightOnly() {
        BufferedImage result = new BufferedImage(currImage.getWidth(), currImage.getHeight(), 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color col1 = (new Color(currImage.getRGB(x, y), true));
                float b1 = Color.RGBtoHSB(col1.getRed(), col1.getGreen(), col1.getBlue(), null)[2];
                if (b1 > 0.5) {
                    result.setRGB(x, y, currImage.getRGB(x, y));
                }
            }
        }
        return result;

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
                float b1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null)[2];
                float b2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null)[2];
                return Double.compare(b1, b2);

            }
        });

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, colorList.get(y * (width - 1) + y).getRGB());
            }
        }
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

}
