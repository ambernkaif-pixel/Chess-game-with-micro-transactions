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
    public static Pieces enPassantTarget = null;
    public  static LinkedList<Pieces> ps = new LinkedList<>();
    public static Pieces selectedPiece = null;
    public static boolean gameOver = false;
    public static String winner = "";
    public static boolean stalemate= false;
    public static void drawString(Graphics g, String text, int x, int y) {
        g.drawString(text, x, y);
    }

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
                if (Board.selectedPiece != null && Board.selectedPiece.promotion) {
                Board.drawString(g, "Promote to:", 520, 50);

                for (Pieces option : Board.selectedPiece.promoPiece) {
                    g.drawImage(option.img, option.x, option.y, 64, 64, null);
                    }   
                }
                if (Board.gameOver && !(Board.stalemate)) {

                    g.setColor(new Color(0, 0, 0, 180));
                    g.fillRect(0, 0, 512, 512);

                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 40));

                    FontMetrics fm = g.getFontMetrics();

                    String text = "GAME OVER";

                    int x = (512 - fm.stringWidth(text)) / 2;

                    g.drawString(text, x, 230);

                    g.setFont(new Font("Arial", Font.BOLD, 30));

                    x = (512 - g.getFontMetrics().stringWidth(Board.winner)) / 2;

                    g.drawString(Board.winner, x, 280);
                }
                if(Board.stalemate){
                    g.setColor(new Color(0, 0, 0, 180));
                    g.fillRect(0, 0, 512, 512);

                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 30));

                    FontMetrics fm = g.getFontMetrics();

                    String text = "GAME OVER BY STALEMATE";
                    
                    int x = (512 - fm.stringWidth(text)) / 2;
                    g.drawString(text, x, 256);
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
                if(Board.gameOver){
                    return;
                }
                selectedPiece=getPiece(e.getX(),e.getY());
                selectedPiece = getPiece(e.getX(), e.getY());

                if(selectedPiece != null){

                    if((selectedPiece.white && currentPlayer != 1) ||
                       (!selectedPiece.white && currentPlayer != 0)){

                        selectedPiece = null;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();
                
                
//                // PROMOTION MODE
//                if (Board.selectedPiece != null && Board.selectedPiece.promotion) {
//
//                    for (Pieces option : Board.selectedPiece.promoPiece) {
//                        if (mx >= option.x && mx < option.x + 64 &&
//                            my >= option.y && my < option.y + 64) {
//
//                            Board.selectedPiece.promoteTo(option);
//
//                            Board.selectedPiece.promotion = false;
//                            Board.selectedPiece.promoPiece.clear();
//
//                            board.repaint();
//                            return;  // STOP 
//                        }
//                    }
//
//                    return;
//                }

                // NORMAL MOVE
                int xp = mx / 64;
                int yp = my / 64;
                if (selectedPiece != null && selectedPiece.canmove(xp, yp)) {

                    int oldXP = selectedPiece.xp;
                    int oldYP = selectedPiece.yp;

                    Pieces capturedPiece = getPiece(xp * 64, yp * 64);

                    // En passant simulation
                    if (capturedPiece == null &&
                        selectedPiece.name.equals("Pawn") &&
                        Math.abs(xp - oldXP) == 1) {

                        Pieces epPawn = getPiece(xp * 64, oldYP * 64);

                        if (epPawn != null &&
                            epPawn == Board.enPassantTarget) {

                            capturedPiece = epPawn;
                        }
                    }

                    // SIMULATE
                    selectedPiece.xp = xp;
                    selectedPiece.yp = yp;
                    //PIN
//                    if ( selectedPiece.canmove(xp, yp) && selectedPiece.kinginCheck()){
//                        selectedPiece.xp = oldXP;
//                        selectedPiece.yp = oldYP;
//                    }
                    if (capturedPiece != null) {
                        ps.remove(capturedPiece);
                    }

                    boolean illegal = selectedPiece.cancaptureking();

                    // RESTORE SIMULATION
                    selectedPiece.xp = oldXP;
                    selectedPiece.yp = oldYP;

                    if (capturedPiece != null && !ps.contains(capturedPiece)) {
                        ps.add(capturedPiece);
                    }

                    if (illegal) {

                        selectedPiece.x = oldXP * 64;
                        selectedPiece.y = oldYP * 64;

                        selectedPiece = null;

                        board.repaint();
                        return;
                    }

                    // REAL MOVE
                   selectedPiece.move(xp, yp);

                   selectedPiece = null;

                   for (Pieces p : ps) {
                        if (p.kinginCheck()) {

                            System.out.println("Check!");

                            if (p.isCheckmate()) {

                                Board.gameOver = true;

                                if (p.white) {
                                    Board.winner = "WHITE WINS";
                                } else {
                                    Board.winner = "BLACK WINS";
                                }

                                System.out.println("Game Over");
                            }

                            break;
                        }
                        else if(p.name.equals("King")&& p.white==(currentPlayer == 0)&& p.isStalemate()){ 
                            System.out.println("STALEMATE BLOCK REACHED");
                            stalemate = true;
                            Board.gameOver=true;
                            
                        }
                    }

                    } else if (selectedPiece != null) {

                        selectedPiece.x = selectedPiece.xp * 64;
                        selectedPiece.y = selectedPiece.yp * 64;

                        selectedPiece = null;
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

