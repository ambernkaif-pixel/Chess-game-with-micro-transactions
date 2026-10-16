
package CHESS;

import static CHESS.Board.ps;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
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
    //boolean twosteps=false;
    boolean stalemate = false;
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
//     System.out.println(
//    name + " "
//    + (white ? "WHITE" : "BLACK")
//    + " checking against "
//    + (King.white ? "WHITE" : "BLACK")
//    + " king at "
//    + King.xp + "," + King.yp
//);

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
                   King = p;
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
            boolean attack = false;
            int save = Board.currentPlayer;
            Board.currentPlayer = p.white ? 1 : 0;
            if (p.canmove(king.xp,king.yp)){
                if (p.white != king.white){
                    attack = p.canmove(king.xp, king.yp);
                }
            }


            Board.currentPlayer = save;

            if (attack) {
//                if(p.white != king.white){
//                System.out.println(
//                    p.name + " attacks king at "
//                    + king.xp + "," + king.yp
//                );
               return true;
            }
//            }
            }
        }

        return false;
    }

//    public void promoteTo(Pieces option) {
//
//        String promotedName = option.name;
//
//        int px = this.xp;
//        int py = this.yp;
//        boolean color = this.white;
//
//        ps.remove(this);
//
//        new Pieces(px, py, promotedName, color, ps);
//
//        promotion = false;
//    }

        public boolean isCheckmate(){
        System.out.println(
        "isCheckmate called by "
        + name + " "
        + (white ? "WHITE" : "BLACK")
    );

        Pieces King = getKing(true);

        System.out.println(
        "Checking mate on "
        + (King.white ? "WHITE" : "BLACK")
        + " king at "
        + King.xp + "," + King.yp
        );

        int col= King.xp;
        int row = King.yp;

        Pieces attacker = null;

        for ( Pieces piece : ps ){
            if(!piece.name.equals("King") && piece.white!=King.white){
                if(piece.canmove(col,row)){
                    int save = Board.currentPlayer;
                    Board.currentPlayer = piece.white ? 1 : 0;
                    Board.currentPlayer = save;
                    boolean attack = piece.canmove(col, row);

                    if (attack) {
                        attacker = piece;
                        break;
                    }
                }
            }
        }

        if(attacker == null){
            return false;
        }

        System.out.println("KING CAN MOVE = " + kingCanMove(King));

        if(kingCanMove(King)){
            System.out.println("King can move");
            return false;
        }
        else{
            //any piece could block the attack also
            int coldiff = Math.abs(attacker.xp-King.xp);
            int rowdiff = Math.abs(attacker.yp-King.yp);

            if (coldiff == 0){
                //attack from vertical direction
                if (attacker.yp < row){
                    //below the king
                    for (int r = attacker.yp; r < row ;r++){
                        for (Pieces piece : ps){
                            if(piece!=King &&
                               piece.white == King.white &&
                               piece.canmove(attacker.xp, r)){
                                return false;
                            }
                        }
                    }
                }

                if(attacker.yp > row){
                    //above the king
                    for (int r = attacker.yp; r > row ;r--){
                        for (Pieces piece : ps){
                            if(piece!=King &&
                               piece.white == King.white &&
                               piece.canmove(attacker.xp, r)){
                                return false;
                            }
                        }
                    }
                }
            }

            else if (rowdiff == 0){
                // attack from horizontal direction
                if(attacker.xp < col){
                    //checking from the left
                    for (int c = attacker.xp; c < col ;c++){
                        for (Pieces piece : ps){
                            if(piece!=King &&
                               piece.white == King.white &&
                               piece.canmove(c,attacker.yp )){
                                return false;
                            }
                        }
                    }
                }

                if(attacker.xp > col){
                    for (int c = attacker.xp; c > col ;c--){
                        for (Pieces piece : ps){
                            if(piece!=King &&
                               piece.white == King.white &&
                               piece.canmove(c,attacker.yp )){
                                return false;
                            }
                        }
                    }
                }
            }

            else if(rowdiff == coldiff){
                //diagonal attack
                if(attacker.yp < row){
                    // above the king
                    if(attacker.xp < col){
                        //upper left
                        for  (int c = attacker.xp, r = attacker.yp; c < col;c++,r++){
                            for(Pieces piece : ps){
                                if(piece != King &&
                                   piece.white == King.white &&
                                   piece.canmove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }

                    if(attacker.xp > col){
                        //upper right
                        for  (int c = attacker.xp, r = attacker.yp; c > col; c--,r++){
                            for(Pieces piece : ps){
                                if(piece != King &&
                                   piece.white == King.white &&
                                   piece.canmove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }
                }

                if(attacker.yp > row){
                    // above the king
                    if(attacker.xp < col){
                        //lower left
                        for  (int c = attacker.xp, r = attacker.yp; c < col;c++,r--){
                            for(Pieces piece : ps){
                                if(piece != King &&
                                   piece.white == King.white &&
                                   piece.canmove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }

                    if(attacker.xp > col){
                        //lower right
                        for  (int c=attacker.xp, r = attacker.yp; c > col; c--,r--){
                            for(Pieces piece : ps){
                                if(piece != King &&
                                   piece.white == King.white &&
                                   piece.canmove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            else{
                //horsey attack
                for (Pieces piece : ps) {
                    if (piece != King && piece.white == King.white &&
                        piece.canmove(attacker.xp, attacker.yp)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    private boolean kingCanMove(Pieces King){
        //can the king move to any square?
        if (isValidMove(King,-1,-1)) {return true;}
        if (isValidMove(King,0,-1)) {return true;}
        if (isValidMove(King,1,-1)) {return true;}
        if (isValidMove(King,-1,0)) {return true;}
        if (isValidMove(King,1,0)) {return true;}
        if (isValidMove(King,-1,1)) {return true;}
        if (isValidMove(King,0,1)) {return true;}
        if (isValidMove(King,1,1)) {return true;}
        System.out.println("d7 " + isValidMove(King,-1,1));
        System.out.println("e7 " + isValidMove(King,0,1));
        System.out.println("f7 " + isValidMove(King,1,1));
        System.out.println("d8 " + isValidMove(King,-1,0));
        System.out.println("f8 " + isValidMove(King,1,0));
        return false;
        
    }
    private boolean isValidMove(Pieces King, int srow,int scol){
        boolean isValidMove = false;
        //temp update kings position to check
        int oldX = King.xp;
        int oldY = King.yp;
        int newx = King.xp + scol;
        int newy = King.yp + srow;
        Pieces removed = null;
        if(King.canmove(newx,newy)){
            Pieces target = Board.getPiece(newx*64, newy*64);
            if(target != null && target.white != King.white){
                removed=target;
                ps.remove(target);   
            }
            King.xp = newx;
            King.yp = newy;

            if(!isIllegal(King,newx,newy)){
                isValidMove = true;
            }
        }
        //reset position and restore removed pieces
        if(removed != null){
        ps.add(removed);
        }
        King.xp=oldX;
        King.yp=oldY;
        
        return isValidMove;
    }
    public boolean isStalemate(){
        int count =0;
        for (Pieces p : ps){
            if(p.white != white){
                count ++;
            }
        }
        System.out.println("count = " + count);
        Pieces king = getKing(true);
        boolean move = kingCanMove(king);
        //if king in check no stalemate
        for (Pieces p : ps) {
            if (p.white == white && p.canmove(king.xp, king.yp)) return false;
        } 
        
        //checking any piece except king can move
        for (Pieces p : ps){
            if(!(p.name.equals("King")) && p.white == king.white){
                for (int x=0 ; x<8 ; x++){
                    for ( int y=0;y<8;y++){
                        if(p.canmove(x,y)){
                            System.out.println(
                            (p.white ? "WHITE " : "BLACK ")
                            + p.name
                            + " at "
                            + p.xp + "," + p.yp
                            + " can move to "
                            + x + "," + y
                        );
                            return false;
                        }
                    }
                }
                
            }
        }
        //last check that only king remains
        
        if(!move){
            System.out.println("stalemate detected ");
            return true;
            }
        
        return false;
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
        
        
        this.xp=xp;
        this.yp=yp;
        x=xp*64;
        y=yp*64;
        this.hasMoved= true;
        
        if (canPromote()){
            promotion = true;
        }
        if (canPromote()) {

            Scanner sc = new Scanner(System.in);

            System.out.println("Promote to:");
            System.out.println("Q - Queen");
            System.out.println("R - Rook");
            System.out.println("B - Bishop");
            System.out.println("N - Knight");

            String choice = sc.nextLine().toUpperCase();

            switch(choice) {
                case "Q":
                    name = "Queen";
                    break;

                case "R":
                    name = "Rook";
                    break;

                case "B":
                    name = "Bishop";
                    break;

                case "N":
                    name = "Knight";
                    break;

                default:
                    name = "Queen";
            }

            loadImage();
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

            Pieces captured = Board.getPiece(xp * 64, yp * 64);

            // own piece
            if (captured != null && captured.white == this.white) {
                return false;
            }

            oldXP = this.xp;
            oldYP = this.yp;

            if (captured != null) {
                ps.remove(captured);
            }

            this.xp = xp;
            this.yp = yp;

            illegal = isIllegal(this, xp, yp);

            this.xp = oldXP;
            this.yp = oldYP;

            if (captured != null) {
                ps.add(captured);
            }

            if (illegal) {
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
            if (name.equals("Bishop")) {

            if (Math.abs(xp - this.xp) != Math.abs(yp - this.yp))
                return false;

            int stepX = xp > this.xp ? 1 : -1;
            int stepY = yp > this.yp ? 1 : -1;

            int cx = this.xp + stepX;
            int cy = this.yp + stepY;

            while (cx != xp) {

                if (Board.getPiece(cx * 64, cy * 64) != null)
                    return false;

                cx += stepX;
                cy += stepY;
            }

            Pieces target = Board.getPiece(xp * 64, yp * 64);

            if (target != null && target.white == this.white)
                return false;

            return true;
}
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
            if (Board.enPassantTarget != null && Board.enPassantTarget.white != this.white) {

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
                if (piece.name.equals("King")) {
                    if (Math.abs(King.xp - piece.xp) <= 1 &&
                        Math.abs(King.yp - piece.yp) <= 1) {
                        return true;   // squares next to the enemy king are illegal
                    }
                    continue;
                }
                int save = Board.currentPlayer;                     
                Board.currentPlayer = piece.white ? 1 : 0;          

                boolean attacks;
                if (piece.name.equals("Pawn")) {
                    int dir = piece.white ? -1 : 1;
                    attacks = Math.abs(King.xp - piece.xp) == 1
                              && King.yp == piece.yp + dir;
                } else {
                    attacks = piece.canmove(King.xp, King.yp);
                }

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
