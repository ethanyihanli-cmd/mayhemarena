package com.macondo.mayhemarena.match;

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

    public void endRound(int winnerId) {
        if (!roundActive) {
            return;
        }

        roundActive = false;

        if (winnerId == 1) {
            player1Wins++;
        } else {
            player2Wins++;
        }

        if (listener != null) {
            listener.onRoundEnd(winnerId, player1Wins, player2Wins);
        }

        if (player1Wins >= 2 || player2Wins >= 2) {
            endMatch(winnerId);
        }
    }

    public void endMatch(int winnerId) {
        matchActive = false;
        roundActive = false;

        if (listener != null) {
            listener.onMatchEnd(winnerId, player1Wins, player2Wins);
        }
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
        void onRoundEnd(int winnerId, int p1Wins, int p2Wins);
        void onMatchEnd(int winnerId, int p1Wins, int p2Wins);
    }


}
