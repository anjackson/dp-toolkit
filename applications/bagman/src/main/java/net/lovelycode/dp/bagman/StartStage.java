package net.lovelycode.dp.bagman;

import java.awt.GridLayout;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartStage extends WizardStage {

	private JPanel pnl;
	
	public StartStage(Properties properties) {
		super(properties);
		// Set up the GUI bits:
	    pnl = new JPanel();
	    pnl.setBorder( BorderFactory.createTitledBorder( "Data Carrier Specification" ) );
	    pnl.setLayout( new GridLayout(3,2));
	    // Project
	    pnl.add( new JLabel("Project Identifier:"));
	    String[] data = {"EAP", "EAP180"};
	    JComboBox cb = new JComboBox(data);
	    cb.setEditable( true );
	    cb.setToolTipText("e.g. EAP180");
	    pnl.add(cb);
	    // Carrier
	    pnl.add( new JLabel("Data Carrier Identification:"));
	    JTextField textfield1 = new JTextField("Type something here",15);
	    textfield1.setToolTipText("e.g. Disk #B1-44");
	    pnl.add(textfield1);
	}

	@Override
	public JPanel getWizardPanel() {
		System.out.println("Getting start panel");
	    return pnl;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends WizardStage> T getNextStage() {
		return (T) new CloneSelectSourceStage(this.getProperties());
	}

}
