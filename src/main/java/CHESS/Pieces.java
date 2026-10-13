
package CHESS;

import static CHESS.Board.ps;
import java.awt.Image;
import java.util.ArrayList;
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
    Pieces checkingP;
    boolean gameOver;
    boolean hasMoved=false;
    boolean twosteps=false;
    public ArrayList <Pieces> promoPiece = new ArrayList<>();
    public boolean promotion;
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
    private boolean canPromote(){
        if(!name.equals("Pawn")) return false;
        if(white && this.yp==0){
                promotion = true;
                return true;
            }
        if(!white && this.yp==7){
                promotion=true;
                return true;
            }
        return false;
       }
        
    public boolean kinginCheck() {
     Pieces King = getKing(true);

     if (King == null) {
         return false;
     }

     boolean result = this.canmove(King.xp, King.yp);

//     System.out.println(
//         name + " " +
//         (white ? "WHITE" : "BLACK") +
//         " currentPlayer=" + Board.currentPlayer +
//         " result=" + result
//     );

     checkingP = result ? this : null;
     return result;
 }

    private Pieces getKing(boolean opponent){
        Pieces King=null;
        for (Pieces p: ps){
            if(opponent){
               if(p.name.equals("King")&& p.white != this.white){
                   King=p;
               }
            }else{
                if(p.name.equals("King")&& p.white == this.white){
                    King = p;
                }
            }
        }
        return King;
    }
    public boolean cancaptureking() {

    Pieces king = getKing(false);

    if (king == null) {
        return false;
    }

    for (Pieces p : ps) {

        if (p.white != king.white) {

            int save = Board.currentPlayer;
            Board.currentPlayer = p.white ? 1 : 0;

            boolean attack = p.canmove(king.xp, king.yp);

            Board.currentPlayer = save;

            if (attack) {
                System.out.println(
                    p.name + " attacks king at "
                    + king.xp + "," + king.yp
                );
                return true;
            }
            }
        }

        return false;
    }

    public void promoteTo(Pieces newPiece) {
        // Set board coordinates
        newPiece.xp = this.xp;
        newPiece.yp = this.yp;

        // Convert board coords → pixel coords
        newPiece.x = newPiece.xp * 64;
        newPiece.y = newPiece.yp * 64;

        // Replace pawn with new piece
        ps.remove(this);
        ps.add(newPiece);

        promotion = false;
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
        
        if (canPromote()){
            promotion = true;
        }
        if (promotion) {
            promoPiece.clear();
            Pieces q = new Pieces(-1, -1, "Queen", this.white, new LinkedList<>());
            q.x = 400; q.y = 50;

            Pieces r = new Pieces(-1, -1, "Rook", this.white, new LinkedList<>());
            r.x = 400; r.y = 120;

            Pieces b = new Pieces(-1, -1, "Bishop", this.white, new LinkedList<>());
            b.x = 400; b.y = 190;

            Pieces k = new Pieces(-1, -1, "Knight", this.white, new LinkedList<>());
            k.x = 400; k.y = 260;

            promoPiece.add(q);
            promoPiece.add(r);
            promoPiece.add(b);
            promoPiece.add(k);
        }

        
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
            // simulate king move
            int oldXP = this.xp;
            int oldYP = this.yp;

            this.xp = xp;
            this.yp = yp;

            boolean illegal = isIllegal(this, xp, yp);


            
            this.xp = oldXP;
            this.yp = oldYP;

            if (illegal) return false;
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
        if (name.equals("Rook")) {
            if (!withinboard(xp, yp)) return false;

            if (xp == this.xp) {
                int step = yp > this.yp ? 1 : -1;
                for (int y = this.yp + step; y != yp; y += step) {
                    if (Board.getPiece(this.xp * 64, y * 64) != null) return false;
                }
            } else if (yp == this.yp) {
                int step = xp > this.xp ? 1 : -1;
                for (int x = this.xp + step; x != xp; x += step) {
                   Pieces block = Board.getPiece(x * 64, this.yp * 64);
                   if (block != null) return false;

                }
            } else return false;

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            if (target != null && target.white == this.white) return false;

            return true;
        }


        
       if (name.equals("Bishop")) {
            if (!withinboard(xp, yp)) return false;

            if (Math.abs(xp - this.xp) != Math.abs(yp - this.yp)) return false;

            int stepX = xp > this.xp ? 1 : -1;
            int stepY = yp > this.yp ? 1 : -1;

            int cx = this.xp + stepX;
            int cy = this.yp + stepY;

            while (cx != xp && cy != yp) {
                if (Board.getPiece(cx * 64, cy * 64) != null) return false;
                cx += stepX;
                cy += stepY;
            }

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            Pieces block = Board.getPiece(cx * 64, cy * 64);
            if (block != null) return false;


            return true;
        }

       if (name.equals("Queen")) {
        if (!withinboard(xp, yp)) return false;

        // rook-like
        if (xp == this.xp || yp == this.yp) {

            if (xp == this.xp) {
                int step = yp > this.yp ? 1 : -1;
                for (int y = this.yp + step; y != yp; y += step) {
                    if (Board.getPiece(this.xp * 64, y * 64) != null) return false;
                }
            } else {
                int step = xp > this.xp ? 1 : -1;
                for (int x = this.xp + step; x != xp; x += step) {
                    Pieces block = Board.getPiece(x * 64, this.yp * 64);
                    if (block != null) return false;

                }
            }

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            if (target != null && target.white == this.white) return false;

            return true;
        }

            // bishop-like
            if (Math.abs(xp - this.xp) == Math.abs(yp - this.yp)) {

                int stepX = xp > this.xp ? 1 : -1;
                int stepY = yp > this.yp ? 1 : -1;

                int cx = this.xp + stepX;
                int cy = this.yp + stepY;

                while (cx != xp && cy != yp) {
                    Pieces block = Board.getPiece(cx * 64, cy * 64);
                    if (block != null) return false;

                    cx += stepX;
                    cy += stepY;
                }

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            if (target != null && target.white == this.white) return false;

            return true;
        }

            return false;
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
        
        
    
    private boolean isIllegal(Pieces King, int kx, int ky){

    if(King.name.equals("King")){
        for ( Pieces piece : ps ){

            if(piece != King && piece.white != King.white){
                if (piece.name.equals("King")) continue;
                int save = Board.currentPlayer;                     
                Board.currentPlayer = piece.white ? 1 : 0;          

                boolean attacks = piece.canmove(King.xp,King.yp);

                Board.currentPlayer = save;                         

                if(attacks){
                    return true;
                }
            }
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
    

     
}
