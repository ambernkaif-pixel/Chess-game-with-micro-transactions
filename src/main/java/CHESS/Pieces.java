package CHESS;

import static CHESS.Board.ps;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;

public class Pieces {
    int x;
    int y;
    int xp;
    int yp;
    boolean white;
    LinkedList<Pieces> ps;
    String name;
    Pieces checkingP;
    boolean gameOver;
    boolean hasMoved=false;
    boolean stalemate = false;
    public ArrayList<Pieces> promoPiece = new ArrayList<>();
    public boolean promotion;
    Image img;

    public Pieces(int xp, int yp, String n, 
            boolean white, LinkedList<Pieces> ps) {
        this.xp = xp;
        this.yp = yp;
        x = xp * 64;
        y = yp * 64;
        this.white = white;
        this.ps = ps;
        name = n;
        loadImage();
        ps.add(this);
    }

    private void loadImage() {
        String color = white ? "W" : "B";
        String path = "/CHESS/" + name + color + ".png";
        java.net.URL url = getClass().getResource(path);
        if (url == null) return;
        img = new ImageIcon(url).getImage();
    }

    private boolean canPromote() {
        if (!name.equals("Pawn")) return false;
        if (white && yp == 0) {
            promotion = true;
            return true;
        }
        if (!white && yp == 7) {
            promotion = true;
            return true;
        }
        return false;
    }

    public void promoteGUI() {
        JFrame popup = new JFrame("Promote Pawn");
        popup.setSize(700, 300);
        popup.setLocationRelativeTo(null);
        popup.setLayout(new GridLayout(1, 4));

        String color = white ? "W" : "B";
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};

        for (String opt : options) {

            String path = "/CHESS/" + opt + color + ".png";
            ImageIcon rawIcon = new ImageIcon(getClass().getResource(path));

            Image scaled = rawIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);

            JButton btn = new JButton(icon);
            btn.setPreferredSize(new Dimension(200, 200));
            btn.setMinimumSize(new Dimension(200, 200));
            btn.setMaximumSize(new Dimension(200, 200));

