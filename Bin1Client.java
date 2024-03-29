import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import javax.swing.*;
import java.awt.image.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Bin1Client extends JFrame {

    final int IMG_SIZE = 512;
    byte[][] pic = new byte[IMG_SIZE][IMG_SIZE];
    byte rpic[][] = new byte[IMG_SIZE][IMG_SIZE];
    int[] lut = new int[256];

    BufferedImage img = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_RGB);

    public void start(String[] hosts, String img_name) throws Exception {
        setTitle("Binarization");
        setSize(IMG_SIZE, IMG_SIZE + 25 + 40);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        lut[0] = 0;
        lut[1] = 0xffffff;

        Image im = getImage(img_name);
        int[] pixels = new int[IMG_SIZE * IMG_SIZE];

        // converts Image to an array
        PixelGrabber pg = new PixelGrabber(im, 0, 0, IMG_SIZE, IMG_SIZE, pixels, 0, IMG_SIZE);
        try {
            pg.grabPixels();
        } catch (InterruptedException ee) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }
        for (int i = 0; i < IMG_SIZE; i++)
            for (int j = 0; j < IMG_SIZE; j++)
                pic[i][j] = (byte) (0xff & pixels[j * IMG_SIZE + i]);

        BinThread[] threads = {
                // esquerda superior
                new BinThread(hosts[0], pic, 266, 266, 10, 10),
                // esquerda inferior
                new BinThread(hosts[1], pic, 512, 266, 256, 10),
                // direita superior
                new BinThread(hosts[2], pic, 266, 512, 10, 256),
                // direita inferior
                new BinThread(hosts[3], pic, 512, 512, 256, 256),
        };
        final ExecutorService threadExecutor = Executors.newFixedThreadPool(10);
        for (BinThread thread : threads) {
            threadExecutor.execute(thread);
        }

        // Atualiza a imagem.
        threadExecutor.shutdown();
        try {
            threadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            for (BinThread thread : threads) {
                thread.updateRpic(rpic);
            }
        } catch (InterruptedException e) {
            System.err.println("interrupted updating pixels!");
        }

        repaint();

        File file = new File("imageResult.png");
        ImageIO.write(img, "png", file);
    }

    public void paint(Graphics g) {
        for (int i = 0; i < IMG_SIZE; i++)
            for (int j = 0; j < IMG_SIZE; j++) {
                // System.out.print(pic[i][j] + " ");
                img.setRGB(i, j, lut[rpic[i][j]]);
            }
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, 0, 25, this);
    }

    protected Image getImage(String fileName) {
        Image img = getToolkit().getImage(fileName);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
            tracker.removeImage(img, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void main(String[] args) throws Exception {
        String file = args[0];
        System.out.println(file);

        String[] hosts = {
                args[1],
                args[2],
                args[3],
                args[4],
        };
        Bin1Client mc = new Bin1Client();

        // String host = "rmi://localhost:1099/Bin";
        mc.start(hosts, file);
    }
}
