package net.lovelycode.dp.bagman;

import java.util.Properties;

import javax.swing.JPanel;

public abstract class WizardStage {
	
	private Properties properties;

	public WizardStage(Properties properties) {
		this.properties = properties;
	}
	
	protected Properties getProperties() {
		return this.properties;
	}

	abstract public JPanel getWizardPanel();
	
	abstract public void validate();
	
	abstract public <T extends WizardStage> T getNextStage();
	
}
