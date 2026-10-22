package CHESS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

public class Board extends JPanel {

    public static int currentPlayer = 1; // 1 = white, 0 = black
    public static Pieces enPassantTarget = null;
    public static LinkedList<Pieces> ps = new LinkedList<>();
    public static Pieces selectedPiece = null;
    public static boolean gameOver = false;
    public static String winner = "";
    public static boolean stalemate = false;
    public static int whitePoints = 0;
    public static int blackPoints = 0;

    // CHEAT FLAGS
    public static boolean deleteMode = false;
    public static boolean cheatUsedThisTurn = false;

    public static void startGame() {

        ps.clear();
        selectedPiece = null;
        gameOver = false;
        winner = "";
        stalemate = false;
        whitePoints = 0;
        blackPoints = 0;
        currentPlayer = 1;
        deleteMode = false;
        cheatUsedThisTurn = false;

        initPieces();

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Board board = new Board();
        board.setBackground(new Color(245, 230, 200));

        frame.setLayout(new BorderLayout());
        frame.add(board, BorderLayout.CENTER);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(new Color(245, 230, 200));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JButton cheatsButton = new JButton("CHEATS");
        cheatsButton.setPreferredSize(new Dimension(260, 80));
        cheatsButton.setFont(new Font("Arial", Font.BOLD, 28));
        cheatsButton.setFocusPainted(false);
        cheatsButton.setBackground(new Color(255, 200, 120));
        cheatsButton.addActionListener(e -> openCheatMenu(frame));

        sidebar.add(cheatsButton);
        frame.add(sidebar, BorderLayout.EAST);

        frame.setVisible(true);

        frame.addMouseMotionListener(board.motionListener(frame));
        frame.addMouseListener(board.clickListener(frame));
    }

