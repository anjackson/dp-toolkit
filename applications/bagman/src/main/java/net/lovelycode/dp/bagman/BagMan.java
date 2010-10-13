/**
 * 
 */
package net.lovelycode.dp.bagman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.driver.CommandLineBagDriver;

/**
 * @author anj
 *
 */
public class BagMan extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jWizardPane = new JPanel();
    private JButton nextButton = null;
    private JButton backButton = null;
    private JButton cancelButton = null;
    private Properties properties = new Properties();
    private List<WizardStage> stages = new Vector<WizardStage>(); 

    /**
     */
    public BagMan() {
        super();
        initialize();
        BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        //bag.addFileToPayload(null);
    }
    
    /**
     * Example invocation from BagIt sources.
     * @param args
     * @throws Exception
     */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		BagMan bm = new BagMan();
	    // Finish up and centre on the screen
	    bm.setLocationRelativeTo(null);
		bm.setVisible(true);
		
/*
		CommandLineBagDriver driver = new CommandLineBagDriver();		
		int ret = driver.execute(args);
		if (ret == CommandLineBagDriver.RETURN_ERROR) {
			System.err.println(MessageFormat.format("An error occurred. Check the {0}/logs/bag-{1}.log for more details.", System.getProperty("app.home"), System.getProperty("log.timestamp")));
		}
		System.exit(ret);
		*/
	}


    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setTitle("BagMan Data Migration Tool");
	    setLayout(new BorderLayout());
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    
	    // Navigation
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new BorderLayout());
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Cancel!");
				doCancel();
			}
		}
		);
	    backButton = new JButton("Back");
	    backButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Back!");
				goToPreviousStage();
			}
		}
		);
	    nextButton = new JButton("Next");
	    nextButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Next!");
				goToNextStage(getCurrentStage().getNextStage());
			}
		}
		);
	    JPanel bcPnl = new JPanel();
	    bcPnl.add(backButton);
	    bcPnl.add(nextButton);
	    bottomPanel.add(bcPnl, BorderLayout.EAST);
	    JPanel cPnl = new JPanel();
	    cPnl.add(cancelButton);
	    bottomPanel.add(cPnl, BorderLayout.WEST);
	    // Add components to the frame:
	    jWizardPane.setLayout(new GridLayout(1,1));
	    add(jWizardPane, BorderLayout.CENTER);
	    add(bottomPanel, BorderLayout.SOUTH);
	    this.setMinimumSize(new Dimension(400,300));

	    WizardStage stage = new StartStage();
	    stage.setWizardFrame(this);
	    this.goToNextStage(stage);
	    
	    pack();

    }
    
	/**
     * 
     */
    protected void doCancel() {
    	int n = JOptionPane.showConfirmDialog(
    		    this,
    		    "Are you sure you want to exit?",
    		    "Are you sure?",
    		    JOptionPane.YES_NO_OPTION);
    	if( n == JOptionPane.OK_OPTION ) {
    		System.exit(0);
    	}
	}

    /**
     * 
     * @param s
     */
	protected void goToNextStage(WizardStage nextStage) {
		// Validate.
		// Go
		// Move forward.
		this.removeCurrentStage();
		stages.add(nextStage);
		this.setStage(nextStage);
	}
	
	protected void setStage(WizardStage s) {
    	jWizardPane.add(s.getPanel(), BorderLayout.CENTER);
    	this.pack();
	    this.setLocationRelativeTo(null);
	    this.updateButtons();
	    this.repaint();
	}
	
	protected void updateButtons() {
	    if( stages.size() <= 1 ) {
	    	this.backButton.setEnabled(false);
	    } else {
	    	this.backButton.setEnabled(true);
	    }
	    if( getCurrentStage().getNextStage() == null ) {
	    	this.nextButton.setEnabled(false);
	    } else {
	    	this.nextButton.setEnabled(true);
	    }
	}
	
    protected void goToPreviousStage() {
    	System.out.println("Size: "+stages.size());
    	if( stages.size() <= 1 ) return;
    	this.removeCurrentStage();
    	stages.remove(stages.size()-1);
    	this.setStage(stages.get(stages.size()-1));
	}
    
    protected void removeCurrentStage() {
		WizardStage currentStage = this.getCurrentStage();
		if( currentStage != null ) {
			jWizardPane.remove(currentStage.getPanel());
		}
    }
    
    private WizardStage getCurrentStage() {
		int currentStageIndex = stages.size() - 1;
		if( currentStageIndex >= 0 ) {
			return stages.get(currentStageIndex);
		} else {
			return null;
		}
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    /*
    private JPanel getJContentPane() {
        if (jWizardPane == null) {
            jWizardPane = new JPanel();
            jWizardPane.setLayout(new BorderLayout());
            jWizardPane.add(new JButton("One"), BorderLayout.SOUTH);
            jWizardPane.add(new JToolBar(), BorderLayout.NORTH);
        }
        return jWizardPane;
    }
*/    
}
