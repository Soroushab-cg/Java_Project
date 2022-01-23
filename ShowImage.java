import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ShowImage extends Canvas {

    public static Image GetFile() throws IOException {//GEN-FIRST:event_chooseButtonActionPerformed
        String PATH;
        JFileChooser Chooser = new JFileChooser();
        Chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter Filter = new FileNameExtensionFilter("Image", "jpg", "png", "ico","gif");
        Chooser.addChoosableFileFilter(Filter);
        int result =Chooser.showSaveDialog(null);

        if(result == JFileChooser.APPROVE_OPTION) {

            File select_file = Chooser.getSelectedFile();
            PATH = select_file.getAbsolutePath();

            FileInputStream input = new FileInputStream(new File(PATH));
            BufferedImage image = ImageIO.read(input);
            image = scale(image, 600, 600);
            scaleImage = image;

            ShowImage show = new ShowImage();
            JFrame f = new JFrame();
            f.add(show);
            f.setSize(600, 600);
            f.setVisible(true);

            int check = JOptionPane.showConfirmDialog(null,"Is This the Post you want to send?");
            if(check == 0){
                f.dispose();
                System.out.println("Yes");
                return scaleImage;
            }
            else {
                f.dispose();
                return GetFile();
            }

        }
        return null;
    }

    public static Image scaleImage = null;

    public void SetScaleImage(Image Scale){
        scaleImage = Scale;
    }


    public void paint(Graphics g) {

        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = null;

        g.drawImage(scaleImage, 0, 0, this);
    }


    public static BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }

}
