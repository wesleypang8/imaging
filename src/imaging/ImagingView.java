package imaging;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImagingView extends JComponent {
    private static final long serialVersionUID = 1L;

    private BufferedImage img;

    public ImagingView() {
        img = this.getImage("./src/imaging/RedApple.jpg");
        this.setPreferredSize(new Dimension(1024,600));
    }

    /**
     * returns the image corresponding to input
     * 
     * @param s
     *            name/relative path of image
     * @return the image as a BufferedImage
     * @throws IllegalArgumentException
     *             if arg is null
     */
    private BufferedImage getImage(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        try {
            return ImageIO.read(new File(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * resets and updates the view for gui
     * 
     * @modifies this.path
     * @modifies this.start
     * @effects sets path to null
     * @effects sets start to null
     */
    public void reset() {

        // update view
        repaint();
    }

    /**
     * paints the components
     * 
     * @param g
     *            graphics for painting
     * @modifies the view displayed
     * @effects draws the image and path/markers if a path is chosen.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;

        graphics.setBackground(Color.BLACK);
        // scales image pixels to size of view so locations match
        double wRatio = (this.getWidth() + 0.0) / img.getWidth();
        double hRatio = (this.getHeight() + 0.0) / img.getHeight();

        System.out.println(this.getWidth());
        System.out.println(this.getHeight());
        double scalingFactor = this.getHeight() / (double) img.getHeight();
        if ((int) (img.getWidth() * scalingFactor) > this.getWidth()) {
            scalingFactor = this.getWidth() / (double) img.getWidth();
        }
        System.out.println("scaling: "+scalingFactor);
        // double xScalingFactor = this.getWidth()/(double)img.getWidth();
        // draws map
        graphics.drawImage(img, 0, 0, (int) (img.getWidth() * scalingFactor), (int) (img.getHeight() * scalingFactor),
                0, 0, img.getWidth(), img.getHeight(), null);
        // graphics.drawImage(img, 0, 0, (int)(img.getWidth()*scalingFactor),
        // (int)(img.getHeight()*scalingFactor), 0, 0, img.getWidth(),
        // img.getHeight(), null);

        
        if (fliip) {
            graphics.drawImage(img, (int) (img.getWidth() * scalingFactor), (int) (img.getHeight() * scalingFactor), 0,
                    0, 0, 0, img.getWidth(), img.getHeight(), null);
        }
    }

    public boolean fliip = false;

    public void flip() {
        fliip = true;
        repaint();
    }
}
