// PaintDemo.java - Demonstrates cut, copy, and pasting.

// Name: Dylan Miller
// Class: CS 5551
// Assignment: Project
// Date: 12/12/13
//

package PaintMe;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import PaintMe.ShapeData.SHAPE;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Creates a GUI that allows the user to draw shapes, and
 * provides inputs for the user to choose their color (red, green,
 * or blue) and whether they're filled or unfilled.
 * @author Dylan
 */
public class PaintDemo {  
    //String constants
    private static String TITLE = "PaintMe";
    private static MenuBar menuBar;
    private static Toolbar toolbar;
    
    /**
     * Sets up UI for this application, adding the neccessary
     * buttons and related components.
     * @param container the JFrame's content pane
     */
    private void buildUI(Container container) {                
        //Initialize panels and set layouts and borders
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        //Add selection area to container
        SelectionArea area = new SelectionArea();
        c.gridx = 1;
        c.gridy = 0;
        container.add(area, c);
        
        toolbar = new Toolbar(area);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        container.add(toolbar.createToolBar(), c);
        
        //Get parent JFrame
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(container);
        
        //Add window listener
        WindowClosingListener wl = new WindowClosingListener(area);
        frame.addWindowListener(wl);
        
        //Add menu bar
        menuBar = new MenuBar(area, toolbar);
        toolbar.setMenuBar(menuBar);
        frame.setJMenuBar(menuBar.createMenuBar());        
    }    
    
    /**
     * Create the GUI and show it.  For thread safety, 
     * this method should be invoked from the 
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame(TITLE);        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Set up the content pane.
        PaintDemo controller = new PaintDemo();
        controller.buildUI(frame.getContentPane());
        
        //Display the window.
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        
    }

    /**
     * The main method for this class.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
    
    /**
     * Creates the panel where the shapes are drawn.
     */
    public class SelectionArea extends JPanel implements Serializable {
        Rectangle currentRect = null;
        Rectangle rectToDraw = null;
        Rectangle previousRectDrawn = new Rectangle();
        ArrayList<ShapeData> shapes = new ArrayList<ShapeData>(100);
        private SHAPE selectedShape;
        private Color selectedColor;
        private boolean isFilled;
        private boolean dragDropIsSelected;
        public ShapeData shapeBeingMoved;
        private boolean shapeIsSelected;
        private String stringToBeDrawn;
        boolean isPolygonBeingDrawn;
        int polygonIndex;
        boolean isDrawingFreeHand;
        int freeHandIndex;
        int strokeSize;
        
