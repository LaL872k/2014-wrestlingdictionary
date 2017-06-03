/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WrestlingFile;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Arthur Lewis
 */
public class WrestlingMove implements ActionListener{
    private String description;
    private String title;
    private String link;
    private String WMpackage;
    private String date;
    private ImageIcon videoimage;
    private JButton video = new JButton();
    private String fileLocation;
    
    public WrestlingMove(String title, String description, String link, String WMpackage, String date, String fileLocation){
        this.fileLocation = fileLocation;
        this.description = description;
        this.title = title;
        this.link = link;
        this.WMpackage = WMpackage;
        this.date = date;
        videoimage = new ImageIcon(new ImageIcon(fileLocation+"Images/Video.png").getImage().getScaledInstance(90, 80, 1));
    }
    
    public void save(){
        final String fileTitle = removechar(' ', getTitle().toCharArray()) + ".txt";
        File WMFile = new File(fileLocation+"WrestlingMoves/"+fileTitle);
        
        try{
            WMFile.createNewFile();
            
            System.out.println("file " + fileTitle + " created");
            
            FileOutputStream stream = new FileOutputStream(WMFile);
            write(stream, "title=" + getTitle());
            write(stream, "description=" + getDescription());
            write(stream, "link=" + getLink());
            write(stream, "package=" + getWMpackage());
            write(stream, "date=" + getDate());
            
        }catch(Exception e){
            System.out.println("Could not save file " + fileTitle);
            e.printStackTrace();
        }
    }
    
    public String removechar(char c, char[] cs){
        String result = "";
        for (int q = 0; q < cs.length; q++){
            if (cs[q] != c){
                result += cs[q];
            }
        }
        return result;
    }
    
    private void write(FileOutputStream stream, String content)throws Exception{
        String newLine = System.getProperty("line.separator");
        
        content += newLine;
        byte[] data = content.getBytes();
        stream.write(data, 0, data.length);
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getLink(){
        return link;
    }
    
    public String getWMpackage(){
        return WMpackage;
    }
    
    public String getDate(){
        return date;
    }
    
    public void render(Graphics g, JPanel panel, int x, int y, int sw, int sh){
        int returns = 0;
        
        g.drawString(title, x+10, y+30);
        g.drawLine(x+5, y+40, sw-x-100, y+40);
        
        g.drawString("Created on: " + date, x+10, y+60);
        g.drawLine(x+5, y+70, sw-x-100, y+70);
        
        char[] des = description.toCharArray();
        ArrayList <String> para = new ArrayList();
        String currentWord = "";
        int current = 0;
        for (int q = 0; q < des.length; q++){
            if ((des[q] == '[' && des[q+1] == 'n' && des[q+2] == ']') || current>140){
                current = 0;
                para.add(currentWord);
                currentWord = "";
                q+=2;
                returns++;
            }else{
                currentWord += des[q];
                current++;
            }
            if (q+1 == des.length){
                current = 0;
                para.add(currentWord);
                currentWord = "";
                q+=2;
                break;
            }
        }
        for (int q = 0; q < para.size(); q++){
            g.drawString(para.get(q), x+10, y+95+(q*15));
        }
        g.drawLine(x+5, (y+110+(returns*15)), sw-x-100, y+110+(returns*15));
        
        video.setBounds(sw-x-90, y+10, 90, 80);
        video.addActionListener(this);
        video.setIcon(videoimage);
        panel.add(video);
    }
    
    public void removeGraphics(JPanel panel){
        try{
            panel.remove(video);
        }catch(Exception e){}
    }
    
    public void actionPerformed(ActionEvent e) {
       Object source = e.getSource();
       if (source.equals(video)){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(link));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
       }
    }
}
