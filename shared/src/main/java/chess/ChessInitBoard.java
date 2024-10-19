package chess;

public class ChessInitBoard {

    public static void addWhitePieces(ChessBoard board) {

        // White Pawns
        ChessPiece wp = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        board.addPiece(new ChessPosition(2, 1), wp);
        board.addPiece(new ChessPosition(2, 2), wp);
        board.addPiece(new ChessPosition(2, 3), wp);
        board.addPiece(new ChessPosition(2, 4), wp);
        board.addPiece(new ChessPosition(2, 5), wp);
        board.addPiece(new ChessPosition(2, 6), wp);
        board.addPiece(new ChessPosition(2, 7), wp);
        board.addPiece(new ChessPosition(2, 8), wp);

        // White Rooks
        ChessPiece wr = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board.addPiece(new ChessPosition(1, 1), wr);
        board.addPiece(new ChessPosition(1, 8), wr);

        // White Knights
        ChessPiece wk = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board.addPiece(new ChessPosition(1, 2), wk);
        board.addPiece(new ChessPosition(1, 7), wk);

        // White Bishops
        ChessPiece wb = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(new ChessPosition(1, 3), wb);
        board.addPiece(new ChessPosition(1, 6), wb);

        // White Royalty
        ChessPiece wq = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece wki = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board.addPiece(new ChessPosition(1, 4), wq);
        board.addPiece(new ChessPosition(1, 5), wki);
    }

    public static void addBlackPieces(ChessBoard board) {

        // Black Pawns
        ChessPiece bp = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        board.addPiece(new ChessPosition(7, 1), bp);
        board.addPiece(new ChessPosition(7, 2), bp);
        board.addPiece(new ChessPosition(7, 3), bp);
        board.addPiece(new ChessPosition(7, 4), bp);
        board.addPiece(new ChessPosition(7, 5), bp);
        board.addPiece(new ChessPosition(7, 6), bp);
        board.addPiece(new ChessPosition(7, 7), bp);
        board.addPiece(new ChessPosition(7, 8), bp);

        // Black Rooks
        ChessPiece br = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board.addPiece(new ChessPosition(8, 1), br);
        board.addPiece(new ChessPosition(8, 8), br);

        // Black Knights
        ChessPiece bk = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.addPiece(new ChessPosition(8, 2), bk);
        board.addPiece(new ChessPosition(8, 7), bk);

        // Black Bishops
        ChessPiece bb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board.addPiece(new ChessPosition(8, 3), bb);
        board.addPiece(new ChessPosition(8, 6), bb);

        // White Royalty
        ChessPiece bq = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece bki = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board.addPiece(new ChessPosition(8, 4), bq);
        board.addPiece(new ChessPosition(8, 5), bki);
    }
}
