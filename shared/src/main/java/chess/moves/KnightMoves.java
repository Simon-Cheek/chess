package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public static ArrayList<ChessMove> getKnightMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor otherTeam = (piece.getTeamColor() == ChessGame.TeamColor.BLACK)
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;

        int[][] directions = {
                {-2, -1},
                {-2, 1},
                {-1, -2},
                {-1, 2},
                {1, -2},
                {1, 2},
                {2, -1},
                {2, 1}
        };

        for (int[] direction : directions) {
            ChessPosition nextPos = new ChessPosition(
                    position.getRow() + direction[0],
                    position.getColumn() + direction[1]
            );

            // Add move if valid and either null or enemy piece
            if (ChessGetMoves.validatePosition(nextPos)) {
                ChessPiece enemyPiece = board.getPiece(nextPos);
                if (enemyPiece == null || enemyPiece.getTeamColor() == otherTeam) {
                    ChessMove nextMove = new ChessMove(position, nextPos, null);
                    moves.add(nextMove);
                }
            }
        }
        return moves;
    }
}