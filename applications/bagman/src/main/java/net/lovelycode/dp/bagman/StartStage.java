package net.lovelycode.dp.bagman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import net.lovelycode.dp.bagman.clone.CloneSelectSourceStage;
import net.lovelycode.dp.bagman.create.CreateSelectSourceStage;
import net.lovelycode.dp.bagman.validate.ValidateSelectSourceStage;

public class StartStage extends WizardStage {
	
	private static Logger log = Logger.getLogger(StartStage.class);
	
	int currentSelection = -1;
	
	String defaultText = "Please choose an option from this list.";
	
	static class WizardChoice {
		String title;
		String description;
		Class<? extends WizardStage> nextStage;
		
		WizardChoice(String title, String description, Class<? extends WizardStage> stage ) {
			this.title = title;
			this.description = description;
			this.nextStage = stage;
		}
	}
	
	private static List<WizardChoice> choices = new Vector<WizardChoice>();
	
	public StartStage() {
		
		choices.add( 
				new WizardChoice(
						"Create Collection", 
						"Create a Collection from a folder, set of files, a disk, or other source.", 
						CreateSelectSourceStage.class)
				);
		choices.add( 
				new WizardChoice(
						"Validate Collection", 
						"Validate an existing Collection.", null ) //ValidateSelectSourceStage.class)
				);
		choices.add( 
				new WizardChoice(
						"Clone Collection", 
						"Clone an existing Collection.", null ) //CloneSelectSourceStage.class)
				);
	
		// Set up the GUI bits:
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Choose" ) );
	    getPanel().setLayout( new GridLayout(0,2));

	    DefaultListModel listModel = new DefaultListModel();
	    for( WizardChoice c : choices ) {
	    	listModel.addElement(c.title);
	    }
	    final JEditorPane explanation = new JEditorPane();
	    explanation.setBorder( BorderFactory.createEmptyBorder(0, 10, 0, 10) );
	    explanation.setContentType("text/plain");
	    explanation.setOpaque(false);
	    explanation.setEditable(false);
	    explanation.setFocusable(false);
	    explanation.setText(defaultText);

	    final JList options = new JList(listModel);
	    options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    options.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
	    options.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				currentSelection = options.getSelectedIndex();
				if( currentSelection >= 0 ) {
					log.info("Got " + currentSelection + " : "+ choices.get(currentSelection).title );
					explanation.setText(choices.get(currentSelection).description);
				} else {
					explanation.setText(defaultText);
				}
				getWizardFrame().updateButtons();
			}
	    });
	    getPanel().add(options);
	    getPanel().add(explanation);

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		if( currentSelection < -1 ) {
			log.error("Nothing selected!");
		}
	}

	@Override
	protected WizardStage getNextStage() {
		if( currentSelection >= 0 ) {
			try {
				Class<? extends WizardStage> wsClass = choices.get(currentSelection).nextStage;
				if( wsClass == null ) return null;
				WizardStage ws = wsClass.newInstance();
				ws.setWizardFrame(this.getWizardFrame());
				return ws;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
