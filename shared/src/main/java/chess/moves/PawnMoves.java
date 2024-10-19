package chess.moves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {

    public static ArrayList<ChessMove> getPawnMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor otherTeam = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

        // Determine direction of movement based on team
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 8 : 1;

        // List of all possible promotion pieces
        ChessPiece.PieceType[] promotionPieces = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
        };

        // Forward move (one space)
        ChessPosition inFront = new ChessPosition(position.getRow() + direction, position.getColumn());
        if (ChessGetMoves.validatePosition(inFront) && board.getPiece(inFront) == null) {
            // Add all promotion pieces for forward move if promotion row
            if (inFront.getRow() == promotionRow) {
                for (ChessPiece.PieceType promotion : promotionPieces) {
                    ChessMove moveUpOne = new ChessMove(position, inFront, promotion);
                    if (ChessGetMoves.validateMove(moveUpOne)) { moves.add(moveUpOne); };
                }
            } else {
                ChessMove moveUpOne = new ChessMove(position, inFront, null);
                if (ChessGetMoves.validateMove(moveUpOne)) { moves.add(moveUpOne); }
            }

            // Forward move (two spaces) if starting position
            if (position.getRow() == startRow) {
                ChessPosition upTwo = new ChessPosition(position.getRow() + 2 * direction, position.getColumn());

                if (ChessGetMoves.validatePosition(upTwo) &&
                        board.getPiece(upTwo) == null && board.getPiece(inFront) == null) {
                    ChessMove moveUpTwo = new ChessMove(position, upTwo, null);
                    if (ChessGetMoves.validateMove(moveUpTwo)) { moves.add(moveUpTwo); }
                }
            }
        }

        // Diagonal Captures
        int[] diagonalOffsets = {-1, 1};

        for (int offset : diagonalOffsets) {
            ChessPosition diagonalPos = new ChessPosition(position.getRow() + direction, position.getColumn() + offset);

            if (ChessGetMoves.validatePosition(diagonalPos)) {
                ChessPiece targetPiece = board.getPiece(diagonalPos);

                if (targetPiece != null && targetPiece.getTeamColor() == otherTeam && diagonalPos.getRow() == promotionRow) {
                    // Add all promotion pieces for diagonal captures if promotion row
                        for (ChessPiece.PieceType promotion : promotionPieces) {
                            ChessMove diagonalCapture = new ChessMove(position, diagonalPos, promotion);
                            if (ChessGetMoves.validateMove(diagonalCapture)) { moves.add(diagonalCapture); }
                        }
                } else if (targetPiece != null && targetPiece.getTeamColor() == otherTeam) {
                    ChessMove diagonalCapture = new ChessMove(position, diagonalPos, null);
                    if (ChessGetMoves.validateMove(diagonalCapture)) { moves.add(diagonalCapture); }
                }
            }
        }
        return moves;
    }
}
