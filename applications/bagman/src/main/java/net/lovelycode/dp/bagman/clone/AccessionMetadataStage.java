package net.lovelycode.dp.bagman.clone;

import java.awt.GridLayout;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.lovelycode.dp.bagman.BagMan;
import net.lovelycode.dp.bagman.WizardStage;

public class AccessionMetadataStage extends WizardStage {

	public AccessionMetadataStage() {
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Data Carrier Specification" ) );
	    getPanel().setLayout( new GridLayout(0,2));
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
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected WizardStage getNextStage() {
		// TODO Auto-generated method stub
		return null;
	}

}
