package ui;

import game.*;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class BoardComponent extends JPanel implements PuyoBoard.SimStepHandler {
    PuyoGame game;
    HashMap<Puyo, PuyoComponent> puyoComps = new HashMap<>();

    public BoardComponent(){
        setLayout(null);

    }

    public void getNextPair(){

    }

    public void setGame(PuyoGame g){
        this.removeAll();
        puyoComps.clear();
        game = g;
        for(Puyo puyo : g.activePuyo()){
            PuyoComponent pc = new PuyoComponent(puyo);
            add(pc);
            pc.updatePos(game.puyoPos(puyo));
        }
    }

    @Override
    public void handleStep(boolean isFinished) {
        for(Puyo p : puyoComps.keySet()){
            BoardPos b = game.puyoPos(p);
            if(b == null)
                remove(puyoComps.remove(p));
            else
                puyoComps.get(p).updatePos(b);
        }
    }
}

