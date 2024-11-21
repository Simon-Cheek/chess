package helpers;

import ui.EscapeSequences;

public class HelpInfo {

    public static String help(boolean isLoggedIn, boolean isInGame) {

        String notLoggedin = String.format("""
                %s register <USERNAME> <PASSWORD> <EMAIL> %s- to create an account
                %s login <USERNAME> <PASSWORD> %s- to play chess
                %s quit %s- playing chess
                %s help %s- with possible commands
                """, EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR
        );

        String loggedIn = String.format("""
                %s create <NAME> %s- a game
                %s list %s- games
                %s join <ID> [WHITE|BLACK] %s- a game
                %s observe <ID> %s- a game
                %s logout %s- when you are done
                %s quit %s- playing chess
                %s help %s- with possible commands
                """, EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR
        );

        String inGame = String.format("""
                %s redraw %s- the chess board
                %s leave %s- the game
                %s resign %s- the game
                %s make move <START_POS> <END_POS> <OPTIONAL: Promotion Piece> %s- in the game
                ( Position being expressed as <Column><Row> ex: 'a3' )
                %s highlight <POS> %s- moves for given piece
                %s help %s- with possible commands
                """, EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR,
                EscapeSequences.SET_TEXT_COLOR_BLUE,
                EscapeSequences.RESET_TEXT_COLOR);

        return isInGame ? inGame : isLoggedIn ? loggedIn : notLoggedin;
    }
}
