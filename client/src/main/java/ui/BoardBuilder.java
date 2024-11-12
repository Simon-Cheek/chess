package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class BoardBuilder {

    public static String buildBoard(ChessGame game) {

        return "";
    }


    private String getPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch(piece.getPieceType()) {
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                default -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch(piece.getPieceType()) {
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                default -> EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
