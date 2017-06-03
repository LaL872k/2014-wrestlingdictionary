/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package UnloadPackage;

/**
 *
 * @author Arthur
 */
public class Unload {
    ReaderFrame read;
    private String fileLocation;
    
    public Unload(String fileLocation){
        this.fileLocation = fileLocation;
        read = new ReaderFrame(fileLocation);
        Thread t = new Thread((Runnable) read);
        t.start();
    }
    
    public ReaderFrame getReader(){
        return read;
    }
}
