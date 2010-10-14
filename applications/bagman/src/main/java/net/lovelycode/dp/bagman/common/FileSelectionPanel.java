/**
 * 
 */
package net.lovelycode.dp.bagman.common;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import net.lovelycode.dp.bagman.BagMan;
import net.lovelycode.dp.bagman.common.FileSelectionPanel.FileSystemModel;

/**
 * The following code was shamelessly lifted from the Duke Data Accessioner code.
 * (was under BSD-style licence)
 * 
 * TODO Add menu to control size and date show/hide.
 * TODO Allow items to be dragged onto the pane.
 * 
 * @author Andrew Jackson
 *
 */
public class FileSelectionPanel {

	private static Logger log = Logger.getLogger(FileSelectionPanel.class);

	/** */
	private static final long serialVersionUID = 512044814571412519L;

	/** */
	private JPanel panel;

	/* */
	private JTree fileTree;
	
	/** */
    private boolean displaySize = false;
    private boolean displayLastModified = false;
    private JScrollPane treeSP;
    private Icon excludeIcon,  fileIcon,  folderIcon,  excludeHiddenIcon,  fileHiddenIcon,  folderHiddenIcon;
    private static DateFormat dateFormat =
        new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	private JButton chooseButton;

	/** */
	protected JFrame parent;

	private FileSystemModel fsm;
    
	/**
	 */
	public FileSelectionPanel(final JFrame parent) {
		this.parent = parent;
	    // Get the icons.
	    excludeIcon = getIcon("resources/banned-16.png","Exclude");
	    fileIcon = getIcon("resources/page.gif","File");
	    folderIcon = getIcon("resources/folder.gif","Folder");
	    excludeHiddenIcon = getIcon("resources/banned-16-grey.png","Exclude Hidden");
	    fileHiddenIcon = getIcon("resources/page_hidden.gif","File Hidden");
	    folderHiddenIcon = getIcon("resources/folder_hidden.gif","Folder Hidden");

	    panel = new JPanel();
	    panel.setLayout( new BorderLayout() );
	    this.treeSP = new JScrollPane();
	    panel.add( this.treeSP, BorderLayout.CENTER );
	    JPanel tools = new JPanel();
	    panel.add(tools, BorderLayout.SOUTH);
	    tools.setLayout( new BorderLayout());
	    JPanel selectionButtons = new JPanel();
	    selectionButtons.setLayout(new GridLayout(1,2));
	    tools.add(selectionButtons, BorderLayout.WEST);
	    chooseButton = new JButton(this.getIcon("resources/plus-8.png", "Add files to this collection."));
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
			    int returnVal = chooser.showOpenDialog(parent);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.print("You chose to open this file:");
			       for( File f : chooser.getSelectedFiles() ) {
			        	System.out.print(" "+f);
			       }
			       System.out.println(" ");
			       fsm.addRoot(chooser.getSelectedFile());
			    }
			}});
	    // And the removal button
	    JButton minusButton = new JButton(this.getIcon("resources/minus-8.png", "Remove files from this collection.") );
	    selectionButtons.add(minusButton);
	    // Change icon when selecting rather than managing root folders.
	    minusButton.setIcon(excludeHiddenIcon);
	    // Toggle selection status.
	    minusButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toggleExcluded();
			}} );
	    
	    // Add add the configuration menu button
	    JButton configButton = new JButton("@");
	    tools.add(configButton, BorderLayout.EAST);

		// Create Source:
        fsm = new FileSystemModel();
	    createTree();
	}

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

    /**
     * @return
     */
    public FileWrapper getCollectionRoot() {
        return (FileWrapper) fileTree.getModel().getRoot();
    }

    /**
     * 
     * @author Andrew Jackson
     *
     */
	public class FileSystemModel implements TreeModel, Serializable {

		private static final long serialVersionUID = -970162809370986907L;
		
		FileWrapper root;
        private Vector<TreeModelListener> treeModelListeners =
                new Vector<TreeModelListener>();

        public FileSystemModel() {
            this(System.getProperty("user.home"));
        }

        public void addRoot(File selectedFile) {
        	this.root = new FileWrapper( selectedFile );
        	createTree();
		}

		public void removeRoot() {
			root = null;
        	createTree();
		}
		
		public FileSystemModel(String startPath) {
            root = new FileWrapper(startPath);
        }
        
        public FileSystemModel(File startFile) {
        	if( startFile != null ) {
            	root = new FileWrapper(startFile.getAbsolutePath());
        	}
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

	/**
	 * @param source
	 */
	private void createTree() {
        // Create Tree
        fileTree = new JTree(fsm) {

			/** */
			private static final long serialVersionUID = -7186397845778237235L;

			@Override
            public String convertValueToText(Object value, boolean selected,
                    boolean expanded, boolean leaf, int row, boolean hasFocus) {
				FileWrapper f = ((FileWrapper) value);
				// TODO replace this with a getNiceName function in the FileWrapper:
                String returned = f.getName();
                if( "".equals(returned)) returned = f.getAbsolutePath();
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

            /**
			 * 
			 */
			private static final long serialVersionUID = -1208838104381323762L;

			@Override
            public Component getTreeCellRendererComponent(
                    JTree tree,
                    Object value,
                    boolean sel,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus) {

                FileWrapper nodeInfo = (FileWrapper) value;

                // Set colour:
                if (nodeInfo.isExcluded()) {
                	this.setTextNonSelectionColor(Color.LIGHT_GRAY);
                } else {
                	this.setTextNonSelectionColor(Color.BLACK);
                }
                	
                super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);

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
        FileWrapper root = ((FileSystemModel)fileTree.getModel()).getRoot();
        if( root == null ) return;
        String displayName = FileSystemView.getFileSystemView().
                getSystemDisplayName(root);
        displayName = displayName.replaceAll(" \\([A-Z]:\\)$", "");
        //diskName.setText(displayName);
        root.showFileInfo();
	    while(root.getParentFile() != null ) {
	    	root = new FileWrapper( root.getParentFile().getAbsolutePath() );
	    	root.showFileInfo();
	    }
    }
	
	/**
	 */
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

    /**
     */
    public void toggleExcluded() {
        TreePath[] currentSelections = fileTree.getSelectionPaths();
        boolean removedRoot = false;
        for (TreePath currentSelection : currentSelections) {
            if (currentSelection != null) {
                FileWrapper currentNode =
                        (FileWrapper) currentSelection.getLastPathComponent();
                boolean exclude = ! currentNode.isExcluded();
                currentNode.setExcluded(exclude);
                if (exclude) {
                    fileTree.collapsePath(currentSelection);
                    if( currentSelection.getPathCount() == 1 ) {
                    	log.info("Removing the root...");
                    	removedRoot = true;
                    }
                }
            }
        }
        // Empty the display if a root is removed.
        if( removedRoot ) {
        	fsm.removeRoot();
        }
        fileTree.repaint();
        return;
    }

	public JPanel getPanel() {
		return panel;
	}

	public void openFileAdder() {
		chooseButton.doClick();
	}
}
