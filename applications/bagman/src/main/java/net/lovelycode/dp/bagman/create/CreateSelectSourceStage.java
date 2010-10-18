package net.lovelycode.dp.bagman.create;

import net.lovelycode.dp.bagman.WizardStage;
import net.lovelycode.dp.bagman.common.DataSelectionWizardStage;

/**
 * This stage allows multiple files to be selected
 * 
 * @author Andrew Jackson
 *
 */
public class CreateSelectSourceStage extends DataSelectionWizardStage {

	
	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected WizardStage getNextStage() {
		// The first time this page is seen, auto-press the button to select files.
		// FIXME This is a rather ugly way of achieving this functionality.
		if( shown == false ) {
			selector.openFileAdder();
			shown = true;
		}

		// TODO Auto-generated method stub
		return null;
	}
	
	

}
