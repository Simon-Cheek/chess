package chess;

import chess.moves.BishopMoves;
import chess.moves.PawnMoves;

import java.util.ArrayList;

public class ChessGetMoves {

    public static boolean validateMove(ChessMove move) {
        if ( !(0 < move.getStartPosition().getRow() && move.getStartPosition().getRow() < 9) ) return false;
        if ( !(0 < move.getStartPosition().getColumn() && move.getStartPosition().getColumn() < 9) ) return false;
        if ( !(0 < move.getEndPosition().getRow() && move.getEndPosition().getRow() < 9) ) return false;
        if ( !(0 < move.getEndPosition().getColumn() && move.getEndPosition().getColumn() < 9) ) return false;
        return true;
    }
    public static ArrayList<ChessMove> getMove(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);

        // Pawn Moves
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return PawnMoves.getPawnMoves(board, position);
        }

        // Bishop Moves
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return BishopMoves.getBishopMoves(board, position);
        }

        // Rook Moves
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {}

        // Knight Moves
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {}

        // King Moves
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {}

        // Queen Moves
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {}

        return moves;
    }
}
