package chess.moves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {

    public static ArrayList<ChessMove> getPawnMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);

        // White moves up one or up two if in starting position if unblocked
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {

            // Possible Moves
            ChessPosition inFront = new ChessPosition(position.getRow() + 1, position.getColumn());
            ChessPosition upTwo = new ChessPosition(position.getRow() + 2, position.getColumn());

            if (board.getPiece(inFront) == null) {

                // Move One Space Ahead
                ChessPiece.PieceType promotion = null;
                if (inFront.getRow() == 8) promotion = ChessPiece.PieceType.QUEEN;
                ChessMove moveUpOne = new ChessMove(position, inFront, promotion);
                if (ChessGetMoves.validateMove(moveUpOne)) moves.add(moveUpOne);

                // Move Two Spaces Ahead
                if (position.getRow() == 2 &&
                        (board.getPiece(upTwo) == null ||
                                board.getPiece(upTwo).getTeamColor() == ChessGame.TeamColor.BLACK)) {
                    ChessMove moveUpTwo = new ChessMove(position, upTwo, null);
                    if (ChessGetMoves.validateMove(moveUpTwo)) moves.add(moveUpTwo);
                }

            } else if (board.getPiece(inFront).getTeamColor() == ChessGame.TeamColor.BLACK) {
                // Move One Space Ahead
                ChessPiece.PieceType promotion = null;
                if (inFront.getRow() == 8) promotion = ChessPiece.PieceType.QUEEN;
                ChessMove moveUpOne = new ChessMove(position, inFront, promotion);
                if (ChessGetMoves.validateMove(moveUpOne)) moves.add(moveUpOne);
            }
        } else {

            // Black Pawn Moves
            ChessPosition inFront = new ChessPosition(position.getRow() - 1, position.getColumn());
            ChessPosition upTwo = new ChessPosition(position.getRow() - 2, position.getColumn());

            if (board.getPiece(inFront) == null) {

                // Move One Space Ahead
                ChessPiece.PieceType promotion = null;
                if (inFront.getRow() == 1) promotion = ChessPiece.PieceType.QUEEN;
                ChessMove moveUpOne = new ChessMove(position, inFront, promotion);
                if (ChessGetMoves.validateMove(moveUpOne)) moves.add(moveUpOne);

                // Move Two Spaces Ahead
                if (position.getRow() == 7 &&
                        (board.getPiece(upTwo) == null ||
                                board.getPiece(upTwo).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                    ChessMove moveUpTwo = new ChessMove(position, upTwo, null);
                    if (ChessGetMoves.validateMove(moveUpTwo)) moves.add(moveUpTwo);
                }

            } else if (board.getPiece(inFront).getTeamColor() == ChessGame.TeamColor.WHITE) {
                // Move One Space Ahead
                ChessPiece.PieceType promotion = null;
                if (inFront.getRow() == 1) promotion = ChessPiece.PieceType.QUEEN;
                ChessMove moveUpOne = new ChessMove(position, inFront, promotion);
                if (ChessGetMoves.validateMove(moveUpOne)) moves.add(moveUpOne);
            }
        }
        return moves;
    }
}
