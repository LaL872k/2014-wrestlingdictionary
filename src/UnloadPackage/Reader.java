/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package UnloadPackage;

import WrestlingGUI.Dictionary;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Arthur
 */
public class Reader extends JPanel implements ActionListener{
    private final int screenwidth, screenheight;
    private JButton[] files;
    private int currentprogress = 0, totalprogress = 1;
    private boolean unloading = false, destroy = false;
    private String fileLocation;
    
    public Reader(int screenw, int screenh, ReaderFrame f, String fileLocation){
        this.fileLocation = fileLocation;
        screenwidth = screenw;
        screenheight = screenh;
    }
    
    public void open(){
        showFiles();
    }
    
    public void showFiles(){
        File folder = new File(fileLocation+"MovePackages");
        File[] allf = folder.listFiles();
        System.out.println("AllFiles:");
        for (int q = 0; q < allf.length; q++){
            System.out.println(allf[q].getName());
        }
        files = new JButton[allf.length];
        for (int q = 0; q < files.length; q++){
            files[q] = new JButton(allf[q].getName());
            files[q].setBounds(20, 20+(q*22), 200, 20);
            files[q].addActionListener(this);
            files[q].setName(fileLocation+"MovePackages/"+allf[q].getName());
        }
        setUp();
    }
    
    public void setUp(){
        super.setLayout(null);
        for (int q = 0; q < files.length; q++){
            super.add(files[q]);
        }
    }
    
    public boolean getDestroy(){
        return destroy;
    }
    
    public void progressBar(Graphics g){
        if (unloading){
            System.out.println("Drawing Progress Bar");
            g.setColor(Color.RED);
            g.drawRect(0, screenheight-20, screenwidth, 20);
            g.fillRect(0, screenheight-20, (currentprogress/totalprogress)*screenwidth, 20);
        }
    }
    
    public void unload(String location, String wmpackage){
        File starter = new File(location);
        File[] movefiles = starter.listFiles();
        unloading = true;
        if (movefiles != null){
            unloading = true;
            totalprogress = movefiles.length;
            System.out.println("1");
            repaint();
            
            System.out.println("Unloading " + wmpackage + "-package...");
            
            for (int q = 0; q < movefiles.length; q++){
                final String fileTitle = movefiles[q].getName();
                File WMFile = new File(fileLocation+"WrestlingMoves/"+fileTitle);

                try{
                    WMFile.createNewFile();

                    System.out.println("file " + fileTitle + " created");

                    FileOutputStream stream = new FileOutputStream(WMFile);
                    FileInputStream streamin = new FileInputStream(movefiles[q]);
                    Properties prop = new Properties();
                    prop.load(streamin);

                    write(stream, "title=" + prop.getProperty("title"));
                    write(stream, "description=" + prop.getProperty("description"));
                    write(stream, "link=" + prop.getProperty("link"));
                    write(stream, "package=" + wmpackage);
                    write(stream, "date=" + prop.getProperty("date"));
                    
                    currentprogress = q+1;
                    
                    System.out.println("repainted the screen - " + currentprogress + "/" + totalprogress + " unloading="+unloading);
                }catch(Exception ex){
                    System.out.println("Could not unload file " + fileTitle);
                    return;
                }
                repaint();
            }
            unloading = false;
            totalprogress = 1;
            currentprogress = 0;
            //repaint();
        }
    }
    
    private void write(FileOutputStream stream, String content)throws Exception{
        String newLine = System.getProperty("line.separator");
        
        content += newLine;
        byte[] data = content.getBytes();
        stream.write(data, 0, data.length);
    }
    
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        System.out.println("Repaint | unloading:" + unloading);
        progressBar(g2);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (int q = 0; q < files.length; q++){
            if (source.equals(files[q])){
                unload(files[q].getName(), files[q].getText());
                destroy = true;
            }
        }
    }
}