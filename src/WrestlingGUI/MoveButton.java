/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WrestlingGUI;

import java.awt.Graphics;

/**
 *
 * @author Arthur
 */
public class MoveButton {
    private String title, wmpackage;
    private int x;
    
    public MoveButton(String title, int x, String wmpackage){
        this.title = title;
        this.x = x;
        this.wmpackage = wmpackage;
    }
    
    public void render(Graphics g, int y, int sw, int sh){
        g.drawRect(x, y, sw-30, 30);
        g.drawString(title + " - " + wmpackage, x+10, y+20);
    }
    
    public String getTitle(){
        return title;
    }
    
    public int getX(){
        return x;
    }
}
