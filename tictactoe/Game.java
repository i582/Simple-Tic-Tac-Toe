package tictactoe;

final class Game {
    private final BattleField field;
    private boolean isRunning = false;

    private final Player firstPlayer;
    private final Player secondPlayer;

    public Game(Player firstPlayer, Player secondPlayer) {
        this.field = new BattleField();

        this.firstPlayer = firstPlayer;
        this.firstPlayer.setField(field);

        this.secondPlayer = secondPlayer;
        this.secondPlayer.setField(field);
    }

    public void move() {
        if (field.isMoveX()) {
            firstPlayer.makeMove();
        } else {
            secondPlayer.makeMove();
        }
    }

    private void showBoard() {
        System.out.print(field);
    }

    private void checkEnd() {
        final String status = field.gameStatus();
        if (status.equals("Game not finished")) {
            return;
        }

        showBoard();
        System.out.println(status);
        stop();
    }

    public void stop() {
        isRunning = false;
    }

    public void play() {
        isRunning = true;

        while (isRunning) {
            showBoard();
            move();
            checkEnd();
        }
    }
}
