package net.lovelycode.dp.bagman;

import javax.swing.JPanel;

public abstract class WizardStage {
	
	private final BagMan wizardFrame;
	
	private final JPanel panel = new JPanel();

	public WizardStage(BagMan wizardFrame ) {
		this.wizardFrame = wizardFrame;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	abstract public void validate();
	
	abstract public <T extends WizardStage> T getNextStage();
	
	protected BagMan getWizardFrame() {
		return this.wizardFrame;
	}
}
