package chess.moves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

    // Refactor this later to better abstract the two loops - could be one

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
            increment = 1;
            ChessPosition nextPos = new ChessPosition(
                    position.getRow() + increment * direction,
                    position.getColumn()
            );

            while (ChessGetMoves.validatePosition(nextPos)) {

                ChessPiece targetPiece = board.getPiece(nextPos);
                if (targetPiece == null) {
                    moves.add(new ChessMove(position, nextPos, null));
                } else {
                    if (targetPiece.getTeamColor() == otherTeam)
                        moves.add(new ChessMove(position, nextPos, null));
                    break;
                }

                increment++;
                nextPos = new ChessPosition(
                        position.getRow() + increment * direction,
                        position.getColumn()
                );
            }
        }

        // Horizontal Moves
        for (int direction : directions) {
            increment = 1;
            ChessPosition nextPos = new ChessPosition(
                    position.getRow(),
                    position.getColumn() + increment * direction
            );

            while (ChessGetMoves.validatePosition(nextPos)) {

                ChessPiece targetPiece = board.getPiece(nextPos);
                if (targetPiece == null) {
                    moves.add(new ChessMove(position, nextPos, null));
                } else {
                    if (targetPiece.getTeamColor() == otherTeam)
                        moves.add(new ChessMove(position, nextPos, null));
                    break;
                }

                increment++;
                nextPos = new ChessPosition(
                        position.getRow(),
                        position.getColumn() + increment * direction
                );
            }
        }

        return moves;
    }
}
