package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamColor;
    private ChessBoard board;

    public ChessGame() {
        this.teamColor = TeamColor.WHITE; // Initialize turn to White
        this.board = new ChessBoard();
        this.board.resetBoard(); // Create new board and initialize to new game
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given position is in danger
     */
    private boolean isInDanger(ChessPosition position) {
        for (int i = 1; i < 9; i ++) {
            for (int ii = 1; ii < 9; ii++) {

                // Iterate through all positions and check if it can move to the given position
                ChessPosition newPosition = new ChessPosition(i, ii);
                ChessPiece newPiece = this.board.getPiece(newPosition);
                if (newPiece != null) {
                    Collection<ChessMove> moves = newPiece.pieceMoves(this.board, newPosition);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(position)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // Iterate through board until King is found of teamColor
        for (int i = 1; i < 9; i ++) {
            for (int ii = 1; ii < 9; ii++) {

                ChessPosition newPosition = new ChessPosition(i, ii);
                ChessPiece newPiece = this.board.getPiece(newPosition);

                if (newPiece != null && newPiece.getPieceType().equals(ChessPiece.PieceType.KING) &&
                        newPiece.getTeamColor().equals(teamColor)) {

                    return isInDanger(newPosition);
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
