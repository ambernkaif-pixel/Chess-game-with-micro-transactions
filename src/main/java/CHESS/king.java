
package CHESS;

import java.util.LinkedList;


public class king extends Pieces {
    public king (int xp, int yp, boolean white,LinkedList<Pieces> ps){
        super(xp,yp,"King",white,ps);
    }
    @Override
    public boolean canmove(int xp,int yp){
        if(!withinboard(xp,yp)) return false;
        if (Math.abs(xp-this.xp) + Math.abs(yp-this.yp) ==1 ||Math.abs(xp-this.xp) * Math.abs(yp-this.yp) ==1 ){
            return true;
        }
        Pieces piece = Board.getPiece(xp*64,yp*64);
        if (piece != null && piece.white == this. white){
            return false;
        }
        else{return true;}
        
    }
}

