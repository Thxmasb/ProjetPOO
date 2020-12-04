
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

public class Connexion implements ActionListener {
    JFrame converterFrame;
    JPanel converterPanel;
    JTextField username;
    JLabel UsernameLabel, TextLabel;
    JButton connexion;
    JButton newcompte;

    public Connexion() {
        //Create and set up the window.
        converterFrame = new JFrame("Clavarding - Connexion");
        converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        converterFrame.setSize(new Dimension(120, 40));
        converterFrame.setLocationRelativeTo(null);
        //Create and set up the panel.
        converterPanel = new JPanel(new GridLayout(5, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        converterFrame.getRootPane().setDefaultButton(connexion);

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
        username = new JTextField();
       
        connexion = new JButton("Connexion");
        newcompte = new JButton("Nouveau Compte");
        
        UsernameLabel = new JLabel("<html><b>Veuillez rentrer un username</b></html>", SwingConstants.CENTER);
        TextLabel = new JLabel("<html><b>Bonjour, veuillez vous connectez</b></html>", SwingConstants.CENTER);

        //Listen to events from the Convert button.
        connexion.addActionListener(this);
        newcompte.addActionListener(this);

        //Add the widgets to the container.
        converterPanel.add(TextLabel);
        converterPanel.add(UsernameLabel);
        converterPanel.add(username);
        converterPanel.add(connexion);
        converterPanel.add(newcompte);
        
        UsernameLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	Object source = event.getSource();
        //Se connecter
    	 String csvFile = "/Users/thomasballotin/ProjetPOO/src/interfacegraphique/bdd_utilisateur.txt";
         BufferedReader br = null;
         String line = "";
         String cvsSplitBy = ",";

         try {

             br = new BufferedReader(new FileReader(csvFile));
             while ((line = br.readLine()) != null) {

                //use comma as separator
                 String[]username = line.split(cvsSplitBy);

                 if(source == this.newcompte) {
                	 if(username[0].equals(this.username.getText())) {
                		 Popup.createAndShowGUIPopup("Username déjà pris");
                	 }
                	 else {//On rajoute username
                	 }	 
                 }
                 else if(source == this.connexion){
                	 if(username[0].equals(this.username.getText())) {
	                	 //On modifie le fichier et on ouvre l'interface de chat
	                	 System.out.println("On se connecte");
                	 }
                	 else {
                		 Popup.createAndShowGUIPopup("Username non reconnu");
                	 }
                 } 
                 
             }

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (br != null) {
                 try {
                     br.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        Connexion converter = new Connexion();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}