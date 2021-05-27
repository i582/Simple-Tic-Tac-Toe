package tictactoe;

interface Player {
    void setField(BattleField field);

    void makeMove();
}

class PlayerFactory {
    public static Player getPlayerByName(String name) {
        switch (name) {
            case "easy":
                return new AIPlayer(new EasyAI());
            case "medium":
                return new AIPlayer(new MediumAI());
            case "hard":
                return new AIPlayer(new HardAI());
            case "user":
                return new HumanPlayer();
        }
        return null;
    }
}

class HumanPlayer implements Player {
    private BattleField field;

    @Override
    public void setField(BattleField field) {
        this.field = field;
    }

    @Override
    public void makeMove() {
        while (true) {
            final Move move = BattleField.readMove();
            if (field.isPossibleMove(move)) {
                field.makeMove(move);
                break;
            } else {
                System.out.println("This cell is occupied! Choose another one!");
            }
        }
    }
}

class AIPlayer implements Player {
    private final AI ai;
    private BattleField field;

    public AIPlayer(AI ai) {
        this.ai = ai;
    }

    @Override
    public void setField(BattleField field) {
        this.field = field;
        this.ai.setField(field);
    }

    @Override
    public void makeMove() {
        final Move move = ai.generateMove();

        field.makeMove(move);
        System.out.printf("Making move level \"%s\"\n", ai.name());
    }
}
