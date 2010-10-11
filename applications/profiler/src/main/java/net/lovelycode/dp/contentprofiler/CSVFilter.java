/**
 * 
 */
package net.lovelycode.dp.contentprofiler;

import java.io.File;

import javax.swing.filechooser.FileFilter;

class CSVFilter extends FileFilter {

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File f) {
        if( f.isDirectory() ) {
            return false;
        }
        String extension = getExtension(f);
        if( extension != null ) {
            if( extension.equals("csv")) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription() {
        return "Just CSV files.";
    }
    
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}