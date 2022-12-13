import game.PuyoGame;
import ui.BoardComponent;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("Look and Feel not set");
        }
        JFrame f = new JFrame("PuyoSim");

        BoardComponent board = new BoardComponent();

        f.add(board);
        f.pack();
        f.setVisible(true);
        board.setGame(new PuyoGame());
    }
    public static class MainPanel extends JPanel {
        static BoardComponent board;
        public MainPanel(){

        }
    }
}