package imaging;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import java.awt.dnd.DropTarget;

/**
 * Resizable GUI used to manipulate images. Contains a view and controller (and
 * model).
 * 
 * @author WP
 *
 */
public class ImagingGUI {

    private JFrame frame;

    /**
     * The GUI
     */
    public ImagingGUI() {

        // create frame
        frame = new JFrame("Imaging");
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // initialize manager/model
        EffectManager em = new EffectManager(null);

        // initialize view
        ImagingView view = new ImagingView(em);
        frame.add(view);

        // add drag and drop capabilities
        DragListener d = new DragListener(view);
        new DropTarget(frame, d);

        // initialize controller
        ImagingController control = new ImagingController(view, em);
        frame.add(control);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
