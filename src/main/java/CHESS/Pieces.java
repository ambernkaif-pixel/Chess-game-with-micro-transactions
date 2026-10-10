
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
    boolean hasMoved=false;
    boolean twosteps=false;
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
        int oldXP = this.xp;
        int oldYP = this.yp;
            if (name.equals("Pawn") && Board.enPassantTarget != null) {

            // White pawn
            if (white &&
                xp == Board.enPassantTarget.xp &&
                yp == oldYP - 1 &&                 // moves one up
                Board.enPassantTarget.yp == oldYP // target pawn is beside

            ) {
                Board.enPassantTarget.kill();
            }

            // Black pawn
            if (!white &&
                xp == Board.enPassantTarget.xp &&
                yp == oldYP + 1 &&                 // moves one down
                Board.enPassantTarget.yp == oldYP  // target pawn is beside

            ) {
                Board.enPassantTarget.kill();
            }
        }

            // CASTLING CHECK BEFORE UPDATING KING POSITION
            if (name.equals("King") && !this.hasMoved) {

            // King-side castling
            if (xp == this.xp + 2) {
            Pieces rook = Board.getPiece((this.xp + 3) * 64, this.yp * 64);
            if (rook != null && rook.name.equals("Rook") && !rook.hasMoved) {
                rook.xp = this.xp + 1;
                rook.yp = this.yp;
                rook.x = rook.xp * 64;
                rook.y = rook.yp * 64;
                rook.hasMoved = true;
            }
        }

            // Queen-side castling
            if (xp == this.xp - 2) {
            Pieces rook = Board.getPiece((this.xp - 4) * 64, this.yp * 64);
            if (rook != null && rook.name.equals("Rook") && !rook.hasMoved) {
                rook.xp = this.xp - 1;
                rook.yp = this.yp;
                rook.x = rook.xp * 64;
                rook.y = rook.yp * 64;
                rook.hasMoved = true;
            }
        }
    }
            Pieces target = Board.getPiece(xp*64,yp*64);
            if(target != null){
            if (target.white != white){
                target.kill();
            } else {
                return;
            }
            }
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
        this.hasMoved= true;
        if (name.equals("Pawn") && Math.abs(yp - oldYP) == 2) {
        Board.enPassantTarget = this;
    } else {
        // CLEAR ONLY IF NOT A DOUBLE MOVE
        Board.enPassantTarget = null;
    }

        Board.currentPlayer= 1- Board.currentPlayer;
    }
    public void kill(){
        ps.remove(this);
    }
    public boolean canmove(int xp,int yp){
        

    if (!withinboard(xp, yp)) return false;
    if ((white && Board.currentPlayer != 1) ||
    (!white && Board.currentPlayer != 0)) {
        return false;
    }


        // KING MOVEMENT
        if (name.equals("King")) {

            int dx = Math.abs(xp - this.xp);
            int dy = Math.abs(yp - this.yp);
            // CASTLING
            if (!hasMoved && yp == this.yp) {

                // King-side castling (short)
                if (xp == this.xp + 2) {
                    Pieces rook = Board.getPiece((this.xp + 3) * 64, this.yp * 64);

                    if (rook != null && rook.name.equals("Rook") && !rook.hasMoved) {

                        // Check squares between king and rook
                        if (Board.getPiece((this.xp + 1) * 64, this.yp * 64) == null &&
                            Board.getPiece((this.xp + 2) * 64, this.yp * 64) == null) {

                            return true; // Castling allowed
                        }
                    }
                }

                // Queen-side castling (long)
                if (xp == this.xp - 2) {
                    Pieces rook = Board.getPiece((this.xp - 4) * 64, this.yp * 64);

                    if (rook != null && rook.name.equals("Rook") && !rook.hasMoved) {

                        if (Board.getPiece((this.xp - 1) * 64, this.yp * 64) == null &&
                            Board.getPiece((this.xp - 2) * 64, this.yp * 64) == null &&
                            Board.getPiece((this.xp - 3) * 64, this.yp * 64) == null) {

                            return true;
                        }
                    }
                }
            }

            // King moves one square in any direction
            if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }

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
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }
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
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }
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
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }
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
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }
                Pieces target = Board.getPiece(xp*64,yp*64);
                
                if (target != null && target.white == this.white) {
                    return false;
                }

                return true;
            }
            if(Math.abs(xp-this.xp) == Math.abs(yp-this.yp)){
                Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
                if (thisPiece.white==true){
                    int w= 1;
                }
                else{
                    int w=0;
                }
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
            Pieces thisPiece=Board.getPiece(this.xp*64,this.yp*64);
            if (thisPiece.white==true){
                    int w= 1;
                }
            else{
                    int w=0;
                }
                        // EN PASSANT CHECK
            if (Board.enPassantTarget != null) {

                // The en-passant pawn must be next to me horizontally
                if (Board.enPassantTarget.yp == this.yp &&
                    Math.abs(Board.enPassantTarget.xp - this.xp) == 1) {

                    // White pawn moves upward
                    if (white && xp == Board.enPassantTarget.xp &&
                        yp == this.yp - 1) {
                        return true;
                    }

                    // Black pawn moves downward
                    if (!white && xp == Board.enPassantTarget.xp &&
                        yp == this.yp + 1) {
                        return true;
                    }
                }
            }



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
    private void changePlayer(int w){
        if (w == 1){
            w=0;
        }
        else{
            w=1;
        }
        
    }
    
    public boolean withinboard(int xp,int yp){
        if(xp>=0 && xp<=7 && yp>=0 && yp<=7){
            return true;
        }
        return false;
    }
    

     
}
