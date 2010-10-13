package net.lovelycode.dp.bagman;

import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CloneSelectSourceStage extends WizardStage {

	private JPanel pnl;

	public CloneSelectSourceStage(Properties properties) {
		super(properties);
		pnl = new JPanel();
		pnl.add( new JButton("DO IT"));
	}

	@Override
	public JPanel getWizardPanel() {
		return pnl;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends WizardStage> T getNextStage() {
		// TODO Auto-generated method stub
		return null;
	}

}
