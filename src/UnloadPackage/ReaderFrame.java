/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UnloadPackage;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Arthur Lewis
 */
public class ReaderFrame implements Runnable{
    private JFrame frame;
    private final int screenwidth, screenheight;
    private String title = "Wrestling Dictionary Unloader";
    private Reader screen;
    private BufferedImage logo;
    private boolean running = true;
    private String fileLocation;
    
    public ReaderFrame(String fileLocation){
        this.fileLocation = fileLocation;
        screenwidth = 800;
        screenheight = 500;
    }
    
    public void starter(){
        try{
            logo = ImageIO.read(new File(fileLocation+"Images/Logo.jpg"));
        }catch(Exception e){}
        
        screen = new Reader(screenwidth, screenheight, this, fileLocation);
        
        openFrame();
        
        screen.open();
    }
    
    public void run(){
        starter();
        while(running){
            try{
                if (screen.getDestroy()){
                    deleteFrame();
                }
            }catch(Exception e){
                e.printStackTrace();
                deleteFrame();
            }
        }
    }
    
    public void repaintScreen(){
        screen.repaint();
    }
    
    public void deleteFrame(){
        frame.dispose();
        running = false;
    }
    
    private void openFrame(){
        frame = new JFrame();
        frame.setTitle(title);
        frame.add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMaximumSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setMinimumSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setPreferredSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setIconImage(logo);
    }
    
    public JFrame getframe(){
        return frame;
    }
}
