package imaging;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;
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

        change("Reset");
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
        graphics.setColor(Color.WHITE);
        String dnd = "Drag and Drop an Image!";
        FontMetrics fm = graphics.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(dnd, graphics);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        graphics.drawString(dnd, x, y);

        if(img==null){
            return;
        }
        double scalingFactor = this.getHeight() / (double) img.getHeight();
        if ((int) (img.getWidth() * scalingFactor) > this.getWidth()) {
            scalingFactor = this.getWidth() / (double) img.getWidth();
        }

        
        int destX = (int) (img.getWidth() * scalingFactor);
        int destY = (int) (img.getHeight() * scalingFactor);
        
        int xOffset = (int)((this.getWidth()-destX)/2.0);
        int yOffset = (int)((this.getHeight()-destY)/2.0);
        
        graphics.drawImage(img, xOffset, yOffset, destX+xOffset, destY+yOffset,
                0, 0, img.getWidth(), img.getHeight(), null);


        

    }

    public void setImage(BufferedImage img){
        this.img = em.setImage(img);
        repaint();
    }

    public void change(String s) {
        if(img == null){
            return;
        }
        img = em.buildImage(s);
        repaint();
    }
}
