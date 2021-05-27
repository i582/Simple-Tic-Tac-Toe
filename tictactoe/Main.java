package tictactoe;

public class Main {
    public static void main(String[] runArgs) {
        Menu menu = new Menu();
        menu.registerCommand("start", (String[] args) -> {
            if (args.length != 2) {
                System.out.println("Bad parameters!");
                return;
            }

            Player firstPlayer = PlayerFactory.getPlayerByName(args[0]);
            if (firstPlayer == null) {
                System.out.println("Bad parameters!");
                return;
            }

            Player secondPlayer = PlayerFactory.getPlayerByName(args[1]);
            if (secondPlayer == null) {
                System.out.println("Bad parameters!");
                return;
            }

            Game game = new Game(firstPlayer, secondPlayer);
            game.play();
        });
        menu.run();
    }
}
