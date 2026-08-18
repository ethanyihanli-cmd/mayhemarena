package com.macondo.mayhemarena.match;

import com.macondo.mayhemarena.entity.Player;
import javafx.application.Platform;

public class MatchController {
    private int player1Wins;
    private int player2Wins;
    private int roundNumber;
    private boolean matchActive;
    private boolean roundActive;

    private MatchStateListener listener;

    public MatchController() {
        player1Wins = 0;
        player2Wins = 0;
        roundNumber = 0;
        matchActive = false;
        roundActive = false;
    }

    public void startMatch() {
        matchActive = true;
        startRound();
    }

    public void startRound() {
        if (!matchActive) {
            return;
        }

        roundNumber++;
        roundActive = true;

        if (listener != null) {
            listener.onRoundStart(roundNumber);
        }
    }

    public void endRound(Player winner) {
        if (!roundActive) {
            return;
        }

        roundActive = false;

        if (winner.getPlayerId() == 1) {
            player1Wins++;
        } else {
            player2Wins++;
        }

        if (listener != null) {
            listener.onRoundEnd(winner, player1Wins, player2Wins);
        }

        if (player1Wins >= 2 || player2Wins >= 2) {
            endMatch(winner);
        }
    }

    public void endMatch(Player winner) {
        matchActive = false;
        roundActive = false;

        if (listener != null) {
            listener.onMatchEnd(winner, player1Wins, player2Wins);
        }
    }

    public void resetPlayers(Player p1, Player p2) {
        p1.reset();
        p2.reset();
    }

    public void setListener(MatchStateListener listener) {
        this.listener = listener;
    }

    public int getPlayer1Wins() { return player1Wins; }
    public int getPlayer2Wins() { return player2Wins; }
    public int getRoundNumber() { return roundNumber; }
    public boolean isMatchActive() { return matchActive; }
    public boolean isRoundActive() { return roundActive; }

    public interface MatchStateListener {
        void onRoundStart(int roundNumber);
        void onRoundEnd(Player winner, int p1Wins, int p2Wins);
        void onMatchEnd(Player winner, int p1Wins, int p2Wins);
    }


}
