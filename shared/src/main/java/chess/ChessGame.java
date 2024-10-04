package chess;

import java.util.ArrayList;
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

        // Get starting piece and color
        ChessPiece startPiece = this.board.getPiece(startPosition);
        if (startPiece == null) return null;
        TeamColor startColor = startPiece.getTeamColor();

        // Get List of possible moves
        Collection<ChessMove> moves = startPiece.pieceMoves(this.board, startPosition);
        if (moves == null || moves.isEmpty()) return null;

        // Initialize list of valid moves
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();

        // Simulate Moves one at a time to ensure move doesn't put current king in check
        for (ChessMove move : moves) {

            boolean valid = false;

            // Store End Piece for roll back
            ChessPiece targetPiece = this.board.getPiece(move.getEndPosition());

            // Simulate Move
            this.board.addPiece(move.getEndPosition(), startPiece);
            this.board.addPiece(move.getStartPosition(), null);

            if (!isInCheck(startColor)) valid = true;

            // Roll back move
            this.board.addPiece(move.getStartPosition(), startPiece);
            this.board.addPiece(move.getEndPosition(), targetPiece);

            if (valid) validMoves.add(move);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        if (!this.validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException("Not a valid move");

        ChessPiece startingPiece = this.board.getPiece(move.getStartPosition());

        // Place starting position piece on top of end position
        this.board.addPiece(move.getEndPosition(), startingPiece);

        // Make starting position null
        this.board.addPiece(move.getStartPosition(), null);

        // Promote if necessary
        if (move.getPromotionPiece() != null) {
            ChessPiece promotionPiece = new ChessPiece(startingPiece.getTeamColor(), move.getPromotionPiece());
            this.board.addPiece(move.getEndPosition(), promotionPiece);
        }

        // Change turn
        this.setTeamTurn(this.getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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
     * Locates the King of the given team color, returns null if none
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    private ChessPosition findKing(TeamColor teamColor) {
        // Iterate through board until King is found of teamColor
        for (int i = 1; i < 9; i ++) {
            for (int ii = 1; ii < 9; ii++) {

                ChessPosition newPosition = new ChessPosition(i, ii);
                ChessPiece newPiece = this.board.getPiece(newPosition);

                if (newPiece != null && newPiece.getPieceType().equals(ChessPiece.PieceType.KING) &&
                        newPiece.getTeamColor().equals(teamColor)) {

                    return newPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition != null) return isInDanger(kingPosition);
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition != null) {

            // Simulate every possible move and check if King is in check after
            Collection<ChessMove> validMoves = new ArrayList<ChessMove>();

            for (int i = 1; i < 9; i ++) {
                for (int ii = 1; ii < 9; ii++) {

                    ChessPosition newPosition = new ChessPosition(i, ii);
                    ChessPiece newPiece = this.board.getPiece(newPosition);

                    // Check every possible move by own color and make sure there is at least one valid move
                    if (newPiece != null && newPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validPieceMoves = this.validMoves(newPosition);
                        System.out.println("PRINTING OUT ALL VALID PIECE MOVES FOR");
                        System.out.println(newPiece);
                        System.out.println(validPieceMoves);
                        validMoves.addAll(validPieceMoves);
                    }
                }
            }
            System.out.println("PRINTING ALL VALID FINAL MOVES");
            System.out.println(validMoves);
            return validMoves.isEmpty(); // If no valid moves, King is in Checkmate
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition != null) {

            // Simulate every possible move and check if King is in check after
            Collection<ChessMove> validMoves = new ArrayList<ChessMove>();

            for (int i = 1; i < 9; i ++) {
                for (int ii = 1; ii < 9; ii++) {

                    ChessPosition newPosition = new ChessPosition(i, ii);
                    ChessPiece newPiece = this.board.getPiece(newPosition);

                    // Check every possible move by own color and make sure there is at least one valid move
                    if (newPiece != null && newPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validPieceMoves = this.validMoves(newPosition);
                        if (validPieceMoves != null) validMoves.addAll(validPieceMoves);
                    }
                }
            }
            return validMoves.isEmpty(); // If no valid moves, King is in Checkmate
        }
        return false;
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
