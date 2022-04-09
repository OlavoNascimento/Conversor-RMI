import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

public class Bin1Client extends JFrame {

    final int IMG_SIZE=512;
    byte[][] pic = new byte[IMG_SIZE][IMG_SIZE];
	byte rpic[][] = new byte[IMG_SIZE][IMG_SIZE];
    int[] lut = new int[256];

    Bin1Server myServer;

    BufferedImage img = new BufferedImage(IMG_SIZE, IMG_SIZE,BufferedImage.TYPE_INT_RGB);
 
    public void start(String img_name) {
        setTitle("Binarization");
        setSize(IMG_SIZE,IMG_SIZE+25+40);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        lut[0]=0;
        lut[1]=0xffffff;

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
        for(int i=0; i<IMG_SIZE; i++)
           for(int j=0; j<IMG_SIZE; j++)
                pic[i][j] = (byte) (0xff&pixels[j*IMG_SIZE + i]); 
        
        myServer = new Bin1Server();
            
        double start = new Date().getTime();
        // esquerda superior
        rpic = myServer.bin(pic, rpic, 266, 266, 10, 10);
        System.out.println("Demorou " + (new Date().getTime()-start) + " ms");

        // esquerda inferior
        rpic = myServer.bin(pic, rpic, 512, 266, 256, 10);
        System.out.println("Demorou " + (new Date().getTime()-start) + " ms");


        // direita superior
        rpic = myServer.bin(pic, rpic, 266, 512, 10, 256);
        System.out.println("Demorou " + (new Date().getTime()-start) + " ms");


        // direita inferior
        rpic = myServer.bin(pic, rpic, 512, 512, 256, 256);
        System.out.println("Demorou " + (new Date().getTime()-start) + " ms");

        repaint();

    }

   
    public void paint(Graphics g) { 
 
        for(int i=0; i<IMG_SIZE; i++)
           for(int j=0; j<IMG_SIZE; j++) {
               //System.out.print(pic[i][j] + " ");
               img.setRGB(i,j,lut[rpic[i][j]]);
            }
       Graphics2D g2 = (Graphics2D) g;
       g2.drawImage(img,0,25,this);   
    }
 
    protected Image getImage(String fileName) {
        Image img = getToolkit().getImage(fileName);
        try {
           MediaTracker tracker = new MediaTracker(this);
           tracker.addImage(img, 0);
           tracker.waitForID(0);
           tracker.removeImage(img,0);
        } catch (Exception e) { e.printStackTrace(); }
        return img;
    }

    public static void main(String[] arg) {
        System.out.println(arg[0]);
        Bin1Client mc = new Bin1Client();
        mc.start(arg[0]);
    }
}
