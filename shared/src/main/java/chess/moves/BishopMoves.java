package chess.moves;

import chess.*;

import java.util.ArrayList;

public class BishopMoves {

    public static ArrayList<ChessMove> getBishopMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);

        return moves;
    }
}
