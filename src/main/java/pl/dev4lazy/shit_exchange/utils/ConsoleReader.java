package pl.dev4lazy.shit_exchange.utils;

import java.util.Scanner;

public class ConsoleReader implements KeyboardReader {

    private Scanner scanner = new Scanner( System.in );

    @Override
    public int nextInt() {
        int result;
        try {
            result = scanner.nextInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            nextLine();
        }
        return result;
    }

    @Override
    public void nextLine() {
        scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }

}
