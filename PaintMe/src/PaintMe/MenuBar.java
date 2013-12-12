// MenuBar.Java- Helps create the menu bar for this project. Includes file,
//               shape, color, and help menus.

package PaintMe;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import PaintMe.PaintDemo.SelectionArea;

/**
 * Helps create the menu bar for this project. Includes file,
 * shape, color, and help menus.
 * @author Dylan
 */
public class MenuBar {
    SelectionArea area;
    Toolbar toolbar;
    Clipboard clipboard;
    
    private static JRadioButtonMenuItem btnRed, btnGreen, btnBlue, btnOther;
    private static JRadioButtonMenuItem btnRect, btnOval;
    private static JRadioButtonMenuItem btnFilled, btnUnfilled;
    private static JMenuItem load, save;
    private static JMenuItem cut, copy, paste, clear;
    
    private static String IS_FILLED = "Filled?";
    private static String FILLED = "Filled";
    private static String UNFILLED = "Unilled";
    private static String RED = "Red";
    private static String GREEN = "Green";
    private static String BLUE = "Blue";
    private static String OTHER = "Other...";
    private static String RECTANGLE = "Rectangle";
    private static String OVAL = "Oval";
    
    /**
     * Creates a MenuBar object
     * @param area_ the selection area to draw on
     * @param toolbar_ the toolbar used in the GUI
     */
    public MenuBar(SelectionArea area_, Toolbar toolbar_)
    {
        area = area_;
        toolbar = toolbar_;
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }
    
