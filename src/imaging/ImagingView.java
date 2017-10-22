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
    
    private EffectManager em;

    public ImagingView(EffectManager em) {
        this.em=em;
        img = em.getImage();
        this.setPreferredSize(new Dimension(1024,600));
        
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

        img = em.reset();
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

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,this.getWidth(),this.getHeight());
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
        
        int destX = (int) (img.getWidth() * scalingFactor);
        int destY = (int) (img.getHeight() * scalingFactor);
        
        int xOffset = (int)((this.getWidth()-destX)/2.0);
        
        graphics.drawImage(img, xOffset, 0, destX+xOffset, destY,
                0, 0, img.getWidth(), img.getHeight(), null);
        // graphics.drawImage(img, 0, 0, (int)(img.getWidth()*scalingFactor),
        // (int)(img.getHeight()*scalingFactor), 0, 0, img.getWidth(),
        // img.getHeight(), null);

        

    }



    public void change(String s) {
        img = em.buildImage(s);
        repaint();
    }
}
