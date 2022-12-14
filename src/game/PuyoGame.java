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

    public void moveLeft(){
        PuyoMove m = next();
        if(m.dropCol == 0 || m.dropOrientation == 3 && m.dropCol == 1)
            return;
        m.dropCol -= 1;
    }

    public void moveRight(){
        PuyoMove m = next();
        if(m.dropCol == Parameters.COLUMNS-1 || m.dropOrientation == 1 && m.dropCol == Parameters.COLUMNS-2)
            return;
        m.dropCol += 1;
    }

    public void rotateLeft(){
        PuyoMove m = next();
        m.dropOrientation -= m.dropOrientation == 0? -3 : 1;
        if(m.dropCol == 0 && m.dropOrientation == 3)
            m.dropCol += 1;
        else if(m.dropCol == Parameters.COLUMNS-1 && m.dropOrientation == 1)
            m.dropCol -= 1;
    }

    public void rotateRight(){
        PuyoMove m = next();
        m.dropOrientation += m.dropOrientation == 3? -3 : 1;
        if(m.dropCol == 0 && m.dropOrientation == 3)
            m.dropCol += 1;
        else if(m.dropCol == Parameters.COLUMNS-1 && m.dropOrientation == 1)
            m.dropCol -= 1;
    }

    public int drop(boolean inSteps){
        PuyoMove m = next();
        if(!isMoveValid(m)) return Integer.MIN_VALUE;
        queuePos++;
        makeMove(m);
        simulateNextStep();
        while(inSteps && hasMoreSteps())
            simulateNextStep();
        return currentScore;
    }

    public int dropStep(){
        simulateNextStep();
        return currentScore;
    }

}
