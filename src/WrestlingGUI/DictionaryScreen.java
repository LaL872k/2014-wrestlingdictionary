/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WrestlingGUI;

import UnloadPackage.Unload;
import WrestlingFile.WrestlingMove;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

public class DictionaryScreen extends JPanel implements ActionListener{
    private final int screenw, screenh;
    private String defalutName = "MoveName", defaultDescription = "Description (Hint: use '[n]' to retrurn)", defaultLink = "Youtube Link";
    private JTextField search = new JTextField("search"), createname = new JTextField(defalutName), createlink = new JTextField(defaultLink);
    private JTextArea createdescription = new JTextArea(defaultDescription);
    private String[] searchResults = {};
    private MoveButton[] button;
    private Dictionary d;
    private int scroll = 0, openMove = -1, pageType=1;
    private JButton up, down, createMove, create;
    private final int movesonscreen = 11;
    private boolean repaintNeeded = false, start = false;
    private ImageIcon createimage;
    private ImageIcon upimage;
    private ImageIcon downimage;
    private ImageIcon unloadimage;
    private JButton unload;
    private Unload un;
    private ImageIcon aellc;
    private Countdown sec;
    private String fileLocation;
    
    public DictionaryScreen(int screenw, int screenh, Dictionary dict, String fileLocation){
        this.fileLocation = fileLocation;
        aellc = new ImageIcon(new ImageIcon(fileLocation+"Images/AELLC.png").getImage());
        createimage = new ImageIcon(new ImageIcon(fileLocation+"Images/Create.png").getImage().getScaledInstance(48, 48, 1));
        upimage = new ImageIcon(new ImageIcon(fileLocation+"Images/Up.png").getImage().getScaledInstance(100, 30, 1));
        downimage = new ImageIcon(new ImageIcon(fileLocation+"Images/Down.png").getImage().getScaledInstance(100, 30, 1));
       unloadimage = new ImageIcon(new ImageIcon(fileLocation+"Images/Unload.png").getImage().getScaledInstance(48, 48, 1));
        
        sec = new Countdown(5f, this);
        this.screenh = screenh;
        this.screenw = screenw;
        d = dict;
        repaint();
    }
    
    public void update(Dictionary d){
        this.d = d;
    }
    
    public void setUp(){
        search.setBounds(1, 1, 200, 50);
        search.addActionListener(this);
        
        up = new JButton();
        up.setBounds(350, 60, 100, 30);
        up.setIcon(upimage);
        up.addActionListener(this);
        up.setVisible(false);
        
        down = new JButton();
        down.setBounds(350, 460, 100, 30);
        down.setIcon(downimage);
        down.addActionListener(this);
        down.setVisible(false);
        
        createMove = new JButton();
        createMove.setBounds(201, 2, 48, 48);
        createMove.setIcon(createimage);
        createMove.addActionListener(this);
        
        create = new JButton("Create Move");
        create.setBounds(20, 450, 200, 40);
        create.addActionListener(this);
        
        createname.setBounds(20, 80, 200, 30);
                
        createdescription.setBounds(20, 120, 400, 90);
                
        createlink.setBounds(20, 220, 200, 30);
        
        unload = new JButton();
        unload.setIcon(unloadimage);
        unload.setBounds(250, 2, 48, 48);
        unload.addActionListener(this);
        
        removePage3();
        
        gui();
    }
    
    public void gui(){
        super.setLayout(null);
        super.add(search);
        super.add(up);
        super.add(down);
        super.add(createMove);
        super.add(create);
        super.add(createname);
        super.add(createdescription);
        super.add(createlink);
        super.add(unload);
        super.repaint();
    }
    
