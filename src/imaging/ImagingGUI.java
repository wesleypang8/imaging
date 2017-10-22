package imaging;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class ImagingGUI {

    private JFrame frame;
    
    public ImagingGUI(){
        frame = new JFrame("Imaging");
        frame.setPreferredSize(new Dimension(1024,768));    
        frame.setLayout(new BoxLayout(frame.getContentPane(),
                BoxLayout.Y_AXIS));
        
        ImagingView view = new ImagingView();
        frame.add(view);
        
        ImagingController control = new ImagingController(view);
        frame.add(control);
        
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
}
