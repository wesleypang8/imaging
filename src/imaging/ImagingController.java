package imaging;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for GUI, allows client to choose which effect/action to perform on
 * image.
 * 
 * @author WP
 *
 */
public class ImagingController extends JPanel {

    private static final long serialVersionUID = 1L;

    private ImagingView view;
    JComboBox<String> options;

    /**
     * constructor
     * 
     * @param view
     *            the view to interact with
     * @param em
     *            the manager that operates on the image
     */
    public ImagingController(ImagingView view, EffectManager em) {
        if (view == null) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.setLayout(new FlowLayout());

        String[] effects = em.getEffects();

        this.add(new JLabel("Choose Effect: "));
        this.add(options = new JComboBox<String>(effects));

        JButton go = new JButton("Go!");
        go.addActionListener(new ButtonListener());
        this.add(go);

        JButton df = new JButton("DEEPFRY");
        df.addActionListener(new ButtonListener());
        this.add(df);

        JButton undo = new JButton("Undo");
        undo.addActionListener(new ButtonListener());
        this.add(undo);

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ButtonListener());
        this.add(reset);

    }

    /**
     * action listener for find path and reset buttons
     * 
     * @author WP
     *
     */
    private class ButtonListener implements ActionListener {

        /**
         * activates on ActionEvents. tells view which action to perform
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (action.equals("Go!")) {
                view.change((String) options.getSelectedItem());
            } else if (action.equals("DEEPFRY")) {
                view.change("DEEPFRY");

            } else if (action.equals("Undo")) {
                view.change("Undo");
            } else {

                view.change("Reset");
            }

        }

    }
}
