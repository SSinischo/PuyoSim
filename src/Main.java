import game.PuyoGame;
import ui.BoardComponent;
import ui.QueueComponent;

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
        JPanel p = new JPanel();

        QueueComponent queue = new QueueComponent();
        BoardComponent board = new BoardComponent(queue);

        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(board);
        p.add(queue);

        f.add(p);
        f.pack();
        f.setVisible(true);
        board.setGame(new PuyoGame());
    }
}