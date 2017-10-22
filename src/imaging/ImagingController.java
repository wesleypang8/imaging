package imaging;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ImagingController extends JPanel{


    private static final long serialVersionUID = 1L;

    private ImagingView view;
    JComboBox<String> options;
    
    public ImagingController(ImagingView view){
        if(view==null){
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.setLayout(new FlowLayout());
        
        String[] effects = {"Original", "Negative"};
        
        this.add(new JLabel("Choose Effect: "));
        this.add(options = new JComboBox<String>(effects));
        
        JButton go = new JButton("Go!");
        go.addActionListener(new ButtonListener());
        this.add(go);
    }
    
    /**
     * action listener for find path and reset buttons
     * 
     * @author WP
     *
     */
    private class ButtonListener implements ActionListener {

        /**
         * activates on ActionEvents. Either tells view to find a path or resets
         * the view and controller
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (action.equals("Go!"))
                view.flip();
            

        }

    }
}
