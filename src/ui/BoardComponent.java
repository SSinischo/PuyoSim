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
    boolean isIdle = true;
    HashMap<Puyo, PuyoComponent> puyoComps = new HashMap<>();

    public BoardComponent(){
        setFocusable(true);
        setLayout(null);
        setMinimumSize(new Dimension(530, 1000));
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
        updateNextPair();
    }

    @Override
    public void handleStep(boolean isFinished) {
        for(Puyo p : Set.copyOf(puyoComps.keySet())){
            BoardPos b = game.puyoPos(p);
            if(b == null)
                remove(puyoComps.remove(p));
            else
                puyoComps.get(p).updatePos(b);
        }
        if(isFinished)
            isIdle = true;
    }

    void updateNextPair(){
        PuyoMove nextPair = game.next();
        int p1r, p1c, p2r, p2c;
        p1c = nextPair.dropCol;
        p2c = nextPair.dropOrientation==1? p1c+1 : nextPair.dropOrientation==3? p1c-1 : p1c;
        p1r = nextPair.dropOrientation==2? Parameters.ROWS-1 : Parameters.ROWS-2;
        p2r = nextPair.dropOrientation==0? p1r+1 : p1r;
        PuyoComponent pc1 = puyoComps.get(nextPair.puyo1);
        PuyoComponent pc2 = puyoComps.get(nextPair.puyo2);
        if(pc1 == null){
            pc1 = new PuyoComponent(nextPair.puyo1, this);
            pc2 = new PuyoComponent(nextPair.puyo2, this);
            puyoComps.put(nextPair.puyo1, pc1);
            puyoComps.put(nextPair.puyo2, pc2);
            add(pc1);
            add(pc2);
        }
        pc1.updatePos(new BoardPos(p1c, p1r));
        pc2.updatePos(new BoardPos(p2c, p2r));
    }


    public void onKeyPress(KeyEvent e) {
        if (!isIdle)
            return;
        if (e.getKeyChar() == 'z') {
            game.moveLeft();
            updateNextPair();
        } else if (e.getKeyChar() == 'x') {
            game.moveRight();
            updateNextPair();
        } else if (e.getKeyChar() == '.') {
            game.rotateLeft();
            updateNextPair();
        } else if (e.getKeyChar() == '/') {
            game.rotateRight();
            updateNextPair();
        } else if (e.getKeyChar() == ' ') {
            isIdle = false;
            game.dropSteps(this);
            updateNextPair();
        }
    }
}

