package imaging;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;

import javax.swing.JComponent;

/**
 * view for GUI, displays the image
 * 
 * @author WP
 *
 */
public class ImagingView extends JComponent {
    private static final long serialVersionUID = 1L;

    private BufferedImage img;

    private EffectManager em;

    /**
     * constructor
     * 
     * @param em
     *            the manager that performs actions on the image
     */
    public ImagingView(EffectManager em) {
        this.em = em;
        img = null;
        this.setPreferredSize(new Dimension(1024, 600));

    }

    /**
     * resets and updates the view for gui
     * 
     */
    public void reset() {

        change("Reset");
        repaint();
    }

    /**
     * paints the components
     * 
     * @param g
     *            graphics for painting
     * @modifies the view displayed
     * @effects draws the image
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;

        // black background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        // writes the message dead center
        graphics.setColor(Color.WHITE);
        String dnd = "Drag and Drop an Image!";
        FontMetrics fm = graphics.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(dnd, graphics);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        graphics.drawString(dnd, x, y);

        // only draw an image if theres something to draw
        if (img == null) {
            return;
        }

        // ensures image sits nicely
        double scalingFactor = this.getHeight() / (double) img.getHeight();
        if ((int) (img.getWidth() * scalingFactor) > this.getWidth()) {
            scalingFactor = this.getWidth() / (double) img.getWidth();
        }

        // ensures image is centered
        int destX = (int) (img.getWidth() * scalingFactor);
        int destY = (int) (img.getHeight() * scalingFactor);

        int xOffset = (int) ((this.getWidth() - destX) / 2.0);
        int yOffset = (int) ((this.getHeight() - destY) / 2.0);

        graphics.drawImage(img, xOffset, yOffset, destX + xOffset, destY + yOffset, 0, 0, img.getWidth(),
                img.getHeight(), null);

    }

    /**
     * sets the image for the view
     * 
     * @param img
     *            the image to be set
     */
    public void setImage(BufferedImage img) {
        this.img = em.setImage(img);
        repaint();
    }

    /**
     * performs an action on the image
     * 
     * @param s
     *            the action to perform
     */
    public void change(String s) {
        if (img == null) {
            return;
        }
        img = em.buildImage(s);
        repaint();
    }
}
