package tictactoe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

@FunctionalInterface
interface Command {
    void execute(String[] args);
}

final class Menu {
    private final HashMap<String, Command> commands;
    private boolean isRunning = false;

    public Menu() {
        commands = new HashMap<>();
        registerCommand("exit", (String[] args) -> isRunning = false);
    }

    public void registerCommand(String name, Command callback) {
        commands.put(name, callback);
    }

    public void run() {
        isRunning = true;

        final Scanner in = new Scanner(System.in);
        while (isRunning) {
            final String line = in.nextLine().strip();
            final String[] parts = line.split(" ");

            if (line.equals("")) {
                System.out.println("Empty line");
                continue;
            }

            final String command = parts[0];

            if (commands.containsKey(command)) {
                final Command cmd = commands.get(command);
                cmd.execute(Arrays.copyOfRange(parts, 1, parts.length));
            } else {
                System.out.printf("Command '%s' not found!\n", command);
            }
        }
    }
}
