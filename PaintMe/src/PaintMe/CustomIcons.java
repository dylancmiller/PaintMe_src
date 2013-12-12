// CustomIcons.java - A class containing custom icon classes.

package PaintMe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Contains custom icon classes
 * @author Dylan
 */
public class CustomIcons {
    private static final int ICON_WIDTH = 20;
    private static final int ICON_HEIGHT = 20;
    
    /**
     * An icon with a Graphics2D oval
     */
    public static class OvalIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(1);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(stroke);
            g2.setColor(Color.BLACK);
            g2.drawOval(x + 1, y + 5, ICON_WIDTH - 2, ICON_HEIGHT - 10);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon with a Graphics2D rectangle
     */
    public static class RectangleIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(2);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y + 5, ICON_WIDTH - 1, ICON_HEIGHT - 10);
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon with a circle
     */
    public static class CircleIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(1);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(stroke);
            g2.setColor(Color.BLACK);
            g2.drawOval(x + 2, y + 2, ICON_WIDTH - 4, ICON_HEIGHT - 4);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon with a square
     */
    public static class SquareIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(2);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.BLACK);
            g.drawRect(x + 1, y + 2, ICON_WIDTH - 3, ICON_HEIGHT - 4);
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon with a polygon
     */
    public static class PolygonIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(1);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(stroke);
            g2.setColor(Color.BLACK);
            int xPoly[] = { 5, 13, 21, 16, 21, 13, 5};
            int yPoly[] = { 9,  4,  9, 13, 15, 21, 15};
            
            g2.draw(new Polygon(xPoly, yPoly, xPoly.length));
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
   
    /**
     * An icon with a polyline
     */
    public static class PolylineIcon implements Icon{
        private BasicStroke stroke = new BasicStroke(1);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(stroke);
            g2.setColor(Color.BLACK);
            int xPoly[] = { 5, 13,  7, 19, 14};
            int yPoly[] = { 9,  4, 15,  8, 20};
            
            Path2D path = new Path2D.Double();
            path.moveTo(xPoly[0], yPoly[0]);
            for(int i = 1; i < xPoly.length; i++){
                path.lineTo(xPoly[i], yPoly[i]);
            }
            g2.draw(path);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon with a polyline
     */
    public static class FreeHandIcon extends ImageIcon{
        public FreeHandIcon(){
            ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/icon-pencil.png"));
            super.setImage(image.getImage());
        }
        
        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon representing the current pen color
     */
    public static class SwatchIcon implements Icon{
        private Color color;
        
        private BasicStroke stroke = new BasicStroke(2);

        public SwatchIcon(Color color_)
        {
            color = color_;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x + 1, y + 1, 12 - 2, 12 -2);
        }

        @Override
        public int getIconWidth() {
            return 12;
        }

        @Override
        public int getIconHeight() {
            return 12;
        }
        
        public void setColor(Color color_){
            color = color_;
        }
    }
    
    /**
     * An icon representing the drag and drop feature
     */
    public static class SelectionIcon extends ImageIcon{
        public SelectionIcon(){
            ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/icon-selection.png"));
            super.setImage(image.getImage());
        }
        
        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon representing the draw text feature
     */
    public static class TextIcon extends ImageIcon{
        public TextIcon(){
            ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/icon-text.png"));
            super.setImage(image.getImage());
        }
        
        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
    
    /**
     * An icon representing the draw text feature
     */
    public static class MoveToFrontIcon extends ImageIcon{
        public MoveToFrontIcon(){
            ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/icon-moveToFront.png"));
            super.setImage(image.getImage());
        }
        
        @Override
        public int getIconWidth() {
            return ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return ICON_HEIGHT;
        }
    }
}
