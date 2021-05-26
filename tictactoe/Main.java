package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

final class Move {
    int X, Y;

    public Move(int x, int y) {
        X = x;
        Y = y;
    }
}

final class BattleField {
    private int fieldsOfX = 0;
    private int fieldsOfO = 0;
    private int countOfX = 0;
    private int countOfO = 0;
    private boolean isMoveX = true;

    /**
     * A simple constructor to create an empty field.
     */
    public BattleField() {
    }

    /**
     * Constructor that creates a field from a string of a given format.
     * @param data A 9-character string consisting of _, X, O, where each character defines a field on the board.
     * @throws DataFormatException If the string length is not 9, then an exception is thrown.
     */
    public BattleField(String data) throws DataFormatException {
        if (data.length() != 9) {
            throw new DataFormatException("The data for building the position must have a length equal to 9 characters.");
        }

        for (int i = 0; i < 9; i++) {
            switch (data.charAt(i)) {
                case '_':
                    break;
                case 'X':
                    countOfX++;
                    fieldsOfX |= 1 << i;
                    break;
                case 'O':
                    countOfO++;
                    fieldsOfO |= 1 << i;
                    break;
                default:
                    throw new DataFormatException(String.format("Invalid symbol '%s' (only _, X, O is allowed)", data.charAt(i)));
            }
        }
    }

    /**
     * A function that analyzes the current position on the board.
     * @return Current status message.
     */
    public String gameStatus() {
        boolean XWins = isWin(fieldsOfX);
        boolean OWins = isWin(fieldsOfO);

        if (Math.abs(countOfX - countOfO) > 1 || (XWins && OWins)) {
            return "Impossible";
        } else if (XWins) {
            return "X wins";
        } else if (OWins) {
            return "O wins";
        } else if (countOfX + countOfO == 9) {
            return "Draw";
        }

        return "Game not finished";
    }

    /**
     * The function reads a move from the user input while handling cases
     * of incorrect input. The function will not terminate until valid data is entered.
     * @return The entered move by the user.
     */
    public static Move readMove() {
        int x, y;

        while (true) {
            Scanner io = new Scanner(System.in);
            System.out.print("Enter the coordinates: ");

            try {
                y = io.nextInt();
                if (y > 3 || y < 1) {
                    throw new DataFormatException("Coordinates should be from 1 to 3!");
                }

                x = io.nextInt();
                if (x > 3 || x < 1) {
                    throw new DataFormatException("Coordinates should be from 1 to 3!");
                }

                break;
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
            } catch (DataFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        return new Move(x, y);
    }

    /**
     * The function that makes the given move.
     * @param move The move to be made.
     * @throws DataFormatException If the field to which they are setting is already occupied,
     * then an exception is thrown.
     */
    public void makeMove(Move move) throws DataFormatException {
        if (!isEmptyField(move.X, move.Y)) {
            throw new DataFormatException("This cell is occupied! Choose another one!");
        }

        if (isMoveX) {
            setX(move.X, move.Y);
        } else {
            setO(move.X, move.Y);
        }

        isMoveX = !isMoveX;
    }

    private int setField(int x, int y, int where) {
        return where | 1 << (x - 1) + (y - 1) * 3;
    }

    private void setX(int x, int y) {
        fieldsOfX = setField(x, y, fieldsOfX);
        countOfX++;
    }

    private void setO(int x, int y) {
        fieldsOfO = setField(x, y, fieldsOfO);
        countOfO++;
    }

    private static boolean isSet(int fieldsMask, int needMask) {
        return (fieldsMask & needMask) == needMask;
    }

    private static boolean isWin(int fieldsMask) {
        return isSet(fieldsMask, 0b111_000_000) ||
                isSet(fieldsMask, 0b000_111_000) ||
                isSet(fieldsMask, 0b000_000_111) ||
                isSet(fieldsMask, 0b100_100_100) ||
                isSet(fieldsMask, 0b010_010_010) ||
                isSet(fieldsMask, 0b001_001_001) ||
                isSet(fieldsMask, 0b100_010_001) ||
                isSet(fieldsMask, 0b001_010_100);
    }

    private boolean isEmptyField(int x, int y) {
        return fieldsOfX != setField(x, y, fieldsOfX) &&
                fieldsOfO != setField(x, y, fieldsOfO);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("---------\n");

        for (int i = 0; i < 3; i++) {
            res.append("| ");

            for (int j = 0; j < 3; j++) {
                int index = i * 3 + j;

                if ((fieldsOfX & (1 << index)) != 0) {
                    res.append("X ");
                } else if ((fieldsOfO & (1 << index)) != 0) {
                    res.append("O ");
                } else {
                    res.append("_ ");
                }
            }

            res.append("|\n");
        }

        res.append("---------\n");

        return res.toString();
    }
}

final class Game {
    private final BattleField field;
    private boolean isRunning = false;

    public Game() {
        field = new BattleField();
    }

    private void move() {
        while (true) {
            try {
                Move move = BattleField.readMove();
                field.makeMove(move);
                break;
            } catch (DataFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showBoard() {
        System.out.print(field);
    }

    private void checkEnd() {
        String status = field.gameStatus();
        if (status.equals("Game not finished")) {
            return;
        }

        showBoard();
        System.out.print(status);
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

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
