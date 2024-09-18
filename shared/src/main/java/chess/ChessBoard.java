package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[8 - position.getRow()][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[8 - position.getRow()][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];
        ChessInitBoard.addWhitePieces(this);
        ChessInitBoard.addBlackPieces(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ChessBoard)) return false;
        ChessBoard other = (ChessBoard) obj;

        for (int i = 1; i < 9; i++) {
            for (int ii = 1; i < 9; i++) {
                ChessPosition current = new ChessPosition(i, ii);
                if (this.getPiece(current) == null) {
                    System.out.println("null");
                    return other.getPiece(current) == null;
                }
                if (!(this.getPiece(current).equals(other.getPiece(current)))) return false;
            }
        }
        return true;
    }
}
