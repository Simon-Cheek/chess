package chess.moves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

    public static ArrayList<ChessMove> getRookMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor otherTeam = (piece.getTeamColor() == ChessGame.TeamColor.BLACK)
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;

        int[] directions = {-1, 1};
        int increment = 1;

        // Vertical Moves
        for (int direction : directions) {
            ChessPosition nextPos = new ChessPosition(
                    position.getRow() + increment * direction,
                    position.getColumn()
            );
        }

        // Horizontal Moves
        increment = 1;
        for (int direction : directions) {
            ChessPosition nextPos = new ChessPosition(
                    position.getRow(),
                    position.getColumn() + increment * direction
            );
        }

        return moves;
    }
}
