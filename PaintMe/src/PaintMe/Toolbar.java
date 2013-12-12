// Toolbar.java- A class that manages the data related to the toolbar used in
//               PaintDemo.java

package PaintMe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import PaintMe.PaintDemo.SelectionArea;
import PaintMe.ShapeData.SHAPE;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * A class that manages the data related to the toolbar used in PaintDemo.java
 * @author Dylan
 */
public class Toolbar {
    protected SelectionArea area;
    protected MenuBar menubar;
    protected Action setShapeToOval, setShapeToRect, setShapeToSquare,
                     setShapeToCircle, setShapeToPolygon, setShapeToText, 
                     setMoveToFront, setFilled, showColorChooser, 
                     enableShapeSelection, setShapeToPolyline, 
                     setShapeToFreeHand;
    protected ChangeListener setStrokeSize;
    protected JButton moveToFrontButton;
    protected JCheckBox filledCheckBox;
    protected JLabel mouseLocation;
    protected JSpinner strokeSpinner;
    
    /**
     * Creates a new Toolbar object
     * @param area_ The selection area that is being painted
     */
    public Toolbar(SelectionArea area_) {
        this.area = area_;
        
        //Create the actions shared by the toolbar and menu.
        setShapeToOval = new SetShapeToOval(  "Oval",
            new CustomIcons.OvalIcon(),
            "Draw ovals", 
            new Integer(KeyEvent.VK_O));
        setShapeToRect = new SetShapeToRect("Rectangle",
            new CustomIcons.RectangleIcon(),
            "Draw rectangles", 
            new Integer(KeyEvent.VK_R));
        setShapeToCircle = new SetShapeToCircle("Circle",
            new CustomIcons.CircleIcon(),
            "Draw circles", 
            new Integer(KeyEvent.VK_C));
        setShapeToSquare = new SetShapeToSquare("Square",
            new CustomIcons.SquareIcon(),
            "Draw squares", 
            new Integer(KeyEvent.VK_S));
        setShapeToPolygon = new SetShapeToPolygon("Polygon",
            new CustomIcons.PolygonIcon(),
            "Draw polygons",
            new Integer(KeyEvent.VK_P));
        setShapeToPolyline = new SetShapeToPolyline("Polyline",
            new CustomIcons.PolylineIcon(),
            "Draw polylines",
            new Integer(KeyEvent.VK_L));
        setShapeToText = new SetShapeToText("Text",
            new CustomIcons.TextIcon(),
            "Draw text", 
            new Integer(KeyEvent.VK_T));
        setShapeToFreeHand = new SetShapeToFreeHand("Free Hand",
            new CustomIcons.FreeHandIcon(),
            "Draw free hand", 
            new Integer(KeyEvent.VK_P));
        setMoveToFront = new SetMoveToFront("Move to front",
            new CustomIcons.MoveToFrontIcon(),
            "Move to front", 
            new Integer(KeyEvent.VK_M));
        setFilled =  new SetFilled( "Filled", null,
            "Fill/unfill shapes", 
            new Integer(KeyEvent.VK_F));
        showColorChooser = new ShowColorChooser("Color", 
            new CustomIcons.SwatchIcon(area.getSelectedColor()),
            "Drawing color", 
            KeyEvent.VK_C);
        enableShapeSelection = new EnableShapeSelection("Selection",
            new CustomIcons.SelectionIcon(),
            "Drag and drop",
            new Integer(KeyEvent.VK_K));
        setStrokeSize = new SetStrokeSize();
    }

