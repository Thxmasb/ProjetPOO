package graphicalInterface;

/**
 * CelsiusConverter.java is a 1.4 application that 
 * demonstrates the use of JButton, JTextField and
 * JLabel.  It requires no other files.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Popup implements ActionListener {
    JFrame Frame;
    JPanel Panel;
    JLabel TextLabel;
    JButton OK;
    String Text;

    public Popup(String Text) {
        //Create and set up the window.
    	this.Text=Text;
        Frame = new JFrame("Attention");
        Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Frame.setSize(new Dimension(120, 40));
        Frame.setLocationRelativeTo(null);
        
        //Create and set up the panel.
        Panel = new JPanel(new GridLayout(2, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        Frame.getRootPane().setDefaultButton(OK);

        //Add the panel to the window.
        Frame.getContentPane().add(Panel, BorderLayout.CENTER);

        //Display the window.
        Frame.pack();
        Frame.setVisible(true);
    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {
        //Create widgets.
    	//Add the OK button
        OK = new JButton("OK");
        
        //Show the message
        TextLabel = new JLabel("<html><b>"+this.Text+"</b></html>", SwingConstants.CENTER);

        //Listen to events from the OK button.
        OK.addActionListener(this);

        //Add the widgets to the container.
        Panel.add(TextLabel);
        Panel.add(OK);
        
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    //Function to close the window when clicking on the ok button.
    public void actionPerformed(ActionEvent event) {
    	Frame.dispose();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    static void createAndShowGUIPopup(String texte) {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        new Popup(texte);
    }


}