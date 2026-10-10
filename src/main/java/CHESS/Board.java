package CHESS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

/**
 *
 * @author amber
 */
public class Board {
    public static int currentPlayer = 1; // white starts
    public  static LinkedList<Pieces> ps = new LinkedList<>();
    public static Pieces selectedPiece = null;
    public static void main(String[] args){
        
        // WHITE PIECES
        new Pieces(0, 6, "Pawn", true, ps);
        new Pieces(1, 6, "Pawn", true, ps);
        new Pieces(2, 6, "Pawn", true, ps);
        new Pieces(3, 6, "Pawn", true, ps);
        new Pieces(4, 6, "Pawn", true, ps);
        new Pieces(5, 6, "Pawn", true, ps);
        new Pieces(6, 6, "Pawn", true, ps);
        new Pieces(7, 6, "Pawn", true, ps);

        new Pieces(0, 7, "Rook", true, ps);
        new Pieces(1, 7, "Knight", true, ps);
        new Pieces(2, 7, "Bishop", true, ps);
        new Pieces(3, 7, "Queen", true, ps);
        new Pieces(4, 7, "King", true, ps);
        new Pieces(5, 7, "Bishop", true, ps);
        new Pieces(6, 7, "Knight", true, ps);
        new Pieces(7, 7, "Rook", true, ps);

        // BLACK PIECES
        new Pieces(0, 1, "Pawn", false, ps);
        new Pieces(1, 1, "Pawn", false, ps);
        new Pieces(2, 1, "Pawn", false, ps);
        new Pieces(3, 1, "Pawn", false, ps);
        new Pieces(4, 1, "Pawn", false, ps);
        new Pieces(5, 1, "Pawn", false, ps);
        new Pieces(6, 1, "Pawn", false, ps);
        new Pieces(7, 1, "Pawn", false, ps);

        new Pieces(0, 0, "Rook", false, ps);
        new Pieces(1, 0, "Knight", false, ps);
        new Pieces(2, 0, "Bishop", false, ps);
        new Pieces(3, 0, "Queen", false, ps);
        new Pieces(4, 0, "King", false, ps);
        new Pieces(5, 0, "Bishop", false, ps);
        new Pieces(6, 0, "Knight", false, ps);
        new Pieces(7, 0, "Rook", false, ps);

    

        JFrame board = new JFrame();
        board.setBounds(10,10,512,512);//seting board
        board.setUndecorated(true);
        board.setLayout(new BorderLayout());
        JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g){
                boolean white = true;
                for(int y=0;y<8;y++){
                    for(int x = 0; x<8;x++){
                        if (white){
                            g.setColor(Color.white.darker());
                        }
                        else{
                            g.setColor(Color.green.darker());
                        }
                        g.fillRect(x*64, y*64, 64, 64);
                    white=!white;
                    }
                    white=!white;
                }
                for (Pieces p : ps) {
                    g.drawImage(p.img, p.x, p.y, 64, 64, null);
                }

             
            }
            
            
        };
        board.add(panel, BorderLayout.CENTER);
        //adding panels to board
        board.add(panel);
        board.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedPiece != null){
                    selectedPiece.x =e.getX();
                    selectedPiece.y =e.getY();
                    board.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        } );
        board.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println(getPiece(e.getX(),e.getY()).white?"white"+getPiece(e.getX(),e.getY()).name:"black"+getPiece(e.getX(),e.getY()).name);
                selectedPiece=getPiece(e.getX(),e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int xp = e.getX() / 64;
                int yp = e.getY() / 64;

                if (selectedPiece != null && selectedPiece.canmove(xp, yp)) {
                    selectedPiece.move(xp, yp);
                } else {
                    // back to original square
                    selectedPiece.x = selectedPiece.xp * 64;
                    selectedPiece.y = selectedPiece.yp * 64;
                }

                board.repaint();
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        board.setDefaultCloseOperation(3);
        board.setVisible(true);
    }
    public static Pieces getPiece(int x , int y){
        int xp=x/64;
        int yp=y/64;
        for(Pieces p: ps){
            if(p.xp==xp && p.yp==yp){
                return p;
            }
        }
        return null;
    }
}

