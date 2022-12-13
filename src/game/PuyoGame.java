package game;

import java.util.LinkedList;
import java.util.Random;

public class PuyoGame extends PuyoBoard{
    PuyoMove[] queue = new PuyoMove[Parameters.QUEUE_SIZE];
    int queuePos = 0;

    public PuyoGame(){
        for(int i=0; i<queue.length; i++){
            queue[i] = new PuyoMove(new Puyo(), new Puyo());
        }
    }

    public PuyoMove next(){
        return queue[(queuePos)%queue.length];
    }

    public PuyoMove getQueued(int idx){
        return queue[(queuePos+idx)%queue.length];
    }

    public boolean moveLeft(){
        PuyoMove m = next();
        if(m.dropCol == 0 || m.dropOrientation == 3 && m.dropCol == 1)
            return false;
        m.dropCol -= 1;
        return true;
    }

    public boolean moveRight(){
        PuyoMove m = next();
        if(m.dropCol == Parameters.COLUMNS-1 || m.dropOrientation == 1 && m.dropCol == Parameters.COLUMNS-2)
            return false;
        m.dropCol += 1;
        return true;
    }

    public void rotateLeft(){
        PuyoMove m = next();
        m.dropOrientation -= m.dropOrientation == 0? -3 : 1;
        if(m.dropCol == 0 && m.dropOrientation == 3)
            m.dropCol += 1;
    }

    public void rotateRight(){
        PuyoMove m = next();
        m.dropOrientation += m.dropOrientation == 3? -3 : 1;
        if(m.dropCol == Parameters.COLUMNS-1 && m.dropOrientation == 1)
            m.dropCol -= 1;
    }

    public int drop(){
        return dropSteps(null);
    }

    public int dropSteps(SimStepHandler handler){
        PuyoMove m = next();
        if(!isMoveValid(m)) return Integer.MIN_VALUE;
        queuePos++;
        makeMove(m);
        while(hasMoreSteps())
            simulateNextStep(handler);
        return currentScore;
    }

}
