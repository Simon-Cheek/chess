package model;

import chess.ChessGame;

import java.util.ArrayList;

public record GameRecord(
        int gameID, String whiteUsername, String blackUsername,
        String gameName, ChessGame game) {}