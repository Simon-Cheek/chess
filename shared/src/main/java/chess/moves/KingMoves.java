package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KingMoves {

    public static ArrayList<ChessMove> getKingMoves(ChessBoard board, ChessPosition position) {


        int[][] directions = {
                {-1, -1},
                {-1, 0},
                {-1, 1},
                {0, -1},
                {0, 1},
                {1, -1},
                {1, 0},
                {1, 1}
        };

        return KingKnightHelper.getMoves(board, position, directions);
    }
}
