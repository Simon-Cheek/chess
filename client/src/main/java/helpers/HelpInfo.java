package helpers;

import ui.EscapeSequences;

public class HelpInfo {

    public static String help(boolean isLoggedIn) {

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

        return isLoggedIn ? loggedIn : notLoggedin;
    }
}
