//WindowClosingListener.java- A window listener that interrupts the window 
//                            closing event and displays an option dialog that 
//                            asks to save the user's work, and a message dialog
//                            with the number of shapes drawn to the provided 
//                            SelectionArea.

package PaintMe;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * A window listener that interrupts the window closing event and
 * displays an option dialog that asks to save the user's work, and a 
 * message dialog with the number of shapes drawn to the provided 
 * SelectionArea.
 * 
 * @author Dylan
 */
public class WindowClosingListener extends WindowAdapter
{
   private PaintDemo.SelectionArea area;

   /**
    * Creates a new MyWindowListener
    * @param area_ the SelectionArea
    */
   public WindowClosingListener(PaintDemo.SelectionArea area_)
   {
       area = area_;
   }

   /**
    * Handles the the event fired when the window is closed by displaying
    * a message dialog with the number of shapes drawn to the provided 
    * SelectionArea.
    * @param e 
    */
   @Override
   public void windowClosing(WindowEvent e)
   {
       if(area.shapes.size() > 0){
            int n = JOptionPane.showConfirmDialog(
                area,
                "Would you like to save your work?", 
                null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if(n == JOptionPane.YES_OPTION)
                handleSave();
       }
   }
   
   /**
    * Displays the save file chooser and handles the saving process
    */
   public void handleSave()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(area);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new
                        BufferedOutputStream(new FileOutputStream(file)));
                out.writeObject(area.getShapes());
            }
            catch(FileNotFoundException ex) {
                Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex){
                Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
    }
}