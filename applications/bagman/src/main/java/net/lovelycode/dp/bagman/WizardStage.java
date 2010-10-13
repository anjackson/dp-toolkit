package net.lovelycode.dp.bagman;

import javax.swing.JPanel;

public abstract class WizardStage {
	
	private BagMan wizardFrame;
	
	private final JPanel panel = new JPanel();

	public WizardStage() { }
	
	public JPanel getPanel() {
		return panel;
	}
	
	abstract public void validate();
	
	protected abstract WizardStage getNextStage();
	
	protected BagMan getWizardFrame() {
		return this.wizardFrame;
	}

	public void setWizardFrame(BagMan wizardFrame) {
		this.wizardFrame = wizardFrame;
	}
}
