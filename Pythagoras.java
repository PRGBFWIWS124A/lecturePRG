public class Pythagoras {

    public static void main(final String[] args) {
        if (args.length < 1 || args.length > 5) {
            System.out.println(
                "Es müssen zwischen 1 und 5 Parameter angegeben werden "
                + "(Level, Basislänge, linker Winkel, rechter Winkel, Wechsellänge)!"
            );
            return;
        }
        final int level = Integer.parseInt(args[0]);
        final int length = args.length > 1 ? Integer.parseInt(args[1]) : 100;
        final int leftAngle = args.length > 2 ? Integer.parseInt(args[2]) : 45;
        final int rightAngle = args.length > 3 ? Integer.parseInt(args[3]) : 45;
        final int switchLength = args.length > 4 ? Integer.parseInt(args[4]) : 10;
        if (level < 1) {
            System.out.println("Das Rekursionslevel muss positiv sein!");
            return;
        }
        if (length < 1) {
            System.out.println("Die Basislaenge muss positiv sein!");
            return;
        }
        if (leftAngle < 1) {
            System.out.println("Der linke Winkel muss positiv sein!");
            return;
        }
        if (rightAngle < 1) {
            System.out.println("Der rechte Winkel muss positiv sein!");
            return;
        }
        if (leftAngle >= 90) {
            System.out.println("Der linke Winkel muss kleiner als 90 sein!");
            return;
        }
        if (rightAngle >= 90) {
            System.out.println("Der rechte Winkel muss kleiner als 90 sein!");
            return;
        }
        if (leftAngle + rightAngle > 120) {
            System.out.println(
                "Die beiden Winkel duerfen zusammen nicht mehr als 120 ergeben!"
            );
            return;
        }
        Pythagoras.paintPythagorasTree(
            level,
            length,
            leftAngle,
            rightAngle,
            switchLength
        );
    }

    static void paintPythagorasTree(
        final int level,
        final double length,
        final int leftAngle,
        final int rightAngle,
        final int switchLength
    ) {
        //TODO
    }

}
