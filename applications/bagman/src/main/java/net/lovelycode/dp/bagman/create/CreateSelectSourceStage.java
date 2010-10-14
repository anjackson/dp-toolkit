package net.lovelycode.dp.bagman.create;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import net.lovelycode.dp.bagman.BagMan;
import net.lovelycode.dp.bagman.StartStage;
import net.lovelycode.dp.bagman.WizardStage;
import net.lovelycode.dp.bagman.common.FileSelectionPanel;

/**
 * This stage allows multiple files to be selected
 * 
 * @author Andrew Jackson
 *
 */
public class CreateSelectSourceStage extends WizardStage {

	private static Logger log = Logger.getLogger(CreateSelectSourceStage.class);
	
	private boolean shown = false;

	private FileSelectionPanel selector;
	
    private JTextField diskName;

	public CreateSelectSourceStage() {
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Choose Source Files & Folders" ) );
	    getPanel().setLayout( new GridLayout(1,1));

	    // File Selector
	    selector = new FileSelectionPanel(this.getWizardFrame());
	    
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
	    //chooseButton.doClick();
	    
	}
	
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