    /**
     * Creates a new JMenuBar object with file, shape, color, and help menus
     * @return a static instance of a JMenuBar
     */
    public JMenuBar createMenuBar()
    {          
        //Create menubar
        JMenuBar menu = new JMenuBar();
        
        //Create top-level menus
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu shape = new JMenu("Shape");
        JMenu color = new JMenu("Color");
        JMenu help = new JMenu("Help");
        
        //Set tool-tip text for each menu
        file.setToolTipText("Standard file actions");
        edit.setToolTipText("Edit operations");
        shape.setToolTipText("Current shape");
        color.setToolTipText("Current color");
        help.setToolTipText("Documentation");
        
        //Add the menus to the menubar
        menu.add(file);
        menu.add(edit);
        menu.add(shape);
        menu.add(color);
        menu.add(Box.createHorizontalGlue());
        menu.add(help);
        
        //Create menu items for the file menu
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        JMenuItem print = new JMenuItem("Print");
        JMenuItem exit = new JMenuItem("Exit");
        
        //Set mnemonics and accelerators
        load.setMnemonic('L');
        save.setMnemonic('S');
        print.setMnemonic('P');
        exit.setMnemonic('x');
        exit.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        
        //Add action listener to print event
        print.addActionListener(new PrintListener(area));
        
        //Add action listener to exit event
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(area.shapes.size() > 0)
                {
                    int n = JOptionPane.showConfirmDialog(
                            area,
                            "Would you like to save your work?", 
                            null,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                   if(n == JOptionPane.YES_OPTION)
                       handleSave();
               }
               System.exit(0);
            }
        });
        
        //Add action listener to load and save events
        LoadAndSaveListener ls = new LoadAndSaveListener();
        load.addActionListener(ls);
        save.addActionListener(ls);
        
        //Add menu items to file menu
        file.add(load);
        file.add(save);
        file.add(print);
        file.addSeparator();
        file.add(exit);
        
        //Instantiate edit menu items
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        clear = new JMenuItem("Clear");
        
        //Set mnemonics
        cut.setMnemonic('u');
        copy.setMnemonic('o');
        paste.setMnemonic('P');
        clear.setMnemonic('C');
        cut.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        
        //Add action listeners
        cut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeData shapeCopy = new ShapeData(area.shapeBeingMoved);
                ShapeDataClipboardOwner selection = 
                        new ShapeDataClipboardOwner(shapeCopy);
                clipboard.setContents(selection, selection);
                area.cutCurrentShape();
                paste.setEnabled(true);
                cut.setEnabled(false);
                copy.setEnabled(false);
            }            
        });
        
        copy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeData copy = new ShapeData(area.shapeBeingMoved);
                ShapeDataClipboardOwner selection = 
                        new ShapeDataClipboardOwner(copy);
                clipboard.setContents(selection, selection);
                paste.setEnabled(true);
            }            
        });
        
        paste.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeData shape = null;
                Transferable contents = clipboard.getContents(null);
                boolean hasTransferableShape = (contents != null) &&
                        contents.isDataFlavorSupported(ShapeDataClipboardOwner.dataFlavor);
                if(hasTransferableShape)
                {   
                    try {
                        ShapeData copy = (ShapeData)contents
                                .getTransferData(ShapeDataClipboardOwner
                                    .dataFlavor);
                        shape = new ShapeData(copy);
                    }
                    catch (UnsupportedFlavorException ex){
                      System.out.println(ex);
                      ex.printStackTrace();
                    }
                    catch (IOException ex){
                      System.out.println(ex);
                      ex.printStackTrace();
                    }
                }
                area.pasteNewShape(shape);
            }            
        });
        
        clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                area.clear();                
            }
        });
        
                
        //Initially enable/disable cut, copy, paste functions 
        cut.setEnabled(false);
        copy.setEnabled(false);
        
        Transferable contents = clipboard.getContents(null);
                boolean hasTransferableShape = (contents != null) &&
                        contents.isDataFlavorSupported(ShapeDataClipboardOwner.dataFlavor);
        if(hasTransferableShape)
            paste.setEnabled(true);
        else
            paste.setEnabled(false);
        
        //Add menuitems to edit menu
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.addSeparator();
        edit.add(clear);
        
        //Instantiate shape menu items
        btnRect = new JRadioButtonMenuItem(RECTANGLE);
        btnOval = new JRadioButtonMenuItem(OVAL);
        
        //Set mnemonics
        btnRect.setMnemonic('R');
        btnOval.setMnemonic('O');
        
        //Add menuitems to shape menu
        shape.add(btnRect);
        shape.add(btnOval);
        
        //Group shape selection buttons
        ButtonGroup group1 = new ButtonGroup();
        btnRect.setSelected(true);
        group1.add(btnRect);
        group1.add(btnOval);
        
        shape.addSeparator();
        
        //Create submenu for Filled attribute
        JMenu filled = new JMenu(IS_FILLED);
        filled.setMnemonic('F');
        shape.add(filled);
        
        //Create menu items for filled submenu
        btnFilled = new JRadioButtonMenuItem(FILLED);
        btnUnfilled = new JRadioButtonMenuItem(UNFILLED);
        
        //set mnemonics
        btnFilled.setMnemonic('F');
        btnUnfilled.setMnemonic('U');
        
        //Group filled/unfilled selection buttons
        ButtonGroup group3 = new ButtonGroup();
        btnFilled.setSelected(true);
        group3.add(btnFilled);
        group3.add(btnUnfilled);
        
        //Add filled buttons to submenu
        filled.add(btnFilled);
        filled.add(btnUnfilled);
        
        //Instantiate color buttons
        btnRed = new JRadioButtonMenuItem(new SetColorToRed());
        btnRed.setText(RED);
        btnGreen = new JRadioButtonMenuItem(new SetColorToGreen());
        btnGreen.setText(GREEN);
        btnBlue = new JRadioButtonMenuItem(new SetColorToBlue());
        btnBlue.setText(BLUE);
        btnOther = new JRadioButtonMenuItem(new ShowColorChooser());
        btnOther.setText(OTHER);
        
        //Set mnemonics
        btnRed.setMnemonic('R');
        btnGreen.setMnemonic('G');
        btnBlue.setMnemonic('B');
        btnOther.setMnemonic('O');
        
        //Add color buttons to color menu
        color.add(btnRed);
        color.add(btnGreen);
        color.add(btnBlue);
        color.add(btnOther);
        
        //Group color selection buttons
        ButtonGroup group2 = new ButtonGroup();
        btnRed.setSelected(true);
        group2.add(btnRed);
        group2.add(btnGreen);
        group2.add(btnBlue);
        group2.add(btnOther);
        
        //Add help menuitem to the help menu
        JMenuItem subHelp = new JMenuItem("Help");
        subHelp.setMnemonic('H');
        subHelp.setToolTipText("Click here for help.");
        help.add(subHelp);
        
        return menu;
    }
    
    /**
     * Sets the color to red by setting the area's color, menu radio button,
     * and toolbar swatch color.
     */
    private class SetColorToRed extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            area.setSelectedColor(Color.RED);
            btnRed.setSelected(true);
            toolbar.setSwatchColor(Color.RED);
        }
    }
    
    /**
     * Sets the color to green by setting the area's color, menu radio button,
     * and toolbar swatch color.
     */
    private class SetColorToGreen extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            area.setSelectedColor(Color.GREEN);
            btnGreen.setSelected(true);
            toolbar.setSwatchColor(Color.green);
        }
    }
    
    /**
     * Sets the color to blue by setting the area's color, menu radio button,
     * and toolbar swatch color.
     */
    private class SetColorToBlue extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            area.setSelectedColor(Color.BLUE);
            btnBlue.setSelected(true);
            toolbar.setSwatchColor(Color.blue);
        }
    }
    
    /**
     * Displays the color chooser and sets the area's color, menu radio button,
     * and toolbar swatch colors appropriately.
     */
    private class ShowColorChooser extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(
                     area,
                     "Choose Drawing Color",
                     area.getSelectedColor());
            
            //In case user cancels out of color chooser dialog, set to current
            //color
            if(newColor == null)
                newColor = area.getSelectedColor();
            
            area.setSelectedColor(newColor);
            btnOther.setSelected(true);
            toolbar.setSwatchColor(newColor);
        }
    }
    
    /**
     * Sets the menu's color radio button
     * @param color the current color
     */
    public void setSelectedColor(Color color)
    {
        if(color.equals(Color.RED))
            btnRed.setSelected(true);
        else if(color.equals(Color.BLUE))
            btnBlue.setSelected(true);
        else if(color.equals(Color.GREEN))
            btnGreen.setSelected(true);
        else
            btnOther.setSelected(true);
    }
    
    /**
     * Enables/disables the cut menu item
     * @param selected enable/disable flag
     */
    public void setCutEnabled(boolean selected)
    {
        cut.setEnabled(selected);
    }
    
    /**
     * Enables/disables the copy menu item
     * @param selected enable/disable flag
     */
    public void setCopyEnabled(boolean selected)
    {
        copy.setEnabled(selected);
    }
    
    /**
     * Enables/disables the paste menu item
     * @param selected enable/disable flag
     */
    public void setPasteEnabled(boolean selected)
    {
        paste.setEnabled(selected);
    }
    
    /**
     * Saves or loads files when this event is fired.
     */
    private class LoadAndSaveListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == save)
                handleSave();
            else if(e.getSource() == load)
                handleLoad();
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

    /**
     * Displays the load file chooser and handles the loading process
     */
    public void handleLoad()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(area);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new
                        BufferedInputStream(new FileInputStream(file)));

                area.setShapes((ArrayList<ShapeData>)in.readObject());
            }
            catch(ClassNotFoundException ex) {
                Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(FileNotFoundException ex) {
                Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IOException ex){
                Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
    }
}
