package chess;

import chess.moves.*;

import java.util.ArrayList;

public class ChessGetMoves {

    public static boolean validatePosition(ChessPosition position) {
        if ( !(0 < position.getRow() && position.getRow() < 9) ) { return false; }
        return 0 < position.getColumn() && position.getColumn() < 9;
    }

    public static boolean validateMove(ChessMove move) {
        if (!validatePosition(move.getStartPosition())) { return false; }
        return validatePosition(move.getEndPosition());
    }

    public static ArrayList<ChessMove> getMove(ChessBoard board, ChessPosition position) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);

        // Pawn Moves
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) { return PawnMoves.getPawnMoves(board, position); }


        // Bishop Moves
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) { return BishopMoves.getBishopMoves(board, position); }


        // Rook Moves
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) { return RookMoves.getRookMoves(board, position); }

        // Knight Moves
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) { return KnightMoves.getKnightMoves(board, position); }

        // King Moves
        if (piece.getPieceType() == ChessPiece.PieceType.KING) { return KingMoves.getKingMoves(board, position); }

        // Queen Moves
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) { return QueenMoves.getQueenMoves(board, position); }

        return moves;
    }
}
