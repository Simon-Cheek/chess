package helpers;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MoveGenerator {

    public static boolean validatePosition(String pos) {
        char[] valid = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        if (!(pos.length() == 2)) { return false; }
        boolean validColumn = false;
        for (char c : valid) {
            if (c == pos.charAt(0)) {
                validColumn = true;
                break;
            }
        }
        int row = pos.charAt(1) - '0';
        if (row < 0 || row > 8) { return false; }

        return validColumn;
    }

    public static ChessPosition getPosition(String pos) {
        if (!validatePosition(pos)) { throw new RuntimeException("Invalid input"); }
        int column = pos.charAt(0) - 'a' + 1;
        int row = pos.charAt(1) - '0';
        return new ChessPosition(row, column);
    }

    public static ChessMove getMove(String start, String end, ChessPiece.PieceType promotion) {
        char[] valid = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        // Validate Column inputs
        if (!validatePosition(start) || !validatePosition(end)) { throw new RuntimeException("Invalid input"); }

        // Turn into positions
        ChessPosition startPos = getPosition(start);
        ChessPosition endPos = getPosition(end);


        return new ChessMove(startPos, endPos, promotion);
    }
}
