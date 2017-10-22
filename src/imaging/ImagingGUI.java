package imaging;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class ImagingGUI {

    private JFrame frame;
    
    public ImagingGUI(){
        
        EffectManager em = new EffectManager(getImage("./src/imaging/RedApple.jpg"));

        
        frame = new JFrame("Imaging");
        frame.setPreferredSize(new Dimension(1024,768));    
        frame.setLayout(new BoxLayout(frame.getContentPane(),
                BoxLayout.Y_AXIS));
        
        ImagingView view = new ImagingView(em);
        frame.add(view);
        
        ImagingController control = new ImagingController(view, em);
        frame.add(control);
        
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    
    
}
