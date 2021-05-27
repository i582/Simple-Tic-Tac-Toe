package tictactoe;

import java.util.ArrayList;
import java.util.Random;

abstract class AI {
    protected BattleField field;

    public void setField(BattleField field) {
        this.field = field;
    }

    public int positionRank(BattleField field) {
        String status = field.gameStatus();

        switch (status) {
            case "X wins":
                return 1000;
            case "O wins":
                return -1000;
            case "Draw":
                return 0;
        }

        return 0;
    }

    abstract public String name();

    abstract public Move generateMove();
}

final class EasyAI extends AI {
    @Override
    public String name() {
        return "easy";
    }

    @Override
    public Move generateMove() {
        Random random = new Random();
        ArrayList<Move> moves = field.emptyFields();
        return moves.get(random.nextInt(moves.size()));
    }
}

final class MediumAI extends AI {
    @Override
    public String name() {
        return "medium";
    }

    @Override
    public Move generateMove() {
        Random random = new Random();
        ArrayList<Move> moves = field.emptyFields();
        int maxRank = -1000;
        Move maxRankMove = moves.get(0);

        for (Move move : moves) {
            BattleField analyzeField = field.clone();
            analyzeField.makeMove(move);

            ArrayList<Move> opponentMoves = analyzeField.emptyFields();
            int maxOpponentRank = -1000;
            for (Move opponentMove : opponentMoves) {
                BattleField analyzeOpField = analyzeField.clone();

                analyzeOpField.makeMove(opponentMove);
                int rank = positionRank(analyzeOpField);
                if (rank == 0) {
                    rank = random.nextInt(10);
                }

                if (rank > maxOpponentRank) {
                    maxOpponentRank = rank;
                }
            }

            if (-maxOpponentRank > maxRank) {
                maxRank = -maxOpponentRank;
                maxRankMove = move;
            }
        }

        return maxRankMove;
    }
}

final class HardAI extends AI {
    @Override
    public String name() {
        return "hard";
    }

    @Override
    public Move generateMove() {
        final ArrayList<Move> moves = field.emptyFields();

        final boolean isX = field.isMoveX();

        int maxRank = Integer.MIN_VALUE;
        Move maxRankMove = moves.get(0);
        for (Move move : moves) {
            BattleField analyzeField = field.clone();
            analyzeField.makeMove(move);
            final int rank = (isX ? 1 : -1) * minimax(analyzeField, 9, !isX);
            if (rank > maxRank) {
                maxRank = rank;
                maxRankMove = move;
            }
        }

        return maxRankMove;
    }

    private int minimax(BattleField field, int depth, boolean maximizingPlayer) {
        if (depth == 0 || !field.gameStatus().equals("Game not finished")) {
            return positionRank(field);
        }

        int rank;
        ArrayList<Move> moves = field.emptyFields();

        if (maximizingPlayer) {
            rank = Integer.MIN_VALUE;
            for (Move move : moves) {
                BattleField analyzeField = field.clone();
                analyzeField.makeMove(move);
                rank = Math.max(rank, minimax(analyzeField, depth - 1, false));
            }
        } else {
            rank = Integer.MAX_VALUE;
            for (Move move : moves) {
                BattleField analyzeField = field.clone();
                analyzeField.makeMove(move);
                rank = Math.min(rank, minimax(analyzeField, depth - 1, true));
            }
        }
        return rank;
    }
}
