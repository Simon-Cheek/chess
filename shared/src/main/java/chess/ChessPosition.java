package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(ChessPosition  obj) {
        this.row = obj.getRow();
        this.col = obj.getColumn();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChessPosition)) return false;
        ChessPosition other = (ChessPosition) obj;
        return this.getRow() == other.getRow() && this.getColumn() == other.getColumn();
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += 10 * this.row;
        code += this.col;
        code *= 137;
        return code;
    }

    @Override
    public String toString() {
        return this.row + " x " + this.col;
    }
}
