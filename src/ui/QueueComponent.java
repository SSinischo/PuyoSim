package ui;

import game.Parameters;
import game.Puyo;
import game.PuyoGame;
import game.PuyoMove;

import javax.swing.*;
import java.awt.*;

public class QueueComponent extends JComponent {
    private PuyoGame game;

    public QueueComponent(){
        setPreferredSize(new Dimension(100, -1));
    }

    public void setGame(PuyoGame g){
        game = g;
        repaint();
    }

    @Override
    public void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(3));
        g.drawLine(0, 0, 0, getHeight());

        if(game == null)
            return;

        int x = getWidth() / 2;
        int yStep = getHeight() / (Parameters.QUEUE_PREVIEW + 2);
        int d = yStep / 2;

        for(int i=0; i<Parameters.QUEUE_PREVIEW; i++){
            PuyoMove pm = game.getQueued(i+1);
            int offset = yStep*i + (10*i);
            g.setColor(Puyo.COLOR_MAP.get(pm.puyo2.color));
            g.fillOval(x, (yStep/4)+offset, d, d);
            g.setColor(Puyo.COLOR_MAP.get(pm.puyo1.color));
            g.fillOval(x, (3*yStep/4)+offset, d, d);
        }
    }
}
