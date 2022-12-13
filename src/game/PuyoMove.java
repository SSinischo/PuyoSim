package game;

import java.util.ArrayList;

public class PuyoMove {
    Puyo puyo1;
    Puyo puyo2;
    int dropCol = 2;
    int dropOrientation;
    int moveChain;
    int moveScore;

    public PuyoMove(Puyo puyo1, Puyo puyo2){
        this.puyo1 = puyo1;
        this.puyo2 = puyo2;
    }

    public PuyoMove(Puyo puyo1, Puyo puyo2, int dropCol, int dropOrientation){
        this.puyo1 = puyo1;
        this.puyo2 = puyo2;
        this.dropCol = dropCol;
        this.dropOrientation = dropOrientation;
    }

    public static ArrayList<PuyoMove> possibleMoves(PuyoBoard b, Puyo puyo1, Puyo puyo2){
        ArrayList<PuyoMove> moves = new ArrayList<>();
        for(int i=0; i<Parameters.COLUMNS; i++){
            for(int j=0; j<4; j++){
                PuyoMove m = new PuyoMove(puyo1, puyo2, i, j);
                if(b.isMoveValid(m))
                    moves.add(m);
            };
        }
        return moves;
    }
}
