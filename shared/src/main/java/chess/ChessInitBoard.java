package chess;

public class ChessInitBoard {

    public static void addWhitePieces(ChessBoard board) {

        // White Pawns
        ChessPiece w_p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        board.addPiece(new ChessPosition(2, 1), w_p);
        board.addPiece(new ChessPosition(2, 2), w_p);
        board.addPiece(new ChessPosition(2, 3), w_p);
        board.addPiece(new ChessPosition(2, 4), w_p);
        board.addPiece(new ChessPosition(2, 5), w_p);
        board.addPiece(new ChessPosition(2, 6), w_p);
        board.addPiece(new ChessPosition(2, 7), w_p);
        board.addPiece(new ChessPosition(2, 8), w_p);

        // White Rooks
        ChessPiece w_r = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board.addPiece(new ChessPosition(1, 1), w_r);
        board.addPiece(new ChessPosition(1, 8), w_r);

        // White Knights
        ChessPiece w_k = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board.addPiece(new ChessPosition(1, 2), w_k);
        board.addPiece(new ChessPosition(1, 7), w_k);

        // White Bishops
        ChessPiece w_b = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(new ChessPosition(1, 3), w_b);
        board.addPiece(new ChessPosition(1, 6), w_b);

        // White Royalty
        ChessPiece w_q = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece w_ki = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board.addPiece(new ChessPosition(1, 4), w_q);
        board.addPiece(new ChessPosition(1, 5), w_ki);
    }

    public static void addBlackPieces(ChessBoard board) {

        // Black Pawns
        ChessPiece w_p = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        board.addPiece(new ChessPosition(7, 1), w_p);
        board.addPiece(new ChessPosition(7, 2), w_p);
        board.addPiece(new ChessPosition(7, 3), w_p);
        board.addPiece(new ChessPosition(7, 4), w_p);
        board.addPiece(new ChessPosition(7, 5), w_p);
        board.addPiece(new ChessPosition(7, 6), w_p);
        board.addPiece(new ChessPosition(7, 7), w_p);
        board.addPiece(new ChessPosition(7, 8), w_p);

        // Black Rooks
        ChessPiece w_r = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board.addPiece(new ChessPosition(8, 1), w_r);
        board.addPiece(new ChessPosition(8, 8), w_r);

        // Black Knights
        ChessPiece w_k = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.addPiece(new ChessPosition(8, 2), w_k);
        board.addPiece(new ChessPosition(8, 7), w_k);

        // Black Bishops
        ChessPiece w_b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board.addPiece(new ChessPosition(8, 3), w_k);
        board.addPiece(new ChessPosition(8, 6), w_k);

        // White Royalty
        ChessPiece w_q = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece w_ki = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board.addPiece(new ChessPosition(8, 4), w_q);
        board.addPiece(new ChessPosition(8, 5), w_ki);
    }
}