        /**
         * Sets up the SelectionArea by adding listeners and
         * attributes.
         */
        public SelectionArea() {
            setOpaque(true);
            setBackground(Color.white);
            this.setMinimumSize(new Dimension(550, 400)); 
            this.setPreferredSize(new Dimension(550, 400));
            
            selectedShape = SHAPE.Rectangle;
            selectedColor = Color.RED;
            dragDropIsSelected = false;
            shapeBeingMoved = null;
            isFilled = true;
            shapeIsSelected = false;
            isPolygonBeingDrawn = false;
            polygonIndex = -1;
            isDrawingFreeHand = false;
            freeHandIndex = -1;
            strokeSize = 5;
            
            //Add mouse listeners
            DrawingListener myListener = new DrawingListener();
            addMouseListener(myListener);
            addMouseMotionListener(myListener);
            
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("ESCAPE"),
                            "cancelCurrentAction");
            getActionMap().put("cancelCurrentAction", 
                new AbstractAction(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(isPolygonBeingDrawn)
                        {
                            isPolygonBeingDrawn = false;
                            polygonIndex = -1;
                            repaint();
                        }
                }
            });
        }
            
        /**
         * Executes when repaint is called
         * @param g the graphics object for this class
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //paints the background
            Graphics2D g2 = (Graphics2D)g;
            
            //Paints all previous shapes
            for(int i = 0; i < shapes.size(); i++)
                drawShape(g2, shapes.get(i));
            
            //If currentRect exists, paint a shape on top.
            if (currentRect != null) {   
                if(!selectedShape.equals(SHAPE.Text))
                {
                    drawShape(g2, new ShapeData(selectedShape, 
                                                rectToDraw, 
                                                selectedColor, 
                                                isFilled,
                                                strokeSize));
                }
            }
        }
        
        /**
         * A generic method that draws a shape to the given graphics
         * object.
         * @param g the graphics object for this class.
         * @param shapeData contains the information needed to draw
         * the shape correctly.
         */
        private void drawShape(Graphics2D g2, ShapeData shapeData)
        {            
            if(shapeData.shape != SHAPE.None){
                g2.setColor(shapeData.color);
                g2.setPaintMode();
                g2.setStroke(new BasicStroke(shapeData.strokeSize));
                                
                switch(shapeData.shape){
                    case Square:
                        setEqualWidthAndHeightIfNotSet(shapeData);
                    case Rectangle:
                        drawRect(g2, shapeData);
                        break;
                    case Circle:
                        setEqualWidthAndHeightIfNotSet(shapeData);
                    case Oval:
                        drawOval(g2, shapeData);
                        break;
                    case Polygon:
                        drawPolygon(g2, shapeData);
                        break;
                    case Polyline:
                        drawPolyline(g2, shapeData);
                        break;
                    case FreeHand:
                        drawFreeHand(g2, shapeData);
                        break;
                    case Text:
                        drawText(g2, shapeData);
                        break;
                }
                
                drawBorderForSelectedShape(g2, shapeData);
            }
        }
        
        /**
         * Sets the width and height equal to each other if they aren't already
         * equal
         * @param shapeData the shape to modify
         */
        private void setEqualWidthAndHeightIfNotSet(ShapeData shapeData){
            shapeData.boundingRect.height = 
                        shapeData.boundingRect.width;
            repaint();
        }
    
        /**
         * Draws a rectangle based on the the given shapeData.
         * @param g the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.Rectangle.
         */
        private void drawRect(Graphics2D g2, ShapeData shapeData)
        {
            if(shapeData.isFilled) {
                g2.fill(new Rectangle2D.Double(
                           shapeData.boundingRect.x, 
                           shapeData.boundingRect.y, 
                           shapeData.boundingRect.width - 1, 
                           shapeData.boundingRect.height - 1));
            }
            else
            {
                g2.draw(new Rectangle2D.Double(
                           shapeData.boundingRect.x, 
                           shapeData.boundingRect.y, 
                           shapeData.boundingRect.width - 1, 
                           shapeData.boundingRect.height - 1));
            }
        }
        
        /**
         * Draws an oval based on the the given shapeData.
         * @param g the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.Oval.
         */
        private void drawOval(Graphics2D g2, ShapeData shapeData)
        {
            if(shapeData.isFilled)
                g2.fillOval(shapeData.boundingRect.x, 
                           shapeData.boundingRect.y, 
                           shapeData.boundingRect.width - 1, 
                           shapeData.boundingRect.height - 1);
            else
                g2.drawOval(shapeData.boundingRect.x, 
                           shapeData.boundingRect.y, 
                           shapeData.boundingRect.width - 1, 
                           shapeData.boundingRect.height - 1);
            
        }
        
        /**
         * Draws a polygon based on the given shape data
         * @param g2 the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.Polygon
         */
        private void drawPolygon(Graphics2D g2, ShapeData shapeData)
        {            
            if(isPolygonBeingDrawn && 
                    shapes.get(polygonIndex).equals(shapeData)){
                Path2D path = new Path2D.Double();
                for(int i = 0; i < shapeData.getVertices().size(); i++){
                    if(i == 0)
                        path.moveTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                    else
                        path.lineTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                }
                path.lineTo(getMousePosition().x, getMousePosition().y);
                g2.draw(path);
            }
            else if(shapeData.getVertices() != null){
                int[] polyX =  new int[shapeData.getVertices().size()];
                int[] polyY =  new int[shapeData.getVertices().size()];

                for(int i = 0; i < shapeData.getVertices().size(); i++){
                    polyX[i] = shapeData.getVertices().get(i).x;
                    polyY[i] = shapeData.getVertices().get(i).y;
                }
                if(shapeData.isFilled)
                    g2.fill(new Polygon(polyX, polyY, polyX.length));
                else
                    g2.draw(new Polygon(polyX, polyY, polyX.length));
            }
        }
        
        /**
         * Draws a polyline based on the given shape data
         * @param g2 the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.Polyline
         */
        private void drawPolyline(Graphics2D g2, ShapeData shapeData)
        {            
            if(shapeData.getVertices() != null)
            {
                Path2D path = new Path2D.Double();
                for(int i = 0; i < shapeData.getVertices().size(); i++){
                    if(i == 0)
                        path.moveTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                    else
                        path.lineTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                }

                if(isPolygonBeingDrawn && 
                        shapes.get(polygonIndex).equals(shapeData) &&
                        getMousePosition() != null){                
                    path.lineTo(getMousePosition().x, getMousePosition().y);
                }

                g2.draw(path);
            }
        }
        
        /**
         * Draws a freehand shape based on the given shape data
         * @param g2 the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.FreeHand
         */
        private void drawFreeHand(Graphics2D g2, ShapeData shapeData)
        {      
            if(shapeData.getVertices() != null)
            {
                Path2D path = new Path2D.Double();
                for(int i = 0; i < shapeData.getVertices().size(); i++){
                    if(i == 0)
                        path.moveTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                    else
                        path.lineTo(shapeData.getVertices().get(i).x, 
                                    shapeData.getVertices().get(i).y);
                }
                g2.draw(path);
            }
        }
        
        /**
         * Draws text on the given shape data
         * @param g2 the current graphics object
         * @param shapeData a shapeData object with a 'shape' data member
         * equal to SHAPE.Text
         */
        private void drawText(Graphics g, ShapeData shapeData)
        {
            Graphics2D g2 = (Graphics2D)g;
            FontMetrics fm = g2.getFontMetrics();
            int width = fm.getStringBounds(shapeData.text, g2).getBounds().width;
            int height = fm.getStringBounds(shapeData.text, g2).getBounds().height;
            shapeData.boundingRect.setBounds(
                    shapeData.boundingRect.x,
                    shapeData.boundingRect.y,
                    width,
                    height);
            g2.drawString(shapeData.text, 
                          shapeData.boundingRect.x, 
                          shapeData.boundingRect.y + height);
        }
        
        /**
         * Draws the appropriate border for the current shape
         * @param g2 the current graphics object
         * @param shapeData the shape descriptor
         */
        private void drawBorderForSelectedShape(Graphics2D g2, 
                                                ShapeData shapeData){
            if(shapeBeingMoved != null && shapeData.equals(shapeBeingMoved))
            {
                float dash1[] = {15.0f, 5.0f};
                BasicStroke dashed =
                    new BasicStroke(2.5f,
                                    BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER,
                                    10.0f, dash1, 0.0f);
                Stroke originalStroke = g2.getStroke();
                Color originalColor = g2.getColor();
                
                g2.setStroke(dashed);
                g2.setColor(Color.blue);

                switch(shapeData.shape){
                    case Square:
                    case Rectangle:
                        g2.draw(new Rectangle2D.Double(
                            shapeData.boundingRect.x, 
                            shapeData.boundingRect.y, 
                            shapeData.boundingRect.width - 1, 
                            shapeData.boundingRect.height - 1));
                        break;
                    case Circle:
                    case Oval:
                        g2.draw(new Ellipse2D.Double(
                            shapeData.boundingRect.x, 
                            shapeData.boundingRect.y, 
                            shapeData.boundingRect.width - 1, 
                            shapeData.boundingRect.height - 1));
                        break;
                    case Text:
                        g2.draw(new Rectangle2D.Double(
                            shapeData.boundingRect.x, 
                            shapeData.boundingRect.y + 5, 
                            shapeData.boundingRect.width - 1, 
                            shapeData.boundingRect.height - 1));
                        break;
                    case FreeHand:
                    case Polyline:
                        Path2D path = new Path2D.Double();
                        for(int i = 0; i < shapeData.getVertices().size(); i++){
                            if(i == 0)
                                path.moveTo(shapeData.getVertices().get(i).x, 
                                            shapeData.getVertices().get(i).y);
                            else
                                path.lineTo(shapeData.getVertices().get(i).x, 
                                            shapeData.getVertices().get(i).y);
                        }
                        g2.draw(path);
                        break;
                    case Polygon:
                        int[] polyX =  new int[shapeData.getVertices().size()];
                        int[] polyY =  new int[shapeData.getVertices().size()];

                        for(int i = 0; i < shapeData.getVertices().size(); i++){
                            polyX[i] = shapeData.getVertices().get(i).x;
                            polyY[i] = shapeData.getVertices().get(i).y;
                        }
                        g2.draw(new Polygon(polyX, polyY, polyX.length));
                        break;
                }
                g2.setColor(originalColor);
                g2.setStroke(originalStroke);
            }
        }
        
        /**
         * Sets the coordinates for the next drawable rectangle
         * @param compWidth the width
         * @param compHeight the height
         */
        private void updateDrawableRect(int compWidth, int compHeight) {
            int x = currentRect.x;
            int y = currentRect.y;
            int width = currentRect.width;
            int height = currentRect.height;
    
            //Make the width and height positive, if necessary.
            if (width < 0) {
                width = 0 - width;
                x = x - width + 1; 
                if (x < 0) {
                    width += x; 
                    x = 0;
                }
            }
            if (height < 0) {
                height = 0 - height;
                y = y - height + 1; 
                if (y < 0) {
                    height += y; 
                    y = 0;
                }
            }
    
            //The rectangle shouldn't extend past the drawing area.
            if ((x + width) > compWidth) {
                width = compWidth - x;
            }
            if ((y + height) > compHeight) {
                height = compHeight - y;
            }
          
            //Update rectToDraw after saving old value.
            if (rectToDraw != null) {
                previousRectDrawn.setBounds(
                            rectToDraw.x, rectToDraw.y, 
                            rectToDraw.width, rectToDraw.height);
                rectToDraw.setBounds(x, y, width, height);
            } else {
                rectToDraw = new Rectangle(x, y, width, height);
            }
        }
        
        /**
         * Gets the number of rectangles drawn to the selection area
         * @return the number of rectangles
         */
        public int getNumberOfRectangles()
        {
            int n = 0;
            for(int i = 0; i < shapes.size(); i++)
                if(shapes.get(i).shape == SHAPE.Rectangle)
                    n++;
            return n;
        }
        
        /**
         * Gets the number of ovals drawn to the selection area
         * @return the number of ovals
         */
        public int getNumberOfOvals()
        {
            int n = 0;
            for(int i = 0; i < shapes.size(); i++)
                if(shapes.get(i).shape == SHAPE.Oval)
                    n++;
            return n;
        }
        
        /**
         * Returns the selected shape set by an outside class e.g. The 
         * toolBar class
         * @param selectedShape_ the selected shape
         */
        public void setSelectedShape(SHAPE selectedShape_)
        {
            selectedShape = selectedShape_;
        }
        
        /**
         * Returns the fill selection set by an outside class e.g. The 
         * toolBar class
         * @param setFilled_ the fill selection
         */
        public void setFilled(boolean isFilled_)
        {
            isFilled = isFilled_;
        }
        
        public void setStrokeSize(int size){
            strokeSize = size;
        }
        
        /**
         * Returns the selected color set by an outside class e.g. The 
         * toolBar class
         * @param selectedColor_ the selected color
         */
        public void setSelectedColor(Color selectedColor_)
        {
            selectedColor = selectedColor_;
        }
        
        /**
         * Returns the currently selected color
         * @return 
         */
        public Color getSelectedColor(){
            return selectedColor;
        }
        
        /**
         * Gets the current list of shapes
         * @return the list of shapes
         */
        public ArrayList<ShapeData> getShapes(){
            return shapes;
        }
        
        /**
         * Sets the current list of shapes and repaints the screen
         * @param shapes_ the new list of shapes
         */
        public void setShapes(ArrayList<ShapeData> shapes_){
            shapes = shapes_;
            repaint();
        }
        
        /**
         * Sets the drag and drop mode
         * @param selected 
         */
        public void setDragDropSelected(boolean selected){
            dragDropIsSelected = selected;
        }
        
        /**
         * Removes the shape that is currently selected from the shapes array
         * and repaints the selection area
         */
        public void cutCurrentShape(){
            if(shapeBeingMoved != null)
            {
                shapes.remove(shapeBeingMoved);
                repaint();
            }
        }
        
        /**
         * Adds the shape passed in to the shapes array and sets the new shape
         * as selected
         * @param shape the new shape
         */
        public void pasteNewShape(ShapeData shape){
            if(shape != null){
                shapes.add(shape);
                shapeIsSelected = true;
                shapeBeingMoved = shape;
                toolbar.setMoveToFrontEnabled(true);
                
                menuBar.setCopyEnabled(true);
                menuBar.setCutEnabled(true);
                repaint();
            }
        }
        
        /**
         * Clears all shapes and repaints the area
         */
        public void clear(){
            shapes.clear();
            repaint();
        }
        
        /**
         * Clears the current selected item
         */
        public void clearSelection(){
            shapeIsSelected = false;
            shapeBeingMoved = null;
            toolbar.setMoveToFrontEnabled(false);
            repaint();
        }
        
        /**
         * A customized mouse listener that handles mousePressed,
         * mouseDragged, and mouseReleased events, by painting
         * the necessary components.
         */
        private class DrawingListener extends MouseInputAdapter {
            boolean isDragging = false;
            Point prevPoint;
                
            /**
             * Event fired when the mouse is pressed
             * @param e 
             */
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                currentRect = new Rectangle(x, y, 0, 0);
                
                switch(selectedShape)
                {
                    case FreeHand:
                        currentRect = null;
                        if(!isDrawingFreeHand) {
                            isDrawingFreeHand = true;
                            freeHandIndex = shapes.size();
                            shapes.add(new ShapeData(selectedShape, 
                                    new Rectangle(x, y, 1, 1),
                                    selectedColor, 
                                    isFilled, 
                                    new Point(x, y),
                                    strokeSize));
                        }
                        else{
                            ShapeData freeHand = shapes.get(freeHandIndex);
                            freeHand.getVertices().add(new Point(x, y));
                        }       
                        break;
                    case Polyline:
                    case Polygon:
                        currentRect = null;
                        //First point has been placed
                        if(!isPolygonBeingDrawn) {
                            isPolygonBeingDrawn = true;
                            polygonIndex = shapes.size();
                            shapes.add(new ShapeData(selectedShape, 
                                    new Rectangle(x, y, 1, 1),
                                    selectedColor, 
                                    isFilled, 
                                    new Point(x, y),
                                    strokeSize));
                        }
                        //Add new point to current polygon
                        else{
                            ShapeData polygon = shapes.get(polygonIndex);
                            polygon.getVertices().add(new Point(x, y));
                        }                        
                        break;
                    case Text:
                        isPolygonBeingDrawn = false;
                        polygonIndex = -1;
                        isDrawingFreeHand = false;
                        freeHandIndex = -1;
                        
                        stringToBeDrawn = (String)JOptionPane.showInputDialog(
                            SelectionArea.this, 
                            "Enter text below:", 
                            "Draw Text",
                            JOptionPane.PLAIN_MESSAGE);
                        if(stringToBeDrawn != null)
                        {
                            shapes.add(new ShapeData(selectedShape, currentRect,
                                                     selectedColor, isFilled, 
                                                     stringToBeDrawn,
                                                     strokeSize));
                        } 
                        updateDrawableRect(getWidth(), getHeight());
                        break;
                    default:
                        isPolygonBeingDrawn = false;
                        polygonIndex = -1;
                        isDrawingFreeHand = false;
                        freeHandIndex = -1;
                        
                        //Drag and drop
                        if(dragDropIsSelected)
                        {
                            for(int i = shapes.size() - 1; i >= 0; i--)
                            {
                                ShapeData shape = shapes.get(i);
                                if(shape.contains(x, y))
                                {
                                    isDragging = true;
                                    shapeIsSelected = true;
                                    shapeBeingMoved = shapes.get(i);
                                    toolbar.setMoveToFrontEnabled(true);

                                    menuBar.setCopyEnabled(true);
                                    menuBar.setCutEnabled(true);
                                    updateLocation(e);
                                    break;
                                }
                                else{
                                    clearSelection();

                                    isDragging = false;
                                    menuBar.setCopyEnabled(false);
                                    menuBar.setCutEnabled(false);
                                }
                            }
                        }
                        else{
                            toolbar.setMoveToFrontEnabled(false);
                        }
                        updateDrawableRect(getWidth(), getHeight());
                }
                
                repaint();
            }
    
            /**
             * Event fired when the mouse is dragged
             * @param e 
             */
            public void mouseDragged(MouseEvent e) {
                //Drag and drop
                if(dragDropIsSelected)
                {
                    if (isDragging)
                        updateLocation(e);
                }
                else if(isDrawingFreeHand){
                    ShapeData freeHand = shapes.get(freeHandIndex);
                    freeHand.getVertices().add(new Point(e.getX(), e.getY()));
                    repaint();
                }
                //Draw shape
                else if(!isPolygonBeingDrawn)
                    updateSize(e);
                
                toolbar.updateMouseLocation(e.getPoint());
            }
    
            /**
             * Event fired when the mouse is released
             * @param e 
             */
            public void mouseReleased(MouseEvent e) {
                //End shape drag and drop
                if(dragDropIsSelected)
                {
                    isDragging = false;
                    //shapeBeingMoved = null;
                }
                else if(isDrawingFreeHand){
                    isDrawingFreeHand = false;
                    freeHandIndex = -1;
                }
                //Finish drawing shape 
                else if(!isPolygonBeingDrawn)
                {
                    updateSize(e);
                    if(currentRect.width < 0)
                    {
                        currentRect.x += currentRect.width + 1;
                        currentRect.width *= -1;
                    }
                    if(currentRect.height < 0)
                    {
                        currentRect.y += currentRect.height + 1;
                        currentRect.height *= -1;
                    }
                    if(!selectedShape.equals(SHAPE.Text))
                    {
                        shapes.add(new ShapeData(selectedShape, currentRect,
                                                 selectedColor, isFilled,
                                                 strokeSize));
                    }
                    currentRect = null;
                }
                
                prevPoint = null;
            }
    
            /**
             * Applies the rubberbanding effect for drawing polygons
             * @param e the mouse event
             */
            public void mouseMoved(MouseEvent e){
                if(isPolygonBeingDrawn){
                    repaint();
                }
                else if(dragDropIsSelected){
                    for(int i = shapes.size() - 1; i >= 0; i--)
                    {
                        ShapeData shape = shapes.get(i);
                        if(shape.contains(e.getX(), e.getY()))
                        {
                            setCursor(Cursor
                                    .getPredefinedCursor(Cursor.MOVE_CURSOR));
                            break;
                        }
                        else{
                            setCursor(Cursor
                                    .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
                toolbar.updateMouseLocation(e.getPoint());
            }
            
            /**
             * Handles the mouse exited event
             * @param e 
             */
            public void mouseExited(MouseEvent e){
                toolbar.updateMouseLocation(null);
            }
            
            /* 
             * Update the size of the current rectangle
             * and call repaint.  Because currentRect
             * always has the same origin, translate it
             * if the width or height is negative.
             * 
             * For efficiency (though
             * that isn't an issue for this program),
             * specify the painting region using arguments
             * to the repaint() call.
             * 
             */
            void updateSize(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                currentRect.setSize(x - currentRect.x,
                                    y - currentRect.y);
                updateDrawableRect(getWidth(), getHeight());
                Rectangle totalRepaint = rectToDraw.union(previousRectDrawn);
                repaint(totalRepaint.x, totalRepaint.y,
                        totalRepaint.width, totalRepaint.height);
            }
            
            /**
             * Updates the location of the current shape's bounding rectangle
             * based on the user's mouse coordinates.
             * @param e the mouse event
             */
            public void updateLocation(MouseEvent e) {
                shapeBeingMoved.updateLocation(e.getPoint(), prevPoint);
                prevPoint = e.getPoint();
                repaint();
            }
        }
    }
}
