/**
 * 
 */
package net.lovelycode.dp.bagman;

import java.awt.BorderLayout;
import java.text.MessageFormat;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JToolBar;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.driver.CommandLineBagDriver;

/**
 * @author anj
 *
 */
public class BagMan extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JButton jButton = null;
    private JToolBar jToolBar = null;
    private JButton jButton1 = null;

    /**
     * This is the default constructor
     */
    public BagMan() {
        super();
        initialize();
        BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        bag.addFileToPayload(null);
    }
    
    /**
     * Example invocation from BagIt sources.
     * @param args
     * @throws Exception
     */
	public static void main(String[] args) throws Exception {
		CommandLineBagDriver driver = new CommandLineBagDriver();		
		int ret = driver.execute(args);
		if (ret == CommandLineBagDriver.RETURN_ERROR) {
			System.err.println(MessageFormat.format("An error occurred. Check the {0}/logs/bag-{1}.log for more details.", System.getProperty("app.home"), System.getProperty("log.timestamp")));
		}
		System.exit(ret);
	}


    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJButton(), BorderLayout.SOUTH);
            jContentPane.add(getJToolBar(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Go");
        }
        return jButton;
    }

    /**
     * This method initializes jToolBar	
     * 	
     * @return javax.swing.JToolBar	
     */
    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            jToolBar.add(getJButton1());
        }
        return jToolBar;
    }

    /**
     * This method initializes jButton1	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("hel");
        }
        return jButton1;
    }

}