    private static void initPieces() {

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int boardSize = Math.min(getHeight(), getWidth());
        int tileSize = boardSize / 8;
        double scale = tileSize / 64.0;

        int offsetX = 0;
        int offsetY = (getHeight() - boardSize) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        boolean white = true;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                g2.setColor(white ? Color.white : Color.green.darker());
                g2.fillRect(x * 64, y * 64, 64, 64);
                white = !white;
            }
            white = !white;
        }
        

        for (Pieces p : ps) {
            g2.drawImage(p.img, p.x, p.y, 64, 64, null);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Serif", Font.BOLD, 28));
        g2.drawString("White: " + whitePoints, 520, 40);
        g2.drawString("Black: " + blackPoints, 520, 80);

        if (deleteMode) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.drawString("DELETE", 520, 140);
        }

        if (gameOver && !stalemate) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, 512, 512);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g2.getFontMetrics();
            String text = "GAME OVER";
            int x = (512 - fm.stringWidth(text)) / 2;
            g2.drawString(text, x, 230);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            x = (512 - g2.getFontMetrics().stringWidth(winner)) / 2;
            g2.drawString(winner, x, 280);
        }

        if (stalemate) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, 512, 512);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g2.getFontMetrics();
            String text = "GAME OVER BY STALEMATE";
            int x = (512 - fm.stringWidth(text)) / 2;
            g2.drawString(text, x, 256);
        }
    }

    //CHEAT MENU 

    public static void openCheatMenu(JFrame frame) {

        if (cheatUsedThisTurn) {
            JOptionPane.showMessageDialog(frame, "You already used a cheat this turn.");
            return;
        }

        String[] cheats = {"Delete a Piece (20)", "Bribe Arbiter (100)"};
        String choice = (String) JOptionPane.showInputDialog(
                frame,
                "Choose a cheat:",
                "Cheat Menu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                cheats,
                cheats[0]
        );

        if (choice == null) return;

        boolean whiteTurn = (currentPlayer == 1);
        int points = whiteTurn ? whitePoints : blackPoints;

        if (choice.contains("Delete")) {
            if (points < 20) {
                JOptionPane.showMessageDialog(frame, "Not enough points.");
                return;
            }

            if (whiteTurn) whitePoints -= 20;
            else blackPoints -= 20;

            deleteMode = true;
            JOptionPane.showMessageDialog(frame, "Click a piece to delete.");
        }

        else if (choice.contains("Bribe")) {
            if (points < 100) {
                JOptionPane.showMessageDialog(frame, "Not enough points.");
                return;
            }

            gameOver = true;
            winner = whiteTurn ? "WHITE WINS (Bribed Arbiter)" : "BLACK WINS (Bribed Arbiter)";
            cheatUsedThisTurn = true;
        }

        frame.repaint();
    }

    private MouseMotionListener motionListener(JFrame frame) {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                int boardSize = Math.min(frame.getWidth(), frame.getHeight());
                int tileSize = boardSize / 8;
                double scale = tileSize / 64.0;

                int offsetY = (frame.getHeight() - boardSize) / 2;

                if (selectedPiece != null) {
                    selectedPiece.x = (int)(e.getX() / scale);
                    selectedPiece.y = (int)((e.getY() - offsetY) / scale);
                    frame.repaint();
                }
            }

            @Override public void mouseMoved(MouseEvent e) {}
        };
    }

    private MouseListener clickListener(JFrame frame) {
        return new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (gameOver) return;

                int boardSize = Math.min(frame.getWidth(), frame.getHeight());
                int tileSize = boardSize / 8;
                double scale = tileSize / 64.0;

                int offsetY = (frame.getHeight() - boardSize) / 2;

                int mx = (int)(e.getX() / scale);
                int my = (int)((e.getY() - offsetY) / scale);

                selectedPiece = getPiece(mx, my);

                if (selectedPiece != null) {
                    if ((selectedPiece.white && currentPlayer != 1) ||
                        (!selectedPiece.white && currentPlayer != 0)) {
                        selectedPiece = null;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (gameOver) return;

                int boardSize = Math.min(frame.getWidth(), frame.getHeight());
                int tileSize = boardSize / 8;
                double scale = tileSize / 64.0;

                int offsetY = (frame.getHeight() - boardSize) / 2;

                int mx = (int)(e.getX() / scale);
                int my = (int)((e.getY() - offsetY) / scale);

                int xp = mx / 64;
                int yp = my / 64;

                // DELETE MODE CLICK HANDLING
                if (deleteMode) {

                    // snap any dragged piece back to its square
                    if (selectedPiece != null) {
                        selectedPiece.x = selectedPiece.xp * 64;
                        selectedPiece.y = selectedPiece.yp * 64;
                        selectedPiece = null;
                    }

                    Pieces target = (mx < 0 || my < 0) ? null : getPiece(xp * 64, yp * 64);
                    boolean whiteTurn = (currentPlayer == 1);

                    // INVALID TARGET, REFUND + DO NOT END TURN
                    if (target == null || target.name.equals("King") || target.name.equals("Queen")) {

                        if (whiteTurn) whitePoints += 20;
                        else blackPoints += 20;

                        JOptionPane.showMessageDialog(frame, "Invalid target. Points refunded.");

                        deleteMode = false;
                        frame.repaint();
                        return;
                    }

                    // VALID DELETE, REMOVE + END TURN
                    ps.remove(target);
                    JOptionPane.showMessageDialog(frame, "Piece deleted.");

                    deleteMode = false;
                    cheatUsedThisTurn = true;

                    // END TURN
                    currentPlayer = whiteTurn ? 0 : 1;
                    cheatUsedThisTurn = false;

                    frame.repaint();
                    return;
                }

                if (selectedPiece == null) {
                    frame.repaint();
                    return;
                }

                // Dropped off the board or back on the same square , cancel no turn used
                if (mx < 0 || my < 0 || xp > 7 || yp > 7
                        || (xp == selectedPiece.xp && yp == selectedPiece.yp)) {
                    selectedPiece.x = selectedPiece.xp * 64;
                    selectedPiece.y = selectedPiece.yp * 64;
                    selectedPiece = null;
                    frame.repaint();
                    return;
                }

                if (selectedPiece.canmove(xp, yp)) {

                    int oldXP = selectedPiece.xp;
                    int oldYP = selectedPiece.yp;
                    boolean moverIsWhite = selectedPiece.white;

                    // Simulate the capture (does not award points)
                    Pieces capturedPiece = getPiece(xp * 64, yp * 64);
                    int capturedIndex = -1;
                    if (capturedPiece != null) {
                        capturedIndex = ps.indexOf(capturedPiece);
                        ps.remove(capturedPiece);
                    }

                    selectedPiece.xp = xp;
                    selectedPiece.yp = yp;

                    boolean illegal = selectedPiece.cancaptureking();

                    selectedPiece.xp = oldXP;
                    selectedPiece.yp = oldYP;

                    if (illegal) {
                        // restore the captured piece, reject the move
                        if (capturedPiece != null) {
                            ps.add(capturedIndex, capturedPiece);
                        }
                        selectedPiece.x = oldXP * 64;
                        selectedPiece.y = oldYP * 64;
                        selectedPiece = null;
                        frame.repaint();
                        return;
                    }

                    // Move is legal, award points
                    if (capturedPiece != null) {
                        int value = 0;
                        if (capturedPiece.name.equals("Pawn")) value = 1;
                        else if (capturedPiece.name.equals("Knight")) value = 3;
                        else if (capturedPiece.name.equals("Bishop")) value = 3;
                        else if (capturedPiece.name.equals("Rook")) value = 5;
                        else if (capturedPiece.name.equals("Queen")) value = 9;

                        if (moverIsWhite) whitePoints += value;
                        else blackPoints += value;
                    }

                    selectedPiece.move(xp, yp);
                    selectedPiece = null;

                    //Turn logic
                    currentPlayer = moverIsWhite ? 0 : 1;
                    cheatUsedThisTurn = false;

                    for (Pieces p : ps) {
                        if (p.kinginCheck()) {

                            if (p.white) whitePoints += 5;
                            else blackPoints += 5;

                            if (p.isCheckmate()) {
                                gameOver = true;
                                winner = p.white ? "WHITE WINS" : "BLACK WINS";
                            }
                            break;
                        }
                        else if (p.name.equals("King") &&
                                 p.white == (currentPlayer == 1) &&
                                 p.isStalemate()) {

                            stalemate = true;
                            gameOver = true;
                        }
                    }

                } else {
                    selectedPiece.x = selectedPiece.xp * 64;
                    selectedPiece.y = selectedPiece.yp * 64;
                    selectedPiece = null;
                }

                frame.repaint();
            }

            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        };
    }

    public static Pieces getPiece(int x, int y) {
        int xp = x / 64;
        int yp = y / 64;
        for (Pieces p : ps) {
            if (p.xp == xp && p.yp == yp) return p;
        }
        return null;
    }

    public static void drawString(Graphics g, String text, int x, int y) {
        g.drawString(text, x, y);
    }
}
