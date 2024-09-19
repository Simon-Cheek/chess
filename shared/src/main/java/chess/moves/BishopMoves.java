package chess.moves;

import chess.*;

import java.util.ArrayList;

public class BishopMoves {

    public static ArrayList<ChessMove> getBishopMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor otherTeam;

        // Set up other team
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            otherTeam = ChessGame.TeamColor.WHITE;
        } else otherTeam = ChessGame.TeamColor.BLACK;

        // Up Right

        // Up Left

        // Down Right

        // Down Left

        return moves;
    }
}
