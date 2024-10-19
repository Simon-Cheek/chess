package model;

import chess.ChessGame;

public record GameRecord(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}