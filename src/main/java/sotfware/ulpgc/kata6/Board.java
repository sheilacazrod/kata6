package sotfware.ulpgc.kata6;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.stream;

public class Board {
    private final static char AliveCell = 'X';
    private final static char DeadCell = '.';
    private final String[] state;
    private final List<Rule> rules = List.of(
            (i,j) -> isAlive(i,j) && is(neighboors(i,j),2,3),
            (i,j) -> isDead(i,j) && is(neighboors(i,j),3)

    );

    private boolean isDead(int row, int col) {
        return !isAlive(row,col);
    }

    private boolean is(int expected, int... values) {
        return stream(values).anyMatch(value -> value == expected);
    }

    private int neighboors(int row, int col) {
        return (int) checksOf(row,col).stream().filter(Check::check).count();
    }

    private List<Check> checksOf(int row, int col) {
        return List.of(
                () -> isAlive(row - 1, col -1),
                () -> isAlive(row - 1, col),
                () -> isAlive(row - 1, col +1),
                () -> isAlive(row , col -1),
                () -> isAlive(row , col +1),
                () -> isAlive(row + 1, col -1),
                () -> isAlive(row + 1, col),
                () -> isAlive(row + 1, col +1)
        );
    }

    private boolean isAlive(int row, int col) {
        return isBounds(row,col) && state[row].charAt(col) == AliveCell;
    }

    private boolean isBounds(int row, int col) {
        return row >= 0 && row < rows() && col >= 0 && col < cols();
    }

    public Board(String[] state) {
        this.state = state;
    }

    public Board(String state) {
        this(state.split("\n"));
    }

    public Board next(){
        return new Board(calculateNextState());
    }

    private String[] calculateNextState() {
        String[] result = new String[rows()];
        for(int i = 0; i < rows(); i++){
            result[i] = calculateNextState(i);

        }
        return result;
    }

    private String calculateNextState(int row) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < cols(); i++){
            result.append(calculateNextState(row, i));

        }
        return result.toString();
    }

    private char calculateNextState(int row, int col) {
        return anyRule(row, col) ? AliveCell : DeadCell;
    }

    private boolean anyRule(int row, int col) {
        return rules.stream().anyMatch(rule ->rule.check(row,col));
    }

    public String state() {
        return String.join("\n", state);
    }

    private interface Rule {
        boolean check(int row, int col);
    }

    private interface Check {
        boolean check();
    }

    private int cols() {
        return state[0].length();
    }

    private int rows() {
        return state.length;
    }
}
