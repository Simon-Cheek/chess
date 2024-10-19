package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public static ArrayList<ChessMove> getKnightMoves(ChessBoard board, ChessPosition position) {

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

        return KingKnightHelper.getMoves(board, position, directions);
    }
}
