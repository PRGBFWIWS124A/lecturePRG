import java.awt.*;
import java.io.*;
import java.util.*;

public abstract class Utility {

    public static double PI = Math.PI;

    private static final Random RANDOM = new Random();

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static double distance(final int xa, final int ya, final int xb, final int yb) {
        return new Point(xa, ya).distance(new Point(xb, yb));
    }

    public static void error(final String msg) {
        System.err.println(msg);
    }

    public static int getRandomInt(final int bound) {
        return Utility.RANDOM.nextInt(bound);
    }

    public static int max(final int... args) {
        if (args.length == 0) {
            Utility.error("Called max without arguments!");
        }
        int res = args[0];
        for (int i = 1; i < args.length; i++) {
            res = Math.max(res, args[i]);
        }
        return res;
    }

    public static int min(final int... args) {
        if (args.length == 0) {
            Utility.error("Called min without arguments!");
        }
        int res = args[0];
        for (int i = 1; i < args.length; i++) {
            res = Math.min(res, args[i]);
        }
        return res;
    }

    public static String readStringFromConsole() throws IOException {
        return Utility.READER.readLine();
    }

}
