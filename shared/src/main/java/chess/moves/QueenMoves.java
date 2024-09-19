package chess.moves;

import chess.*;

import java.util.ArrayList;

public class QueenMoves {

    public static ArrayList<ChessMove> getQueenMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessMove> bishopMoves = BishopMoves.getBishopMoves(board, position);
        ArrayList<ChessMove> rookMoves = RookMoves.getRookMoves(board, position);

        moves.addAll(bishopMoves);
        moves.addAll(rookMoves);

        return moves;
    }
}
