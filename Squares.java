public class Squares {

    public static void main(final String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println(
                "Es muessen zwischen 1 und 2 Parameter angegeben werden "
                + "(Level und eventuell Basislaenge)!"
            );
            return;
        }
        final int level = Integer.parseInt(args[0]);
        final int length = args.length == 2 ? Integer.parseInt(args[1]) : 100;
        if (level < 1) {
            System.out.println("Das Rekursionslevel muss positiv sein!");
            return;
        }
        if (length < 1) {
            System.out.println("Die Basislaenge muss positiv sein!");
            return;
        }
        Squares.paintFractal(level, length);
    }

    static void paintFractal(final int level, final double length) {
        //TODO
    }

}
