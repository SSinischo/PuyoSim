package game;

import java.util.*;

public class PuyoBoard {
    public abstract interface SimStepHandler{
        public void handleStep(boolean isFinished);
    }

    static final int[] CHAIN_POWER = new int[]{0, 40, 320, 640, 1280, 2560, 3840, 5120, 6400, 7680, 8960, 10240, 11520, 12800, 14080, 15360, 16640, 17920, 19200, 20480};
    static final int[] COLOR_BONUS = new int[]{0, 0, 3, 6, 12, 24};
    static final int[] GROUP_BONUS = new int[]{0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 10};

    private Puyo[] board = new Puyo[Parameters.ROWS*Parameters.COLUMNS];
    private HashMap<Puyo, BoardPos> puyoPos = new HashMap<>();
    private HashSet<BoardPos> nextStepOrigins = new HashSet<>();
    PuyoMove currentMove;
    int currentScore;

    public static PuyoBoard getCopy(PuyoBoard b){
        PuyoBoard c = new PuyoBoard();
        c.board = b.board.clone();
        c.currentMove = b.currentMove;
        c.currentScore = b.currentScore;
        return c;
    }

    public Set<Puyo> activePuyo(){ return puyoPos.keySet(); }

    public Puyo puyoAt(BoardPos p){
        return board[p.row * Parameters.COLUMNS + p.col];
    }

    public BoardPos puyoPos(Puyo puyo){
        return puyoPos.get(puyo);
    }

    private void setPuyo(BoardPos p, Puyo puyo) {
        board[p.row * Parameters.COLUMNS + p.col] = puyo;
        puyoPos.put(puyo, p);
    }

    private void clear(BoardPos p){
        puyoPos.remove(puyoAt(p));
        board[p.row * Parameters.COLUMNS + p.col] = null;
    }

    private void movePuyo(BoardPos from, BoardPos to){
        setPuyo(to, puyoAt(from));
        clear(from);
    }

    public boolean isMoveValid(PuyoMove m){
        if(m.dropOrientation % 2 == 0)
            return puyoAt(new BoardPos(m.dropCol, Parameters.ROWS-2)) == null;
        if(m.dropCol == 0 && m.dropOrientation == 3 || m.dropCol == Parameters.COLUMNS-1 && m.dropOrientation == 1)
            return false;
        if(puyoAt(new BoardPos(m.dropCol, Parameters.ROWS-1)) != null)
            return false;
        return puyoAt(new BoardPos(m.dropCol + (m.dropOrientation == 1 ? 1 : -1),
                Parameters.ROWS-1)) == null;
    }

    public boolean hasMoreSteps(){
        return !nextStepOrigins.isEmpty();
    }

    public void makeMove(PuyoMove m){
        currentMove = m;
        BoardPos p1, p2;
        if(m.dropOrientation % 2 != 0){
            p1 = new BoardPos(m.dropCol, Parameters.ROWS-1);
            p2 = new BoardPos(m.dropOrientation == 1? m.dropCol+1 : m.dropCol-1, Parameters.ROWS-1);
            while(p1.row > 0 && puyoAt(p1.down()) == null)
                p1.row--;
            while(p2.row > 0 && puyoAt(p2.down()) == null)
                p2.row--;
            setPuyo(p1, m.puyo1);
            setPuyo(p2, m.puyo2);
        }
        else{
            p1 = new BoardPos(m.dropCol, Parameters.ROWS - 2);
            while(p1.row > 0 && puyoAt(p1.down()) == null)
                p1.row--;
            p2 = new BoardPos(m.dropCol, p1.row+1);
            setPuyo(p1, m.dropOrientation==0? m.puyo1 : m.puyo2);
            setPuyo(p2, m.dropOrientation==0? m.puyo2 : m.puyo1);
        }
        nextStepOrigins.add(p1);
        nextStepOrigins.add(p2);
    }

    public void simulateNextStep(SimStepHandler handler){
        Set<BoardPos> willPop = new HashSet<>();
        Set<Character> poppedColors = new HashSet<>();
        int groupBonus = 0;
        for(BoardPos p : nextStepOrigins){
            if(willPop.contains(p)) continue;
            Set<BoardPos> group = findGroup(p);
            if(group.size() >= 4){
                willPop.addAll(group);
                poppedColors.add(puyoAt(p).color);
                groupBonus += group.size() >= GROUP_BONUS.length ? 10 : GROUP_BONUS[group.size()];
            }
        }
        nextStepOrigins.clear();
        if(!poppedColors.isEmpty()){
            currentMove.moveChain++;
            int pts = (10*poppedColors.size()) + (CHAIN_POWER[currentMove.moveChain] + COLOR_BONUS[poppedColors.size()] + groupBonus);
            currentMove.moveScore += pts;
            currentScore += pts;
            pop(willPop);
        }
        if(!hasMoreSteps()){
            if(puyoAt(new BoardPos(2, Parameters.ROWS-1)) != null)
                currentMove.moveScore = currentScore = Integer.MIN_VALUE;
        }
        if(handler != null)
            handler.handleStep(!hasMoreSteps());
    }

    private HashSet<BoardPos> findGroup(BoardPos origin){
        LinkedList<BoardPos> queue = new LinkedList<>();
        HashSet<BoardPos> group = new HashSet<>();
        char color = puyoAt(origin).color;
        queue.add(origin);
        group.add(origin);
        while(!queue.isEmpty()){
            BoardPos p = queue.pop();
            for(BoardPos nextP : p.neighbors()){
                if(nextP == null || puyoAt(nextP) == null || puyoAt(nextP).color != color || group.contains(nextP))
                    continue;
                queue.add(nextP);
                group.add(nextP);
            }
        }
        return group;
    }


    private void pop(Set<BoardPos> popped){
        int[] colFallStart = new int[Parameters.ROWS];
        for(BoardPos p : popped){
            clear(p);
            if(colFallStart[p.col] < Parameters.ROWS-p.row)
                colFallStart[p.col] = Parameters.ROWS-p.row;
        }
        for(int i=0; i<Parameters.COLUMNS; i++){
            int rowStart = Parameters.ROWS-colFallStart[i]+1;
            int spaces = 1;
            for(int j=rowStart; j<Parameters.ROWS; j++){
                BoardPos p = new BoardPos(i, j);
                if(puyoAt(p) == null){
                    if(popped.contains(p))
                        spaces++;
                    else
                        break;
                }
                else{
                    BoardPos newPos = new BoardPos(p.col, p.row-spaces);
                    movePuyo(p, newPos);
                    nextStepOrigins.add(newPos);
                }
            }
        }
    }
}
