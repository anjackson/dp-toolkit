package net.lovelycode.dp.bagman.create;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import net.lovelycode.dp.bagman.BagMan;
import net.lovelycode.dp.bagman.WizardStage;

/**
 * This stage allows multiple files to be selected
 * 
 * @author Andrew Jackson
 *
 */
public class CreateSelectSourceStage extends WizardStage {

	private JTree fileTree;

	public CreateSelectSourceStage() {
	    getPanel().setBorder( BorderFactory.createTitledBorder( "Choose Source Files & Folders" ) );
	    getPanel().setLayout( new GridLayout(1,1));
	    // File Selector
	    JPanel selector = new JPanel();
	    selector.setLayout( new BorderLayout() );
	    this.treeSP = new JScrollPane();
	    selector.add( this.treeSP, BorderLayout.CENTER );
	    JPanel tools = new JPanel();
	    selector.add(tools, BorderLayout.SOUTH);
	    tools.setLayout( new BorderLayout());
	    JPanel selectionButtons = new JPanel();
	    selectionButtons.setLayout(new GridLayout(1,2));
	    tools.add(selectionButtons, BorderLayout.WEST);
	    JButton chooseButton = new JButton("+");
	    selectionButtons.add(chooseButton);
	    chooseButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    JFileChooser chooser = new JFileChooser();
			    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
			    // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
			    /*
			    ExampleFileFilter filter = new ExampleFileFilter();
			    filter.addExtension("jpg");
			    filter.addExtension("gif");
			    filter.setDescription("JPG & GIF Images");
			    chooser.setFileFilter(filter);
			    */
			    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			    chooser.setMultiSelectionEnabled(true);
			    int returnVal = chooser.showOpenDialog(getWizardFrame());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.print("You chose to open this file:");
			       for( File f : chooser.getSelectedFiles() ) {
			        	System.out.print(" "+f);
			       }
			       System.out.println(" ");
			       createTree(chooser.getSelectedFile());
			    }
			}});
	    // And the removal button
	    JButton minusButton = new JButton("-");
	    selectionButtons.add(minusButton);
	    
	    // Add add the configuration menu button
	    JButton configButton = new JButton("@");
	    tools.add(configButton, BorderLayout.EAST);

	    // The other panel:
	    JPanel inspector = new JPanel();
	    inspector.setLayout( new BorderLayout() );
	    diskName = new JTextField();
	    inspector.add(diskName, BorderLayout.NORTH);

	    // Assemble the splitpane
	    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
	    		selector, inspector);
	    splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerLocation(200);
	    getPanel().add(splitPane);
	    /*	    
		    //Provide minimum sizes for the two components in the split pane
		    Dimension minimumSize = new Dimension(100, 50);
		    //options.setMinimumSize(minimumSize);
		    //pictureScrollPane.setMinimumSize(minimumSize);
	     */

	    // Get the icons.
	    excludeIcon = getIcon("resources/action_stop.gif","Exclude");
	    fileIcon = getIcon("resources/page.gif","File");
	    folderIcon = getIcon("resources/folder.gif","Folder");
	    excludeHiddenIcon = getIcon("resources/action_stop_hidden.gif","Exclude Hidden");
	    fileHiddenIcon = getIcon("resources/page_hidden.gif","File Hidden");
	    folderHiddenIcon = getIcon("resources/folder_hidden.gif","Folder Hidden");


	}
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected WizardStage getNextStage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * The following code was shamelessly lifted from the Duke Data Accessioner code.
	 * (BSD-style licence)
	 */
    private boolean displaySize = false;
    private boolean displayLastModified = false;
    private JScrollPane treeSP;
    private Icon excludeIcon,  fileIcon,  folderIcon,  excludeHiddenIcon,  fileHiddenIcon,  folderHiddenIcon;
    private JTextField diskName;
    private static DateFormat dateFormat =
        new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon getIcon(String path,
                                               String description) {
        java.net.URL imgURL = BagMan.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

	public class FileSystemModel implements TreeModel, Serializable {

		private static final long serialVersionUID = -970162809370986907L;
		
		FileWrapper root;
        private Vector<TreeModelListener> treeModelListeners =
                new Vector<TreeModelListener>();

        public FileSystemModel() {
            this(System.getProperty("user.home"));
        }

        public FileSystemModel(String startPath) {
            root = new FileWrapper(startPath);
        }

        public FileWrapper getRoot() {
            return root;
        }

        public FileWrapper getChild(Object parent, int index) {
            FileWrapper directory = (FileWrapper) parent;
            return directory.listMetadata()[index];
        }

        public int getChildCount(Object parent) {
            FileWrapper fileSysEntity = (FileWrapper) parent;
            if (fileSysEntity.isDirectory()) {
                return fileSysEntity.listMetadata().length;
            } else {
                return 0;
            }
        }

        public boolean isLeaf(Object node) {
            return ((FileWrapper) node).isFile();
        }

        public void valueForPathChanged(TreePath path, Object newValue) {
        //Do nothing?
        }

        public int getIndexOfChild(Object parent, Object child) {
            FileWrapper directory = (FileWrapper) parent;
            FileWrapper fileSysEntity = (FileWrapper) child;
            FileWrapper[] children = directory.listMetadata();

            for (int i = 0; i < children.length; ++i) {
                if (fileSysEntity.getName().equals(children[i].getName())) {
                    return i;
                }
            }
            return -1;
        }

        public void addTreeModelListener(TreeModelListener l) {
            treeModelListeners.addElement(l);

        }

        /**
         * Removes a listener previously added with addTreeModelListener().
         */
        public void removeTreeModelListener(TreeModelListener l) {
            treeModelListeners.removeElement(l);
        }
    }

	private void createTree(File source) {
        fileTree = new JTree(new FileSystemModel(source.getAbsolutePath())) {

			@Override
            public String convertValueToText(Object value, boolean selected,
                    boolean expanded, boolean leaf, int row, boolean hasFocus) {
                String returned = ((FileWrapper) value).getName();
                if (leaf) {
                    if (displaySize) {
                        returned +=
                                " (" +
                                prettySize(((FileWrapper) value).length()) + ")";
                    }
					if (displayLastModified) {
                        returned +=
                                " [" +
                                dateFormat.format(new Date(((FileWrapper) value).lastModified())) + "]";
                    }
                }
                return returned;
            }
        };
        fileTree.setLargeModel(true);
        fileTree.setRootVisible(true);
        fileTree.setShowsRootHandles(true);
        fileTree.putClientProperty("JTree.lineStyle", "Angled");
        fileTree.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree,
                    Object value,
                    boolean sel,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus) {

                super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);

                FileWrapper nodeInfo = (FileWrapper) value;

                //Set ToolTip
                if (nodeInfo.isDirectory()) {
                    setToolTipText(null); //no tool tip
                } else {
                    setToolTipText("Size: " +
                            prettySize(nodeInfo.length()) +
                            ", Last Modified: " +
                            dateFormat.format(new Date(nodeInfo.lastModified())));
                }

                //Set Icon
                if (nodeInfo.isExcluded()) {
                    if (nodeInfo.isHidden()) {
                        setIcon(excludeHiddenIcon);
                    } else {
                        setIcon(excludeIcon);
                    }
                    setToolTipText("This file/directory is excluded.");
                } else if (nodeInfo.isHidden()) {
                    setIcon(nodeInfo.isDirectory() ? folderHiddenIcon : fileHiddenIcon);
                } else {
                    setIcon(nodeInfo.isDirectory() ? folderIcon : fileIcon);
                }

                return this;
            }
        });
        treeSP.setViewportView(fileTree);
        treeSP.repaint();
        // Get the disk name:
        String displayName = FileSystemView.getFileSystemView().
                getSystemDisplayName(source);
        displayName = displayName.replaceAll(" \\([A-Z]:\\)$", "");
        diskName.setText(displayName);
    }
	
    protected static String prettySize(Long size) {
        String prettySize = "";
        String[] measures = {"B", "KB", "MB", "GB", "TB", "EB", "ZB", "YB"};

        int power = measures.length - 1;
        //Cycle each measure starting with the smallest
        for (int i = 0; i < measures.length; i++) {
            //Test for best fit 
            if ((size / (Math.pow(1024, i))) < 1024) {
                power = i;
                break;
            }
        }
        DecimalFormat twoPlaces = new DecimalFormat("#,##0.##");
        Double newSize = (size / (Math.pow(1024, power)));
        prettySize = twoPlaces.format(newSize) + " " + measures[power];
        return prettySize;
    }
    

}