            btn.setBorder(null);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);

            btn.addActionListener(e -> {
                name = opt;
                loadImage();
                promotion = false;
                popup.dispose();
            });

            popup.add(btn);
        }

        popup.setVisible(true);
    }

    public boolean kinginCheck() {
        Pieces King = getKing(true);
        if (King == null) return false;
        boolean result = this.canmove(King.xp, King.yp);
        checkingP = result ? this : null;
        return result;
    }

    private Pieces getKing(boolean opponent) {
        Pieces King = null;
        for (Pieces p : ps) {
            if (opponent) {
                if (p.name.equals("King") && p.white != this.white) King = p;
            } else {
                if (p.name.equals("King") && p.white == this.white) King = p;
            }
        }
        return King;
    }

    public boolean cancaptureking() {
        Pieces king = getKing(false);
        if (king == null) return false;

        for (Pieces p : ps) {
            if (p.white != king.white) {
                int save = Board.currentPlayer;
                Board.currentPlayer = p.white ? 1 : 0;
                boolean attack = p.canmove(king.xp, king.yp);
                Board.currentPlayer = save;
                if (attack) return true;
            }
        }
        return false;
    }

    public boolean isCheckmate() {
        Pieces King = getKing(true);
        int col = King.xp;
        int row = King.yp;

        Pieces attacker = null;

        for (Pieces piece : ps) {
            if (!piece.name.equals("King") && piece.white != King.white) {
                boolean attack = piece.canmove(col, row);
                if (attack) {
                    attacker = piece;
                    break;
                }
            }
        }

        if (attacker == null) return false;
        if (kingCanMove(King)) return false;

        int coldiff = Math.abs(attacker.xp - King.xp);
        int rowdiff = Math.abs(attacker.yp - King.yp);

        if (coldiff == 0) {
            if (attacker.yp < row) {
                for (int r = attacker.yp; r < row; r++) {
                    for (Pieces piece : ps) {
                        if (piece != King && piece.white == King.white && piece.canmove(attacker.xp, r)) return false;
                    }
                }
            }
            if (attacker.yp > row) {
                for (int r = attacker.yp; r > row; r--) {
                    for (Pieces piece : ps) {
                        if (piece != King && piece.white == King.white && piece.canmove(attacker.xp, r)) return false;
                    }
                }
            }
        }

        else if (rowdiff == 0) {
            if (attacker.xp < col) {
                for (int c = attacker.xp; c < col; c++) {
                    for (Pieces piece : ps) {
                        if (piece != King && piece.white == King.white && piece.canmove(c, attacker.yp)) return false;
                    }
                }
            }
            if (attacker.xp > col) {
                for (int c = attacker.xp; c > col; c--) {
                    for (Pieces piece : ps) {
                        if (piece != King && piece.white == King.white && piece.canmove(c, attacker.yp)) return false;
                    }
                }
            }
        }

        else if (rowdiff == coldiff) {
            int stepX = attacker.xp < col ? 1 : -1;
            int stepY = attacker.yp < row ? 1 : -1;

            int cx = attacker.xp;
            int cy = attacker.yp;

            while (cx != col && cy != row) {
                for (Pieces piece : ps) {
                    if (piece != King && piece.white == King.white && piece.canmove(cx, cy)) return false;
                }
                cx += stepX;
                cy += stepY;
            }
        }

        else {
            for (Pieces piece : ps) {
                if (piece != King && piece.white == King.white && piece.canmove(attacker.xp, attacker.yp)) return false;
            }
        }

        return true;
    }

    private boolean kingCanMove(Pieces King) {
        if (isValidMove(King, -1, -1)) return true;
        if (isValidMove(King, 0, -1)) return true;
        if (isValidMove(King, 1, -1)) return true;
        if (isValidMove(King, -1, 0)) return true;
        if (isValidMove(King, 1, 0)) return true;
        if (isValidMove(King, -1, 1)) return true;
        if (isValidMove(King, 0, 1)) return true;
        if (isValidMove(King, 1, 1)) return true;
        return false;
    }

    private boolean isValidMove(Pieces King, int srow, int scol) {
        int oldX = King.xp;
        int oldY = King.yp;
        int newx = King.xp + scol;
        int newy = King.yp + srow;
        Pieces removed = null;

        if (King.canmove(newx, newy)) {
            Pieces target = Board.getPiece(newx * 64, newy * 64);
            if (target != null && target.white != King.white) {
                removed = target;
                ps.remove(target);
            }
            King.xp = newx;
            King.yp = newy;

            if (!isIllegal(King, newx, newy)) {
                if (removed != null) ps.add(removed);
                King.xp = oldX;
                King.yp = oldY;
                return true;
            }
        }

        if (removed != null) ps.add(removed);
        King.xp = oldX;
        King.yp = oldY;
        return false;
    }

    public boolean isStalemate() {
        Pieces king = getKing(true);
        boolean move = kingCanMove(king);

        for (Pieces p : ps) {
            if (p.white == white && p.canmove(king.xp, king.yp)) return false;
        }

        for (Pieces p : ps) {
            if (!p.name.equals("King") && p.white == king.white) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canmove(x, y)) return false;
                    }
                }
            }
        }

        if (!move) return true;
        return false;
    }

    public void move(int xp, int yp) {
        int oldXP = this.xp;
        int oldYP = this.yp;

        Pieces target = Board.getPiece(xp * 64, yp * 64);
        if (target != null && target.white != white) target.kill();
        else if (target != null) return;

        this.xp = xp;
        this.yp = yp;
        x = xp * 64;
        y = yp * 64;
        hasMoved = true;

        if (name.equals("Pawn") && Board.enPassantTarget != null) {
            Pieces ep = Board.enPassantTarget;
            int moveValue = white ? -1 : 1;
            if (target == null &&
                xp == ep.xp &&
                yp == ep.yp + moveValue &&
                ep.yp == oldYP &&
                Math.abs(ep.xp - oldXP) == 1) {
                ep.kill();
            }
        }

        if (name.equals("Pawn")) {
            if (Math.abs(yp - oldYP) == 2) {
                Board.enPassantTarget = this;
            } else {
                Board.enPassantTarget = null;
            }
        } else {
            Board.enPassantTarget = null;
        }

        if (canPromote()) promoteGUI();

        Board.currentPlayer = 1 - Board.currentPlayer;
    }

    public void kill() {
        ps.remove(this);
    }

    public boolean canmove(int xp, int yp) {
        if (!withinboard(xp, yp)) return false;

        if (name.equals("King")) {
            int dx = Math.abs(xp - this.xp);
            int dy = Math.abs(yp - this.yp);

            int oldXP = this.xp;
            int oldYP = this.yp;

            this.xp = xp;
            this.yp = yp;

            boolean illegal = isIllegal(this, xp, yp);

            this.xp = oldXP;
            this.yp = oldYP;

            if (illegal) return false;

            if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {
                Pieces captured = Board.getPiece(xp * 64, yp * 64);
                if (captured != null && captured.white == this.white) return false;
                return true;
            }

            return false;
        }

        if (name.equals("Knight")) {
            if (Math.abs(xp - this.xp) * Math.abs(yp - this.yp) == 2) {
                Pieces target = Board.getPiece(xp * 64, yp * 64);
                if (target != null && target.white == this.white) return false;
                return true;
            }
            return false;
        }

        if (name.equals("Rook")) {
            if (xp == this.xp) {
                int step = yp > this.yp ? 1 : -1;
                for (int y = this.yp + step; y != yp; y += step) {
                    if (Board.getPiece(this.xp * 64, y * 64) != null) return false;
                }
            } else if (yp == this.yp) {
                int step = xp > this.xp ? 1 : -1;
                for (int x = this.xp + step; x != xp; x += step) {
                    if (Board.getPiece(x * 64, this.yp * 64) != null) return false;
                }
            } else return false;

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            if (target != null && target.white == this.white) return false;
            return true;
        }

        if (name.equals("Bishop")) {
            if (Math.abs(xp - this.xp) != Math.abs(yp - this.yp)) return false;

            int stepX = xp > this.xp ? 1 : -1;
            int stepY = yp > this.yp ? 1 : -1;

            int cx = this.xp + stepX;
            int cy = this.yp + stepY;

            while (cx != xp) {
                if (Board.getPiece(cx * 64, cy * 64) != null) return false;
                cx += stepX;
                cy += stepY;
            }

            Pieces target = Board.getPiece(xp * 64, yp * 64);
            if (target != null && target.white == this.white) return false;
            return true;
        }

        if (name.equals("Queen")) {
            if (xp == this.xp || yp == this.yp) {
                if (xp == this.xp) {
                    int step = yp > this.yp ? 1 : -1;
                    for (int y = this.yp + step; y != yp; y += step) {
                        if (Board.getPiece(this.xp * 64, y * 64) != null) return false;
                    }
                } else {
                    int step = xp > this.xp ? 1 : -1;
                    for (int x = this.xp + step; x != xp; x += step) {
                        if (Board.getPiece(x * 64, this.yp * 64) != null) return false;
                    }
                }

                Pieces target = Board.getPiece(xp * 64, yp * 64);
                if (target != null && target.white == this.white) return false;
                return true;
            }

            if (Math.abs(xp - this.xp) == Math.abs(yp - this.yp)) {
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
                if (target != null && target.white == this.white) return false;
                return true;
            }

            return false;
        }

        if (name.equals("Pawn")) {
            int moveValue = white ? -1 : 1;
            Pieces target = Board.getPiece(xp * 64, yp * 64);

            if (xp == this.xp && yp == this.yp + moveValue && target == null) return true;

            int startRank = white ? 6 : 1;
            if (this.yp == startRank && xp == this.xp && yp == this.yp + 2 * moveValue) {
                Pieces front1 = Board.getPiece(this.xp * 64, (this.yp + moveValue) * 64);
                Pieces front2 = Board.getPiece(this.xp * 64, (this.yp + 2 * moveValue) * 64);
                if (front1 == null && front2 == null) return true;
            }

            if (Math.abs(xp - this.xp) == 1 && yp == this.yp + moveValue) {
                if (target != null && target.white != this.white) return true;

                Pieces ep = Board.enPassantTarget;
                if (ep != null && ep.white != this.white) {
                    if (ep.xp == xp && ep.yp == this.yp) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isIllegal(Pieces King, int kx, int ky) {
        for (Pieces piece : ps) {
            if (piece != King && piece.white != King.white) {
                if (piece.name.equals("King")) {
                    if (Math.abs(King.xp - piece.xp) <= 1 && Math.abs(King.yp - piece.yp) <= 1) return true;
                    continue;
                }

                int save = Board.currentPlayer;
                Board.currentPlayer = piece.white ? 1 : 0;

                boolean attacks;
                if (piece.name.equals("Pawn")) {
                    int dir = piece.white ? -1 : 1;
                    attacks = Math.abs(King.xp - piece.xp) == 1 && King.yp == piece.yp + dir;
                } else {
                    attacks = piece.canmove(King.xp, King.yp);
                }

                Board.currentPlayer = save;

                if (attacks) return true;
            }
        }
        return false;
    }

    public boolean withinboard(int xp, int yp) {
        return xp >= 0 && xp <= 7 && yp >= 0 && yp <= 7;
    }
}