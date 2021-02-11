package graphicalInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import other.User;

public class PopupConnection implements ActionListener {
    JFrame Frame;
    JPanel Panel;
    JLabel TextLabel;
    JButton SeReconnecter;
    JButton Quitter;
    String texte;
    User user;
    
    public PopupConnection(String texte,User user) {
        //Create and set up the window.
    	this.texte=texte;
    	this.user=user;
    	
        Frame = new JFrame("Attention");
        Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Frame.setSize(new Dimension(120, 40));
        Frame.setLocationRelativeTo(null);
        
        //Create and set up the panel.
        Panel = new JPanel(new GridLayout(3, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        Frame.getRootPane().setDefaultButton(Quitter);

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

        SeReconnecter = new JButton("Se reconnecter");
        Quitter = new JButton("Quitter");

        TextLabel = new JLabel("<html><b>"+this.texte+"</b></html>", SwingConstants.CENTER);

        //Listen to events from the Convert button.
        SeReconnecter.addActionListener(this);
        Quitter.addActionListener(this);

        //Add the widgets to the container.
        Panel.add(TextLabel);
        Panel.add(SeReconnecter);
        Panel.add(Quitter);
        
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	Frame.dispose();
    	if (event.getSource() == SeReconnecter)
        {
        	Frame.dispose();
        	new DiscutionWindow(user);
        }
        
        if (event.getSource() == Quitter)
        {
        	Frame.dispose();
        }
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