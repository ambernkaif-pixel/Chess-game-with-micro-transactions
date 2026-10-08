
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
        if (name.equals("Knight")){
            if (!withinboard(xp, yp)) return false;
            // knight can move if its movement ratio is 1:2 or 2:1
            if(Math.abs(xp-this.xp) * Math.abs(yp-this.yp) == 2){
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
            return false;
        }
        if (name.equals("Rook")){
            if (!withinboard(xp, yp)) return false;
            // rook can move either the full row or the full column
            if(xp == this.xp || yp == this.yp){
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
            return false;
        }

        
       if (name.equals("Bishop")){
            if (!withinboard(xp, yp)) return false;
            if(Math.abs(xp-this.xp) == Math.abs(yp-this.yp)){
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
            return false;
        }
       if (name.equals("Queen")){
            if (!withinboard(xp, yp)) return false;
            if(xp == this.xp || yp == this.yp){
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
            if(Math.abs(xp-this.xp) == Math.abs(yp-this.yp)){
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
        }
       if (name.equals("Pawn")){
            if (!withinboard(xp, yp)) return false;
            int moveValue = white ? -1 :1;
            int frontX = this.xp;
            int frontY = this.yp + moveValue;
            Pieces target = Board.getPiece(xp*64,yp*64);
            Pieces frontPiece = Board.getPiece(frontX * 64, frontY * 64);
            if (xp == this.xp && yp == this.yp + moveValue && frontPiece == null){
                return true;
            }
            int startRank = white ? 6 : 1;

            if (this.yp == startRank && xp == this.xp && yp == this.yp + 2 * moveValue) {

                Pieces front1 = Board.getPiece(this.xp * 64, (this.yp + moveValue) * 64);
                Pieces front2 = Board.getPiece(this.xp * 64, (this.yp + 2 * moveValue) * 64);

                if (front1 == null && front2 == null) {
                    return true;
                }
            }
            if(Math.abs(xp-this.xp) == 1 && yp == this.yp + moveValue && target != null && target.white != this.white){
                return true;
            }

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
