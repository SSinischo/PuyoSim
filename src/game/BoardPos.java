package game;

import java.io.Serializable;

public class BoardPos implements Serializable, Comparable<BoardPos> {
    public int row;
    public int col;

    public BoardPos(int col, int row){
        this.col = col;
        this.row = row;
    }

    public BoardPos left(){
        return col != 0 ? new BoardPos(col-1, row) : null;
    }

    public BoardPos right(){
        return col != Parameters.COLUMNS-1 ? new BoardPos(col+1, row) : null;
    }

    public BoardPos up(){
        return row != Parameters.ROWS-1 ? new BoardPos(col, row+1) : null;
    }

    public BoardPos down(){
        return row != 0 ? new BoardPos(col, row-1) : null;
    }

    public BoardPos[] neighbors(){ return new BoardPos[]{up(), right(), down(), left()}; }

    @Override
    public int compareTo(BoardPos x) {
        if(x.col == col){
            if(x.row == row){
                return 0;
            }
            return x.row > row ? -1 : 1;
        }
        return x.col > col ? -1 : 1;
    }

    @Override
    public boolean equals(Object x){
        if(x.getClass() == BoardPos.class){
            return ((BoardPos) x).col == col && ((BoardPos) x).row == row;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return row * Parameters.COLUMNS + col;
    }
}
