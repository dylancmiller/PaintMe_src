// ShapeData.java - Contains the information needed to draw shapes with their desired colors,
//                  filling, and size.
//

package PaintMe;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Contains the information needed to draw shapes with their desired colors,
 * filling, and size.
 * @author Dylan
 */
public class ShapeData implements Serializable {
    public enum SHAPE {None, Rectangle, Oval, Square, Circle, Polygon, 
                       Polyline, FreeHand, Text};
    
    private UUID shapeId;
    public Rectangle boundingRect;
    public SHAPE shape;
    public Color color;
    public boolean isFilled;
    public String text;
    private ArrayList<Point> vertices;
    public int strokeSize;
    
    /**
     * Creates a new ShapeData object
     * @param shape_ the type of shape
     * @param boundingRect_ the bounding rectangle around the shape
     * @param color_ the color of the shape
     * @param isFilled_ a boolean value that determines whether or not the shape
     *                  is filled.
     * @param strokeSize_ the width of the brush stroke
     */
    public ShapeData(SHAPE shape_, Rectangle boundingRect_, Color color_, 
                     boolean isFilled_, int strokeSize_)
    {
        shapeId = UUID.randomUUID();
        shape = shape_;
        boundingRect = boundingRect_;
        color = color_;
        isFilled = isFilled_;
        strokeSize = strokeSize_;
    } 
    
    /**
     * Creates a new ShapeData object
     * @param shape_ the type of shape
     * @param boundingRect_ the bounding rectangle around the shape
     * @param color_ the color of the shape
     * @param isFilled_ a boolean value that determines whether or not the shape
     *                  is filled.
     * @param text_ the text to be drawn
     * @param strokeSize_ the width of the brush stroke
     */
    public ShapeData(SHAPE shape_, Rectangle boundingRect_, Color color_, 
                     boolean isFilled_, String text_, int strokeSize_)
    {
        shapeId = UUID.randomUUID();
        shape = shape_;
        boundingRect = boundingRect_;
        color = color_;
        isFilled = isFilled_;
        strokeSize = strokeSize_;
        
        if(shape == SHAPE.Text)
            text = text_;
    } 
    
    /**
     * Creates a new ShapeData object (For any shape that can be constructred
     * from a Path2D object)
     * @param shape_ the type of shape
     * @param boundingRect_ the bounding rectangle around the shape
     * @param color_ the color of the shape
     * @param isFilled_ a boolean value that determines whether or not the shape
     *                  is filled.
     * @param startPoint the initial point for the vertices object
     * @param strokeSize_ the width of the brush stroke
     */
    public ShapeData(SHAPE shape_, Rectangle boundingRect_, Color color_, 
                     boolean isFilled_, Point startPoint, int strokeSize_)
    {
        shapeId = UUID.randomUUID();
        shape = shape_;
        boundingRect = boundingRect_;
        color = color_;
        isFilled = isFilled_;
        strokeSize = strokeSize_;
        
        if(shape == SHAPE.Polygon || 
           shape == SHAPE.Polyline || 
           shape == SHAPE.FreeHand){
            vertices = new ArrayList<Point>();
            vertices.add(startPoint);
        }
    } 
    
    /**
     * Creates a new ShapeData object from an existing shape. Assigns a new
     * shapeId.
     * @param otherShape 
     */
    public ShapeData(ShapeData otherShape){
        shapeId = UUID.randomUUID();
        shape = otherShape.shape;
        boundingRect = new Rectangle(otherShape.boundingRect);
        color = otherShape.color;
        isFilled = otherShape.isFilled;
        strokeSize = otherShape.strokeSize;
        text = otherShape.text;
        vertices = otherShape.getVertices();
    }
    
    /**
     * Gets the vertices for the current object
     * @return builds a new list of vertices for Polygons, polylines, and free 
     * hand shapes. Returns the vertices of the bounding box for all other
     * shapes.
     */
    public ArrayList<Point> getVertices(){
        if(shape == SHAPE.Polygon || 
           shape == SHAPE.Polyline || 
           shape == SHAPE.FreeHand)
            return vertices;
        else{
            ArrayList<Point> temp = new ArrayList<Point>();
            temp.add(new Point(boundingRect.x, boundingRect.y));
            temp.add(new Point(boundingRect.x, 
                               boundingRect.y + boundingRect.height));
            temp.add(new Point(boundingRect.x + boundingRect.width,
                               boundingRect.y));
            temp.add(new Point(boundingRect.x + boundingRect.width,
                               boundingRect.y + boundingRect.height));
            return temp;
        }
    }
   
    /**
     * Returns the current shapeId
     * @return 
     */
    public UUID getShapeId(){
        return shapeId;
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ShapeData))return false;
        ShapeData otherShapeData = (ShapeData)other;
        return this.shapeId.equals(otherShapeData.getShapeId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.shapeId != null ? this.shapeId.hashCode() : 0);
        return hash;
    }
    
    /**
     * Determines if a point lies inside the area covered by this shape
     * @param x x coordinate
     * @param y y coordinate
     * @return true or false value
     */
    public boolean contains(int x, int y){
        switch(shape){
            case Square:
            case Rectangle:
            case Text:
                return boundingRect.contains(x, y);
            case Circle:
            case Oval:
                return (new Ellipse2D.Double(boundingRect.x, boundingRect.y,
                        boundingRect.width, boundingRect.height))
                        .contains(x, y);
            case FreeHand:
            case Polyline:
                Path2D path = new Path2D.Double();
                for(int i = 0; i < vertices.size(); i++){
                    if(i == 0)
                        path.moveTo(vertices.get(i).x, 
                                    vertices.get(i).y);
                    else
                        path.lineTo(vertices.get(i).x, 
                                    vertices.get(i).y);
                }
                return path.contains(new Point(x, y));
            case Polygon:
                int[] polyX =  new int[vertices.size()];
                int[] polyY =  new int[vertices.size()];
                for(int i = 0; i < vertices.size(); i++){
                    polyX[i] = vertices.get(i).x;
                    polyY[i] = vertices.get(i).y;
                }
                return (new Polygon(polyX, polyY, polyX.length)).contains(x, y);
        }
        return false;
    }
    
    /**
     * Updates the vertices points for the object to be drawn at a new location
     * based on the mouse position
     * @param curPoint current mouse position
     * @param prevPoint previous mouse position
     */
    public void updateLocation(Point curPoint, Point prevPoint){
        if(prevPoint != null){
            int distX;
            int distY;
            
            switch(shape){
                case Square:
                case Rectangle:
                case Text:
                case Circle:
                case Oval:
                    distX = prevPoint.x - boundingRect.x;
                    distY = prevPoint.y - boundingRect.y;
                    boundingRect.setLocation(curPoint.x - distX, 
                                             curPoint.y - distY);
                    break;
                case FreeHand:
                case Polyline:
                case Polygon:
                    for(int i = 0; i < vertices.size(); i++)
                    {
                        distX = prevPoint.x - vertices.get(i).x;
                        distY = prevPoint.y - vertices.get(i).y;
                        vertices.get(i).x = curPoint.x - distX;
                        vertices.get(i).y = curPoint.y - distY;
                    }
                    break;
            }
        }
    }
}
