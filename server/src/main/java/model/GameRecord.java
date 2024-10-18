package model;

import chess.ChessGame;

public record GameRecord(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}