/*
    Prof. Wonvin Kim
    Department of Computer Software
    Korean Bible University

    Copyright for Wonvin Kim All rights reserved.
 */

import log.ConsoleColors;
import log.Logger;

import java.util.Arrays;

public class Sample {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("color")) Logger.COLOR = true;

        hello();

        while (true) {
            Menu.shared().exec();
        }
    }

    public static void hello() {
        String[][] strs = {
                {
                        ConsoleColors.RED_UNDERLINED,
                        String.format("Welcome to %s project!", Sample.class.getSimpleName())
                },
                {
                        ConsoleColors.BLUE,
                        "자바기반응용프로그래밍"
                },
                {
                        ConsoleColors.BLUE_BOLD,
                        "Prof. Wonvin Kim, Department of Computer Software, Korean Bible University"
                },
                {
                        ConsoleColors.RESET,
                        ""
                }
        };

        Arrays.stream(strs).forEach(v -> Logger.print(v[0], "\n" + v[1]));
    }
}