    public void arrows(){
        if (scroll >= 1){
            up.setVisible(true);
        }else{
            up.setVisible(false);
        }
        if (searchResults.length-scroll>movesonscreen){
            down.setVisible(true);
        }else{
            down.setVisible(false);
        }
    }
    
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        
        if (!sec.finished()){
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenw, screenh);
            aellc.paintIcon(this, g2, 0, 0);
        }else{
            if (!start){
                setUp();
                start = true;
            }
            g2.drawRect(0, 0, screenw-1, 52);
            if (pageType == 1){
                arrows();
                if (button != null){
                    //Grammar
                    if (searchResults.length == 1){
                        g2.drawString("1 wrestling Move found.", 20, 80);
                    }else if (searchResults.length <= 0){
                        g2.drawString("Could not find any wrestling moves.", 20, 80);
                    }else{
                        g2.drawString(searchResults.length + " Wrestling Moves Found.", 20, 80);
                    }

                    for (int q = 0; q < button.length; q++){
                        if (q >= movesonscreen){
                            break;
                        }
                        button[q+scroll].render(graphics, 100+(q*32), screenw, screenh);
                    }
                }
            }else if (pageType == 2){
                d.getWrestlingMove(searchResults[openMove]).render(g2, this, 10, 50, screenw, screenh);
            }else if (pageType == 3){
                g2.drawRect(20, 120, 400, 90);
            }

            if (repaintNeeded){
                super.repaint();
                repaintNeeded = false;
            }
        }
    }
    
    public void returnToDefault(){
        createname.setText(defalutName);
        createdescription.setText(defaultDescription);
        createlink.setText(defaultLink);
    }
    
    public void addPage3(){
        create.setVisible(true);
        createname.setVisible(true);
        createdescription.setVisible(true);
        createlink.setVisible(true);
    }
    
    public void removePage3(){
        create.setVisible(false);
        createname.setVisible(false);
        createdescription.setVisible(false);
        createlink.setVisible(false);
    }
    
    public void removePage2(){
        if (openMove!=-1){
            d.getWrestlingMove(searchResults[openMove]).removeGraphics(this);
            openMove = -1;
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (un != null){
            if (!un.getReader().getframe().isActive()){
                ArrayList <WrestlingMove> example = new ArrayList();
                d.setMoves(example);
                d.openFiles();
                un = null;
            }
        }
        if (source.equals(search)){
            pageType=1;
            removePage2();
            removePage3();
        }
        onCommand();
        
        if (source.equals(down)){
            scroll++;
        }
        if (source.equals(up)){
            scroll--;
        }
        System.out.println(scroll);
        
        if (source.equals(createMove)){
            pageType = 3;
            takeButtonsOff();
            repaintNeeded = true;
            addPage3();
            returnToDefault();
            removePage2();
        }
        
        if (source.equals(create)){
            Date date = new Date();
            String dateText = (date.getMonth()+1) + "/" + date.getDate() + "/" + (date.getYear()+1900);
            d.addMoveAndSave(new WrestlingMove(createname.getText(), createdescription.getText(), createlink.getText(), "User Created", dateText, fileLocation));
        }
        
        if (source.equals(unload)){
            un = new Unload(fileLocation);
        }
        
        repaint();
    }
    
    public void onCommand(){
        searchResults = d.search(search.getText());
        button = new MoveButton[searchResults.length];
        for (int q = 0; q < searchResults.length; q++){
            button[q] = new MoveButton(searchResults[q], 20, d.getWrestlingMove(searchResults[q]).getWMpackage());
        }
    }
    
    public void MousePressed(MouseEvent m){
        int x = m.getX()-8+5;
        int y = m.getY()-31+5;
        
        if (searchResults != null){
            if (movesonscreen <= searchResults.length){
                for (int q = 0; q < movesonscreen; q++){
                    if (new Rectangle(button[q+scroll].getX(), 100+((q)*32), screenw-30, 30).contains(x, y) && pageType == 1){
                        openMove = q+scroll;
                        pageType = 2;
                        takeButtonsOff();
                        repaintNeeded = true;
                    }
                }
            }else{
                for (int q = 0; q < searchResults.length; q++){
                    if (new Rectangle(button[q+scroll].getX(), 100+((q)*32), screenw-30, 30).contains(x, y) && pageType == 1){
                        openMove = q+scroll;
                        pageType = 2;
                        takeButtonsOff();
                        repaintNeeded = true;
                    }
                }
            }
        }
        repaint();
    }
    
    public void takeButtonsOff(){
        up.setVisible(false);
        down.setVisible(false);
    }
}