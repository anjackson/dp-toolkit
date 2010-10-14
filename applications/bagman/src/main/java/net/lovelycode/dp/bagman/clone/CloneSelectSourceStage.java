package net.lovelycode.dp.bagman.clone;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.lovelycode.dp.bagman.BagMan;
import net.lovelycode.dp.bagman.WizardStage;

public class CloneSelectSourceStage extends WizardStage {

	private JButton chooseButton;

	public CloneSelectSourceStage() {
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Data Carrier Specification" ) );
	    getPanel().setLayout( new GridLayout(3,2));
	    // Project
	    getPanel().add( new JLabel("Project Identifier:"));
	    String[] data = {"EAP", "EAP180"};
	    JComboBox cb = new JComboBox(data);
	    cb.setEditable( true );
	    cb.setToolTipText("e.g. EAP180");
	    getPanel().add(cb);
	    // Carrier
	    getPanel().add( new JLabel("Data Carrier Identification:"));
	    JTextField textfield1 = new JTextField("Type something here",15);
	    textfield1.setToolTipText("e.g. Disk #B1-44");
	    getPanel().add(textfield1);
	    chooseButton = new JButton("Choose");
	    getPanel().add(chooseButton);
	    chooseButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    JFileChooser chooser = new JFileChooser();
			    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
			    // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
			    /*
			    ExampleFileFilter filter = new ExampleFileFilter();
			    filter.addExtension("jpg");
			    filter.addExtension("gif");
			    filter.setDescription("JPG & GIF Images");
			    chooser.setFileFilter(filter);
			    */
			    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			    chooser.setMultiSelectionEnabled(true);
			    int returnVal = chooser.showOpenDialog(getWizardFrame());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.print("You chose to open this file:");
			       for( File f : chooser.getSelectedFiles() ) {
			        	System.out.print(" "+f);
			       }
			       System.out.println(" ");
			            
			    }
			}});
	    
	}
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected WizardStage getNextStage() {
		System.out.println("Get Next...");
		// TODO Auto-generated method stub
		return null;
	}

}
