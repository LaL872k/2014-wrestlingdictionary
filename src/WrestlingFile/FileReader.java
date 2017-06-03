/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WrestlingFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author Arthur
 */
public class FileReader {
    
    public FileReader(){
        
    }
    public WrestlingMove find(String filetitle, String fileLocation) throws Exception{
        
        File file = new File(fileLocation+"WrestlingMoves/"+filetitle);
        FileInputStream stream = new FileInputStream(file);
        Properties config = new Properties();
        config.load(stream);
        
        String title = config.getProperty("title");
        String description = config.getProperty("description");
        String link = config.getProperty("link");
        String wmpackage = config.getProperty("package");
        String date = config.getProperty("date");
        
        return new WrestlingMove(title, description, link, wmpackage, date, fileLocation);
    }
}