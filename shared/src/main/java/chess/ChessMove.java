package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {

        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {

        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {

        if (this.promotionPiece != null) {
            return this.promotionPiece;
        } else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += 10000 * this.getStartPosition().getColumn();
        code += 1000 * this.getStartPosition().getRow();
        code += 100 + this.getEndPosition().getColumn();
        code += 10 * this.getEndPosition().getRow();
        if (this.getPromotionPiece() != null) {
            code += 1;
        }
        return code * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof ChessMove other)) { return false; }
        if (!this.getStartPosition().equals(other.getStartPosition())) { return false; }
        if (!this.getEndPosition().equals(other.getEndPosition())) { return false; }
        if (this.getPromotionPiece() == null) {
            return other.getPromotionPiece() == null;
        }
        return this.getPromotionPiece().equals(other.getPromotionPiece());
    }

    @Override
    public String toString() {
        return "{" + this.getEndPosition().getRow() + ":" + this.getEndPosition().getColumn() + "}";
    }
}
