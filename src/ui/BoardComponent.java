package ui;

import game.*;
import org.w3c.dom.css.Rect;
import ui.Utility.KeyPressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BoardComponent extends JPanel implements PuyoBoard.SimStepHandler {
    PuyoGame game;
    QueueComponent queueComp;
    boolean isIdle = true;
    HashMap<Puyo, PuyoComponent> puyoComps = new HashMap<>();

    public BoardComponent(QueueComponent queueComp){
        this.queueComp = queueComp;

        setFocusable(true);
        setLayout(null);
        setPreferredSize(new Dimension(50*(Parameters.COLUMNS), 50*Parameters.ROWS));
        setBackground(Color.BLACK);

        addKeyListener(new KeyPressListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyPress(e);
            }
        });
    }

    public void setGame(PuyoGame g){
        this.removeAll();
        puyoComps.clear();
        game = g;
        for(Puyo puyo : g.activePuyo()){
            PuyoComponent pc = new PuyoComponent(puyo, this);
            add(pc);
            pc.updatePos(game.puyoPos(puyo));
        }
        queueComp.setGame(g);
        repaint();
    }

    public void paintPuyo(Graphics2D g, Puyo puyo, int col, int row, int d){
        if(puyo != null){
            row = Parameters.ROWS - row - 1;
            g.setColor(Puyo.COLOR_MAP.get(puyo.color));
            g.fillOval(col*d, row*d, d, d);
            g.setColor(new Color(255, 255, 255, 50));
            g.setStroke(new BasicStroke(3));
            g.drawOval(col*d, row*d, d, d);
        }
    }

    @Override
    public void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        int w = getWidth() / Parameters.COLUMNS;
        int h = getHeight() / Parameters.ROWS;
        int d = Math.min(w, h);
        int r = d/2;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        for(int x=0; x<Parameters.COLUMNS; x++){
            for(int y=0; y<Parameters.ROWS; y++){
                paintPuyo(g, game.puyoAt(new BoardPos(x, y)), x, y, d);
            }
        }
        PuyoMove nextPair = game.next();
        int p1r, p1c, p2r, p2c;
        p1c = nextPair.dropCol;
        p2c = nextPair.dropOrientation==1? p1c+1 : nextPair.dropOrientation==3? p1c-1 : p1c;
        p1r = nextPair.dropOrientation==2? Parameters.ROWS-1 : Parameters.ROWS-2;
        p2r = nextPair.dropOrientation==0? Parameters.ROWS-1 : Parameters.ROWS-2;
        paintPuyo(g, nextPair.puyo1, p1c, p1r, d);
        paintPuyo(g, nextPair.puyo2, p2c, p2r, d);
    }

    @Override
    public void handleStep(boolean isFinished) {
        repaint();
        if(isFinished) {
            isIdle = true;
        }
    }


    public void onKeyPress(KeyEvent e) {
        if (!isIdle)
            return;
        if (e.getKeyChar() == 'z') {
            game.moveLeft();
            repaint();
        } else if (e.getKeyChar() == 'x') {
            game.moveRight();
            repaint();
        } else if (e.getKeyChar() == '.') {
            game.rotateLeft();
            repaint();
        } else if (e.getKeyChar() == '/') {
            game.rotateRight();
            repaint();
        } else if (e.getKeyChar() == ' ') {
            isIdle = false;
            game.dropSteps(this);
            queueComp.repaint();
            repaint();
        }
    }
}

