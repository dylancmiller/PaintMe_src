//ShapeDataClipboardOwner.java- Sets up a clipboardowner and a transferrable 
//                              object for the ShapeData class.

package PaintMe;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Sets up a clipboardowner and a transferrable object for the ShapeData class.
 * @author Dylan
 */
public class ShapeDataClipboardOwner implements ClipboardOwner, Transferable {
    private ShapeData selection;
    public static DataFlavor dataFlavor = new DataFlavor(ShapeData.class, 
                                                           "ShapeData");
    
    public ShapeDataClipboardOwner(ShapeData selection_){
        selection = selection_;
    }
    
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = {dataFlavor};
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)){
           return this.selection;
        } else {
           throw new UnsupportedFlavorException(dataFlavor);
        }
    }
    
}