    /**
     * Creates a new JToolBar with the necessary components
     * @return a new JToolBar
     */
    public JToolBar createToolBar() {        
        //Create the toolbar.
        JToolBar toolBar = new JToolBar();
        toolBar.setOrientation(JToolBar.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(110, 400));
        
        //Create shape Panel
        JPanel shapePanel = new JPanel();
        shapePanel.setBorder(BorderFactory.createTitledBorder("Drawing"));
        shapePanel.setLayout(new WrapLayout());
        shapePanel.setPreferredSize(new Dimension(110, 125));
        shapePanel.setMaximumSize(new Dimension(110, 125));
        
        //rectangle button
        JToggleButton rectButton = new JToggleButton(setShapeToRect);
        if (rectButton.getIcon() != null) {
            rectButton.setText(""); //an icon-only button
        }
        rectButton.setSelected(true);
        rectButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(rectButton);
              
        //oval button
        JToggleButton ovalButton = new JToggleButton(setShapeToOval);
        if (ovalButton.getIcon() != null) {
            ovalButton.setText(""); //an icon-only button
        }
        ovalButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(ovalButton);
        
        //polygon button
        JToggleButton polygonButton = new JToggleButton(setShapeToPolygon);
        if (polygonButton.getIcon() != null) {
            polygonButton.setText(""); //an icon-only button
        }
        polygonButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(polygonButton);
        
        //square button
        JToggleButton squareButton = new JToggleButton(setShapeToSquare);
        if (squareButton.getIcon() != null) {
            squareButton.setText(""); //an icon-only button
        }
        squareButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(squareButton);
          
        //circle button
        JToggleButton circleButton = new JToggleButton(setShapeToCircle);
        if (circleButton.getIcon() != null) {
            circleButton.setText(""); //an icon-only button
        }
        circleButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(circleButton);
        
        //polyline button
        JToggleButton polylineButton = new JToggleButton(setShapeToPolyline);
        if (polylineButton.getIcon() != null) {
            polylineButton.setText(""); //an icon-only button
        }
        polylineButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(polylineButton);
              
        //polyline button
        JToggleButton freeHandButton = new JToggleButton(setShapeToFreeHand);
        if (freeHandButton.getIcon() != null) {
            freeHandButton.setText(""); //an icon-only button
        }
        freeHandButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(freeHandButton);
        
        //text button
        JToggleButton textButton = new JToggleButton(setShapeToText);
        if (textButton.getIcon() != null) {
            textButton.setText(""); //an icon-only button
        }
        textButton.setPreferredSize(new Dimension(25, 25));
        shapePanel.add(textButton);
        
        shapePanel.add(Box.createRigidArea(new Dimension(25,0)));
        
        //Add shape panel
        toolBar.add(shapePanel);
                
        //Create tools panel
        JPanel toolsPanel = new JPanel();
        toolsPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
        toolsPanel.setLayout(new WrapLayout());
        toolsPanel.setMaximumSize(new Dimension(110, 65));
        toolsPanel.setPreferredSize(new Dimension(110, 65));
        
        //Selection button
        JToggleButton selectionButton = new JToggleButton(enableShapeSelection);
        if(selectionButton.getIcon() != null)
            selectionButton.setText("");
        selectionButton.setPreferredSize(new Dimension(25, 25));
        toolsPanel.add(selectionButton);
        
        //Move to front button
        moveToFrontButton = new JButton(setMoveToFront);
        if(moveToFrontButton.getIcon() != null)
            moveToFrontButton.setText("");
        moveToFrontButton.setEnabled(false);
        moveToFrontButton.setPreferredSize(new Dimension(25, 25));
        toolsPanel.add(moveToFrontButton);
        
        toolsPanel.add(Box.createRigidArea(new Dimension(25,0)));
        
        toolBar.add(toolsPanel);
        
        toolBar.addSeparator();
        
        //Group shape buttons
        ButtonGroup group3 = new ButtonGroup();
        group3.add(rectButton);
        group3.add(ovalButton);
        group3.add(squareButton);
        group3.add(circleButton);
        group3.add(polygonButton);
        group3.add(polylineButton);
        group3.add(freeHandButton);
        group3.add(textButton);
        group3.add(selectionButton);
                
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        propertiesPanel.setBorder(BorderFactory.createTitledBorder(
                "Properties"));
        propertiesPanel.setPreferredSize(new Dimension(110, 90));
        propertiesPanel.setMaximumSize(new Dimension(110, 90));
                
        //Filled label
        JLabel filledLabel = new JLabel("Filled:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        propertiesPanel.add(filledLabel, c);
        
        //Filled checkbox button
        filledCheckBox = new JCheckBox();
        filledCheckBox.addActionListener(setFilled);
        filledCheckBox.setSelected(true);
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        propertiesPanel.add(filledCheckBox, c);
        
        //Color swatch label
        JLabel colorLabel = new JLabel("Color:");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        propertiesPanel.add(colorLabel, c);
                
        //color swatch
        JButton swatchButton = new JButton(showColorChooser);
        if(swatchButton.getIcon() != null)
            swatchButton.setText("");
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        propertiesPanel.add(swatchButton, c);
        swatchButton.setPreferredSize(new Dimension(12, 12));
        swatchButton.setMaximumSize(swatchButton.getPreferredSize());
                        
        //stroke label
        JLabel strokeLabel = new JLabel("Stroke:");
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        propertiesPanel.add(strokeLabel, c);
        
        //Stroke dropdown
        strokeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 30, 5));
        strokeSpinner.addChangeListener(setStrokeSize);
        JFormattedTextField editor = 
            ((JFormattedTextField)
                ((JSpinner.DefaultEditor)strokeSpinner.getEditor())
                    .getTextField());
        editor.setHorizontalAlignment(JTextField.CENTER);
        strokeSpinner.setPreferredSize(new Dimension(35, 20));
        strokeSpinner.setMaximumSize(strokeSpinner.getPreferredSize());
        c.gridx = 2;
        c.gridy = 2;
        c.anchor = GridBagConstraints.CENTER;
        propertiesPanel.add(strokeSpinner, c);
        
        c.gridx = 1;
        c.gridy = 0;
        propertiesPanel.add(Box.createRigidArea(new Dimension(8, 0)), c);
        
        //Add properties panel
        toolBar.add(propertiesPanel);
        
        toolBar.add(Box.createVerticalGlue());
        
        JPanel mousePanel = new JPanel();
        mousePanel.setPreferredSize(new Dimension(110, 25));
        mousePanel.setMaximumSize(mousePanel.getPreferredSize());
        mousePanel.setLayout(new BoxLayout(mousePanel, BoxLayout.X_AXIS));
        mousePanel.setBorder(BorderFactory.createTitledBorder(""));
        
