package ui;

import chess.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class BoardBuilder {

    static String light = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    static String dark = EscapeSequences.SET_BG_COLOR_BLACK;
    static String text = EscapeSequences.SET_TEXT_COLOR_YELLOW;
    static String reset = EscapeSequences.RESET_TEXT_COLOR;


    public static ArrayList<int[]> getHighlights(ChessGame game, ChessPosition pos) {
        ArrayList<int[]> hSquares = new ArrayList<int[]>();
        if (pos != null) {
            Collection<ChessMove> validMoves = game.validMoves(pos);
            for (ChessMove move : validMoves) {
                ChessPosition end = move.getEndPosition();
                hSquares.add(new int[]{end.getRow(), end.getColumn()});
            }
        }
        return hSquares;
    }

    public static boolean checkHighlight(ArrayList<int[]> squares, int row, int col) {
        if (squares.isEmpty()) { return false; }
        for (int[] coord : squares) {
            if (coord[0] == row && coord[1] == col) { return true; }
        }
        return false;
    }


    public static String buildBlackBoard(ChessGame game, ChessPosition pos) {

        ArrayList<int[]> hSquares = new ArrayList<int[]>();
        if (pos != null) {
            hSquares = getHighlights(game, pos);
        }

        StringBuilder blackBoard = new StringBuilder();
        blackBoard.append("Chess Board: Black Perspective\n");
        blackBoard.append(text).append("    a    b    c    d    e    f    g    h\n").append(reset);
        for (int row = 1; row <= 8; row++) {
            blackBoard.append(text).append(row).append(reset).append(" ");
            for (int col = 8; col >= 1; col--) {
                String bgColor = (row + col) % 2 == 0 ? dark: light;
                boolean shouldHighlight = checkHighlight(hSquares, row, col);
                if (shouldHighlight) { bgColor = EscapeSequences.SET_BG_COLOR_BLUE; }
                String piece = getPiece(game, row, col);
                blackBoard.append(bgColor).append(" ").append(piece).append(" ");
            }
            blackBoard.append(reset).append(EscapeSequences.RESET_BG_COLOR)
                    .append(" ").append(text).append(row).append(reset).append("\n");
        }
        blackBoard.append(text).append("    a    b    c    d    e    f    g    h\n").append(reset);

        return blackBoard.toString();
    }

    public static String buildWhiteBoard(ChessGame game, ChessPosition pos) {

        ArrayList<int[]> hSquares = new ArrayList<int[]>();
        if (pos != null) {
            hSquares = getHighlights(game, pos);
        }

        StringBuilder whiteBoard = new StringBuilder();
        whiteBoard.append("Chess Board: White Perspective\n");
        whiteBoard.append(text).append("    h    g    f    e    d    c    b    a\n").append(reset);
        for (int row = 8; row >= 1; row--) {
            whiteBoard.append(text).append(row).append(reset).append(" ");
            for (int col = 1; col <= 8; col++) {
                String bgColor = (row + col) % 2 == 0 ? dark: light;
                boolean shouldHighlight = checkHighlight(hSquares, row, col);
                if (shouldHighlight) { bgColor = EscapeSequences.SET_BG_COLOR_BLUE; }
                String piece = getPiece(game, row, col);
                whiteBoard.append(bgColor).append(" ").append(piece).append(" ");
            }
            whiteBoard.append(reset).append(EscapeSequences.RESET_BG_COLOR)
                    .append(" ").append(text).append(row).append(reset).append("\n");
        }
        whiteBoard.append(text).append("    h    g    f    e    d    c    b    a\n\n").append(reset);

        return whiteBoard.toString();
    }


    private static String getPiece(ChessGame game, int row, int column) {

        String whitePiece = EscapeSequences.SET_TEXT_COLOR_WHITE;
        String blackPiece = EscapeSequences.SET_TEXT_COLOR_RED;

        ChessBoard board = game.getBoard();
        ChessPosition pos = new ChessPosition(row, column);
        ChessPiece piece = board.getPiece(pos);

        if (piece == null) { return "   "; }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch(piece.getPieceType()) {
                case KNIGHT -> whitePiece + EscapeSequences.WHITE_KNIGHT;
                case BISHOP -> whitePiece + EscapeSequences.WHITE_BISHOP;
                case ROOK -> whitePiece + EscapeSequences.WHITE_ROOK;
                case KING -> whitePiece + EscapeSequences.WHITE_KING;
                case QUEEN -> whitePiece + EscapeSequences.WHITE_QUEEN;
                default -> whitePiece + EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch(piece.getPieceType()) {
                case KNIGHT -> blackPiece + EscapeSequences.BLACK_KNIGHT;
                case BISHOP -> blackPiece + EscapeSequences.BLACK_BISHOP;
                case ROOK -> blackPiece + EscapeSequences.BLACK_ROOK;
                case KING -> blackPiece + EscapeSequences.BLACK_KING;
                case QUEEN -> blackPiece + EscapeSequences.BLACK_QUEEN;
                default -> blackPiece + EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
