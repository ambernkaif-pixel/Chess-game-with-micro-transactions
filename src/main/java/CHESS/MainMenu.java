package CHESS;

import javax.swing.*;
import java.awt.*;

public class MainMenu {

    public static void show() {

        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Image img = new ImageIcon(
                MainMenu.class.getResource("/CHESS/MainMenu.png")
        ).getImage();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        int fontSize = (int)(screen.height * 0.05);
        Font menuFont = new Font("Serif", Font.BOLD, fontSize);

        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            }
        };
        background.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        Color beige = new Color(245, 230, 200);
        Color orangeBorder = new Color(255, 140, 0);

        int bw = (int)(screen.width * 0.20);
        int bh = (int)(screen.height * 0.10);
        Dimension buttonSize = new Dimension(bw, bh);

        int borderThickness = (int)(screen.height * 0.005);

        JButton playBtn = new JButton("PLAY");
        playBtn.setFont(menuFont);
        playBtn.setForeground(Color.BLACK);
        playBtn.setBackground(beige);
        playBtn.setFocusPainted(false);
        playBtn.setBorder(BorderFactory.createLineBorder(orangeBorder, borderThickness));
        playBtn.setPreferredSize(buttonSize);
        playBtn.setMaximumSize(buttonSize);
        playBtn.setMinimumSize(buttonSize);

        playBtn.addActionListener(e -> {
            frame.dispose();
            Board.startGame();
        });

        JButton exitBtn = new JButton("EXIT");
        exitBtn.setFont(menuFont);
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setBackground(beige);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(BorderFactory.createLineBorder(orangeBorder, borderThickness));
        exitBtn.setPreferredSize(buttonSize);
        exitBtn.setMaximumSize(buttonSize);
        exitBtn.setMinimumSize(buttonSize);

        exitBtn.addActionListener(e -> System.exit(0));

        int offset = (int)(screen.height * -0.30);
        int leftOffset = (int)(screen.width * 0.35);

        JPanel playWrap = new JPanel();
        playWrap.setOpaque(false);
        playWrap.setLayout(new BoxLayout(playWrap, BoxLayout.X_AXIS));
        playWrap.setBorder(BorderFactory.createEmptyBorder(0, leftOffset, 0, 0));
        playWrap.add(playBtn);

        JPanel exitWrap = new JPanel();
        exitWrap.setOpaque(false);
        exitWrap.setLayout(new BoxLayout(exitWrap, BoxLayout.X_AXIS));
        exitWrap.setBorder(BorderFactory.createEmptyBorder(0, leftOffset, 0, 0));
        exitWrap.add(exitBtn);

        int spacing = (int)(screen.height * 0.02);

        bottomPanel.add(Box.createVerticalGlue());
        bottomPanel.add(playWrap);
        bottomPanel.add(Box.createVerticalStrut(spacing));
        bottomPanel.add(exitWrap);
        bottomPanel.add(Box.createVerticalStrut(offset));
        bottomPanel.add(Box.createVerticalGlue());

        background.add(bottomPanel, BorderLayout.WEST);

        frame.add(background);
        frame.setVisible(true);
    }
}