        mouseLocation = new JLabel();
        mouseLocation.setHorizontalAlignment(JTextField.CENTER);
        mousePanel.add(mouseLocation);
        
        toolBar.add(mousePanel);
                
        return toolBar;
    }
  
    /**
     * An action that sets the selection area's shape to an oval
     */
    public class SetShapeToOval extends AbstractAction {
        public SetShapeToOval(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Oval);  
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }

    /**
     * An action that sets the selection area's shape to a rectangle
     */
    public class SetShapeToRect extends AbstractAction {
        public SetShapeToRect(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Rectangle);  
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }
    
    /**
     * An action that sets the selection area's shape to a square
     */
    public class SetShapeToSquare extends AbstractAction {
        public SetShapeToSquare(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Square);  
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }
    
    /**
     * An action that sets the selection area's shape to a Polygon
     */
    public class SetShapeToPolygon extends AbstractAction {
        public SetShapeToPolygon(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Polygon);    
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }

    /**
     * An action that sets the selection area's shape to a Polygon
     */
    public class SetShapeToPolyline extends AbstractAction {
        public SetShapeToPolyline(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Polyline);    
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }
    
    /**
     * An action that sets the selection area's shape to a circle
     */
    public class SetShapeToCircle extends AbstractAction {
        public SetShapeToCircle(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Circle);          
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }
    
    /**
     * An action that sets the current shape to draw text
     */
    public class SetShapeToText extends AbstractAction {
        public SetShapeToText(String text, Icon icon, String desc, Integer mnemonic){
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.Text);    
            area.clearSelection();
            area.setDragDropSelected(false);
        }                
    }
    
    /**
     * An action that sets the selection area's shape to a Polygon
     */
    public class SetShapeToFreeHand extends AbstractAction {
        public SetShapeToFreeHand(String text, Icon icon, String desc, 
                              Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setSelectedShape(ShapeData.SHAPE.FreeHand);    
            area.clearSelection();
            area.setDragDropSelected(false);
        }
    }
    
    /**
     * An action that moves the selected shape to the front
     */
    public class SetMoveToFront extends AbstractAction {
        public SetMoveToFront(String text, Icon icon, String desc, Integer mnemonic){
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < area.shapes.size(); i++)
            {
                if(area.shapes.get(i).equals(area.shapeBeingMoved)){
                    area.shapes.remove(i);
                    area.shapes.add(area.shapeBeingMoved);
                }
            }
            area.repaint();
        }                
    }
    
    /**
     * An action that sets the selection area's shapes to be filled
     */
    public class SetFilled extends AbstractAction {
        public SetFilled(String text, Icon icon, String desc, Integer mnemonic){
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            if(filledCheckBox.isSelected())
                area.setFilled(true);
            else
                area.setFilled(false);
        }
    }
    
    /**
     * An action that sets the the stroke size
     */
    public class SetStrokeSize implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            area.setStrokeSize(Integer.parseInt(
                    strokeSpinner.getValue().toString()));
        }
    }
    
    /**
     * An action that shows a color chooser, and sets the selection area's
     * color to the chosen color.
     */
    public class ShowColorChooser extends AbstractAction {
        CustomIcons.SwatchIcon icon;
        
        public ShowColorChooser(String text, Icon icon_, String desc, Integer mnemonic){
            super(text, icon_);
            icon = (CustomIcons.SwatchIcon)icon_;
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        
        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(
                     area,
                     "Choose Drawing Color",
                     area.getSelectedColor());
            
            //Sets to current color in case user cancels out of 
                        
            if(newColor == null)
                newColor = area.getSelectedColor();
            
            area.setSelectedColor(newColor);
            icon.setColor(newColor);
            
            if(menubar != null)
                menubar.setSelectedColor(newColor);
        }
        
        public void setIconColor(Color color){
            icon.setColor(color);
        }
    }
    
    /**
     * An action that sets the drag and drop selection mode to true
     */
    public class EnableShapeSelection extends AbstractAction {
        public EnableShapeSelection(String text, Icon icon, String desc, Integer mnemonic){
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            area.setDragDropSelected(true);
            area.setSelectedShape(SHAPE.None);
        }
    }
    
    /**
     * Sets the menubar used in the GUI (eliminates circular reference)
     * @param menubar_ the menubar used in the GUI
     */
    public void setMenuBar(MenuBar menubar_){
        menubar = menubar_;
    }
    
    /**
     * Sets the color of the swatch icon
     * @param color the new color
     */
    public void setSwatchColor(Color color){
        ((ShowColorChooser)showColorChooser).setIconColor(color);
    }
    
    /**
     * Enables/Disables the move to front button
     * @param enabled 
     */
    public void setMoveToFrontEnabled(boolean enabled) {
        moveToFrontButton.setEnabled(enabled);
    }
    
    /**
     * Updates the text for the mouse location
     * @param point the current mouse position
     */
    public void updateMouseLocation(Point point){
        if(point == null)
            mouseLocation.setText("");
        else{
            mouseLocation.setText(
                    String.format("<html><pre>(%3d, %3d)</pre>", 
                            point.x, point.y));
        }
    }
}