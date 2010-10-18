package net.lovelycode.dp.bagman.common;

import net.lovelycode.dp.bagman.WizardStage;
import net.lovelycode.dp.bagman.common.FileSelectionPanel.FileSystemModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;

public abstract class DataSelectionWizardStage extends WizardStage {

	private static Logger log = Logger.getLogger(DataSelectionWizardStage.class);
	
	protected boolean shown = false;

	protected FileSelectionPanel selector;
	
    private JTextField diskName;

	public DataSelectionWizardStage() {
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Choose Source Files & Folders" ) );
	    getPanel().setLayout( new GridLayout(1,1));

	    // File Selector
	    selector = new FileSelectionPanel(this.getWizardFrame());
	    selector.setFileSelectionActionListener( new FileSelectionPanel.FileSelectionActionListener() {
			@Override
			public void fileSelectionChanged(FileSystemModel fsm) {
				log.info("Selection Changed.");
		        // Get the disk name:
		        FileWrapper root = fsm.getRoot();
		        if( root == null ) return;
		        String displayName = FileSystemView.getFileSystemView().
		                getSystemDisplayName(root);
		        displayName = displayName.replaceAll(" \\([A-Z]:\\)$", "");
		        diskName.setText(displayName);
		        root.showFileInfo();
			    while(root.getParentFile() != null ) {
			    	root = new FileWrapper( root.getParentFile().getAbsolutePath() );
			    	root.showFileInfo();
			    }
				
			}} );
	    
	    // The other panel:
	    JPanel inspector = new JPanel();
	    inspector.setLayout( new BorderLayout() );
	    diskName = new JTextField();
	    inspector.add(diskName, BorderLayout.NORTH);

	    // Assemble the splitpane
	    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
	    		selector.getPanel(), inspector);
	    splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerLocation(200);
	    getPanel().add(splitPane);
	    /*	    
		    //Provide minimum sizes for the two components in the split pane
		    Dimension minimumSize = new Dimension(100, 50);
		    //options.setMinimumSize(minimumSize);
		    //pictureScrollPane.setMinimumSize(minimumSize);
	     */

	    // Now auto-fire the Add operation:
	    // Fails because these are constructed before being shown.
	    // selector.openFileAdder();
	}

}
