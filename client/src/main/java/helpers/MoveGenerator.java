package helpers;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MoveGenerator {

    public static ChessMove getMove(String start, String end, ChessPiece.PieceType promotion) {
        char[] valid = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        // Validate Column inputs
        int contained = 0;
        for (char c : valid) {
            if (c == start.charAt(0)) { contained++; }
            if (c == end.charAt(0)) { contained++; }
        }
        if (contained != 2) { throw new RuntimeException("Invalid input"); }

        // Turn into positions
        int startColumn = start.charAt(0) - 'a' + 1;
        int startRow = start.charAt(1) - '0';
        ChessPosition startPos = new ChessPosition(startRow, startColumn);

        int endColumn = end.charAt(0) - 'a' + 1;
        int endRow = end.charAt(1) - '0';
        ChessPosition endPos = new ChessPosition(endRow, endColumn);

        return new ChessMove(startPos, endPos, promotion);
    }
}
