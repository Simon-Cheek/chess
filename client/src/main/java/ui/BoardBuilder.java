package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardBuilder {


    public static String buildBoard(ChessGame game) {

        String light = EscapeSequences.SET_BG_COLOR_DARK_GREY;
        String dark = EscapeSequences.SET_BG_COLOR_BLACK;
        String text = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        String reset = EscapeSequences.RESET_TEXT_COLOR;

        StringBuilder whiteBoard = new StringBuilder();

        whiteBoard.append(text).append("    h    g    f    e    d    c    b    a\n").append(reset);

        for (int row = 8; row >= 1; row--) {
            whiteBoard.append(text).append(row).append(reset).append(" ");
            for (int col = 1; col <= 8; col++) {
                String bgColor = (row + col) % 2 == 0 ? light: dark;
                String piece = getPiece(game, row, col);
                whiteBoard.append(bgColor).append(" ").append(piece).append(" ");
            }
            whiteBoard.append(reset).append(EscapeSequences.RESET_BG_COLOR)
                    .append(" ").append(text).append(row).append(reset).append("\n");
        }

        whiteBoard.append(text).append("    h    g    f    e    d    c    b    a\n").append(reset);

        return whiteBoard.toString();
    }


    private static String getPiece(ChessGame game, int row, int column) {

        ChessBoard board = game.getBoard();
        ChessPosition pos = new ChessPosition(row, column);
        ChessPiece piece = board.getPiece(pos);

        if (piece == null) { return "   "; }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch(piece.getPieceType()) {
                case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_KNIGHT;
                case BISHOP -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_BISHOP;
                case ROOK -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_ROOK;
                case KING -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_QUEEN;
                default -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch(piece.getPieceType()) {
                case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_KNIGHT;
                case BISHOP -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_BISHOP;
                case ROOK -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_ROOK;
                case KING -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_QUEEN;
                default -> EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
