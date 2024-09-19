package chess.moves;

import chess.*;

import java.util.ArrayList;

public class BishopMoves {

    public static ArrayList<ChessMove> getBishopMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor otherTeam = (piece.getTeamColor() == ChessGame.TeamColor.BLACK)
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        // Loop through each direction
        for (int[] direction : directions) {
            int rowIncrement = direction[0];
            int colIncrement = direction[1];
            int increment = 1;
            ChessPosition nextPos = new ChessPosition(
                    position.getRow() + increment * rowIncrement,
                    position.getColumn() + increment * colIncrement
            );

            while (ChessGetMoves.validatePosition(nextPos)) {
                ChessPiece targetPiece = board.getPiece(nextPos);

                // If the square is empty, add the move
                if (targetPiece == null) {
                    moves.add(new ChessMove(position, nextPos, null));
                } else {
                    // If there's an opponent piece, add the move but stop searching in this direction
                    if (targetPiece.getTeamColor() == otherTeam)
                        moves.add(new ChessMove(position, nextPos, null));
                    break;
                }

                increment++;
                nextPos = new ChessPosition(
                        position.getRow() + increment * rowIncrement,
                        position.getColumn() + increment * colIncrement
                );
            }
        }
        return moves;
    }
}
