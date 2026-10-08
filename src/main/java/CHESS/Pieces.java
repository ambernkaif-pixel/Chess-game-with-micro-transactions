
package CHESS;

import java.awt.Image;
import java.util.LinkedList;
import javax.swing.*;

/**
 *
 * @author amber
 */
public class Pieces {
    int x;
    int y;
    int xp;
    int yp;
    boolean white;
    LinkedList <Pieces> ps;
    String name;
    Image img;
    public Pieces(int xp, int yp,String n, boolean white,LinkedList<Pieces> ps) {
        this.xp = xp;
        this.yp = yp;
        x=xp*64;
        y=yp*64;
        
        this.white = white;
        this.ps=ps;
        name=n;
        loadImage();
        ps.add(this);
    }
    private void loadImage(){
        String color=white? "W" :"B";
        String path="/CHESS/" + name + color + ".png";
        System.out.println("TRYING PATH = " + path);

        java.net.URL url = getClass().getResource(path);
        System.out.println("URL = " + url);
        if (url == null) {
        System.out.println("IMAGE NOT FOUND!");
        return;
        }
        else{
            img=new ImageIcon(getClass().getResource(path)).getImage();
        }

        img = new ImageIcon(url).getImage();
    }
        
    public void move(int xp,int yp){
        if(Board.getPiece(xp*64,yp*64)!=null){
            if (Board.getPiece(xp*64,yp*64).white != white){
            Board.getPiece(xp*64, yp*64).kill();
            }  
            else{
                x=this.xp*64;
                y=this.yp*64;
                return;
            }
        }
        this.xp=xp;
        this.yp=yp;
        x=xp*64;
        y=yp*64;
    }
    public void kill(){
        ps.remove(this);
    }
    public boolean canmove(int xp,int yp){
        

    if (!withinboard(xp, yp)) return false;

        // KING MOVEMENT
        if (name.equals("King")) {

            int dx = Math.abs(xp - this.xp);
            int dy = Math.abs(yp - this.yp);

            // King moves one square in any direction
            if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {

                Pieces target = Board.getPiece(xp * 64, yp * 64);

                // Cannot capture own piece
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }

            return false;
        }

        return false;
    }

    
    public boolean withinboard(int xp,int yp){
        if(xp>=0 && xp<=7 && yp>=0 && yp<=7){
            return true;
        }
        return false;
    }
    //public class king extends Pieces {
//        public boolean canmove(int xp, int yp){
//            if(withinboard(xp,yp)){
//                if(Math.abs(xp-this.xp))
//            }
//        }
           
    //}
     
}
