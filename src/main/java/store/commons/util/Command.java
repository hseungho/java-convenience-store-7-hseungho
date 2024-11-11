package store.commons.util;

import camp.nextstep.edu.missionutils.Console;
import store.commons.lang.ProgramExitException;

public class Command {

    private Command() {}

    private static final String EXIT = "exit";

    public static String read() {
        String read = Console.readLine();
        validateRead(read);
        handleCommands(read);
        return read;
    }

    private static void validateRead(String read) {
        if (read == null || read.isBlank()) {
            throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    private static void handleCommands(String read) {
        if (EXIT.equals(read)) {
            Command.close();
            throw new ProgramExitException();
        }
    }

    public static void close() {
        Console.close();
    }

}
