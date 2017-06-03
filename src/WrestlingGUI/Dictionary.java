/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WrestlingGUI;

import WrestlingFile.*;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Arthur Lewis
 */

//rember for later - open up the link
//desktop.browse(<string>link);
public class Dictionary{
    private String fileLocation = "";
    private final int screenwidth, screenheight;
    private JFrame frame;
    private String title, version;
    private ArrayList <WrestlingMove> moves = new ArrayList();
    private DictionaryScreen screen;
    private BufferedImage logo;
    
    public Dictionary(){
        screenwidth = 800;
        screenheight = 500;
        title = "Wrestling Dictionary";
        screen = new DictionaryScreen(screenwidth, screenheight, this, fileLocation);
        
        try {
            String versionfilelocation = fileLocation+"Version.txt";
            File versionf = new File(versionfilelocation);
            FileInputStream streamin = new FileInputStream(versionf);
            Properties prop = new Properties();
            prop.load(streamin);
            
            version = prop.getProperty("version");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException exe){
            exe.printStackTrace();
        }
    }
    
    public void openFrame(){
        try{
            logo = ImageIO.read(new File(fileLocation+"Images/Logo.jpg"));
        }catch(Exception e){}
        
        frame = new JFrame();
        frame.setTitle(title + " | " + version);
        frame.add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMaximumSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setMinimumSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setPreferredSize(new Dimension(screenwidth+6, screenheight + 29));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addMouseListener(new ML());
        frame.addKeyListener(new AL());
        frame.pack();
        frame.setIconImage(logo);
    }
    
    public void setMoves(ArrayList <WrestlingMove> moves){
        this.moves = moves;
    }
    
    public void openFiles(){
        System.out.println("Opening Files...");
        FileReader fr = new FileReader();
        File[] total = new File(fileLocation+"WrestlingMoves").listFiles();
        for (int q = 0; q < total.length; q++){
            try{
                moves.add(fr.find(total[q].getName(), fileLocation));
                System.out.println("Opened " + total[q].getName());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public String[] search(String search){
        String[] results;
        ArrayList <String> lookup = new ArrayList();
        
        for (int q = 0; q < moves.size(); q++){
            if (moves.get(q).getTitle().toLowerCase().contains(search.toLowerCase())){
                lookup.add(moves.get(q).getTitle());
            }
        }
        
        results = toStringArray(lookup);
        return results;
    }
    
    public String[] toStringArray(ArrayList<String>data){
        String[] newdata = new String[data.size()];
        
        for (int q = 0; q < data.size(); q++){
            newdata[q] = data.get(q);
        }
        
        return newdata;
    }
    
    public boolean addMove(WrestlingMove wm){
        for (int q = 0; q < moves.size(); q++){
            if (wm.getTitle().equalsIgnoreCase(moves.get(q).getTitle())){
                return false;
            }
        }
        moves.add(wm);
        screen.update(this);
        return true;
    }
    
    public boolean addMoveAndSave(WrestlingMove wm){
        if (addMove(wm)){
            moves.get(moves.size()-1).save();
            return true;
        }
        return false;
    }
    
    public WrestlingMove getWrestlingMove(String title){
        for (int q = 0; q < moves.size(); q++){
            if (moves.get(q).getTitle().equalsIgnoreCase(title)){
                return moves.get(q);
            }
        }
        return null;
    }
    
    private class ML extends MouseAdapter{
        public void mousePressed(MouseEvent m){
            screen.MousePressed(m);
        }
    }
    
    private class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            screen.onCommand();
            screen.repaint();
        }
    }
}
