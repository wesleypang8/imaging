package imaging;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class DragListener implements DropTargetListener {
    private BufferedImage img;
    private ImagingView view;

    public DragListener(ImagingView view) {
        this.view = view;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent e) {
        e.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable t = e.getTransferable();
        DataFlavor[] df = t.getTransferDataFlavors();

        for (DataFlavor f : df) {
            try {
                if (f.isFlavorJavaFileListType()) {
                    List<File> files =  (List<File>) t.getTransferData(f);

                    for(File s : files){
                        displayImage(s.getPath());

                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void displayImage(String path) {

        try {
            img = ImageIO.read(new File(path));
            view.setImage(img);
        } catch (Exception e) {

        }
    }

    public BufferedImage getImage() {
        return img;
    }

}
