package interfacegraphique;

/**
 * CelsiusConverter.java is a 1.4 application that 
 * demonstrates the use of JButton, JTextField and
 * JLabel.  It requires no other files.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import autre.User;

public class PopupConnection implements ActionListener {
    JFrame converterFrame;
    JPanel converterPanel;
    JLabel TextLabel;
    JButton SeReconnecter;
    JButton Quitter;
    String texte;
    User user;
    
    public PopupConnection(String texte,User user) {
        //Create and set up the window.
    	this.texte=texte;
    	this.user=user;
    	
        converterFrame = new JFrame("Attention");
        converterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        converterFrame.setSize(new Dimension(120, 40));
        converterFrame.setLocationRelativeTo(null);
        
        //Create and set up the panel.
        converterPanel = new JPanel(new GridLayout(3, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        converterFrame.getRootPane().setDefaultButton(Quitter);

        //Add the panel to the window.
        converterFrame.getContentPane().add(converterPanel, BorderLayout.CENTER);

        //Display the window.
        converterFrame.pack();
        converterFrame.setVisible(true);
    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {
        //Create widgets.

        SeReconnecter = new JButton("SeReconnecter");
        Quitter = new JButton("Quitter");

        TextLabel = new JLabel("<html><b>"+this.texte+"</b></html>", SwingConstants.CENTER);

        //Listen to events from the Convert button.
        SeReconnecter.addActionListener(this);

        //Add the widgets to the container.
        converterPanel.add(TextLabel);
        converterPanel.add(SeReconnecter);
        converterPanel.add(Quitter);
        
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	converterFrame.dispose();
    	if (event.getSource() == SeReconnecter)
        {
        	converterFrame.dispose();
        	new DiscutionWindow(user);
        }
        
        if (event.getSource() == Quitter)
        {
        	converterFrame.dispose();
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

        Popup poop = new Popup(texte);
    }


}