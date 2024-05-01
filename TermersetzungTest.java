import java.util.function.*;

public class TermersetzungTest {

    private static final Term A;
    private static final TermList EMPTY_ARGS;
    private static final Term FXY;
    private static final Term O;
    private static final Term PLUSOSY;
    private static final Term SO;
    private static final Term SSO;
    private static final Term SX;
    private static final Term SY;
    private static final boolean TEST_PREDEFINED = false;
    private static final Term X;
    private static final Substitution XOYSO;
    private static final Term Y;
    private static final Term Z;

    static {
        EMPTY_ARGS = new TermList(null);
        X = new Term("X", true, TermersetzungTest.EMPTY_ARGS);
        Y = new Term("Y", true, TermersetzungTest.EMPTY_ARGS);
        Z = new Term("Z", true, TermersetzungTest.EMPTY_ARGS);
        A = new Term("A", true, TermersetzungTest.EMPTY_ARGS);
        O = TermersetzungTest.createTerm("o");
        SX = TermersetzungTest.createTerm("s", TermersetzungTest.X);
        SY = TermersetzungTest.createTerm("s", TermersetzungTest.Y);
        SO = TermersetzungTest.createTerm("s", TermersetzungTest.O);
        SSO = TermersetzungTest.createTerm("s", TermersetzungTest.SO);
        FXY = TermersetzungTest.createTerm("f", TermersetzungTest.X, TermersetzungTest.Y);
        PLUSOSY = TermersetzungTest.createTerm("plus", TermersetzungTest.O, TermersetzungTest.SY);
        XOYSO =
            new Substitution(
                new SubstitutionNode("X", TermersetzungTest.O, new SubstitutionNode("Y", TermersetzungTest.SO, null))
            );

    }

    public static void main(final String[] args) {
        TermersetzungTest.test("toString 1", () -> "f(X,Y)".equals(Termersetzung.toString(TermersetzungTest.FXY)));
        TermersetzungTest.test(
            "toString 2", () -> "plus(o,s(Y))".equals(Termersetzung.toString(TermersetzungTest.PLUSOSY))
        );
        TermersetzungTest.test("toString 3", () -> "X".equals(Termersetzung.toString(TermersetzungTest.X)));
        TermersetzungTest.test("toString 4", () -> "o".equals(Termersetzung.toString(TermersetzungTest.O)));
        System.out.println();
        TermersetzungTest.test(
            "apply 1",
            () -> "f(s(o),Y)".equals(
                Termersetzung.toString(
                    Termersetzung.apply(
                        TermersetzungTest.FXY, new Substitution(new SubstitutionNode("X", TermersetzungTest.SO, null))
                    )
                )
            )
        );
        TermersetzungTest.test(
            "apply 2",
            () -> "f(X,Y)".equals(
                Termersetzung.toString(
                    Termersetzung.apply(
                        TermersetzungTest.FXY, new Substitution(new SubstitutionNode("Z", TermersetzungTest.O, null))
                    )
                )
            )
        );
        TermersetzungTest.test(
            "apply 3",
            () -> "f(o,s(o))".equals(
                Termersetzung.toString(Termersetzung.apply(TermersetzungTest.FXY, TermersetzungTest.XOYSO))
            )
        );
        TermersetzungTest.test(
            "apply 4",
            () -> "plus(o,s(s(o)))".equals(
                Termersetzung.toString(Termersetzung.apply(TermersetzungTest.PLUSOSY, TermersetzungTest.XOYSO))
            )
        );
        System.out.println();
        TermersetzungTest.test(
            "rewrite 1",
            () -> "s(plus(o,s(s(o))))".equals(
                Termersetzung.toString(
                    Termersetzung.rewrite(
                        new Rule(
                            TermersetzungTest.createTerm("plus", TermersetzungTest.SX, TermersetzungTest.Y),
                            TermersetzungTest.createTerm(
                                "s",
                                TermersetzungTest.createTerm("plus", TermersetzungTest.X, TermersetzungTest.Y)
                            )
                        ),
                        TermersetzungTest.createTerm("plus", TermersetzungTest.SO, TermersetzungTest.SSO)
                    )
                )
            )
        );
        TermersetzungTest.test(
            "rewrite 2",
            () -> "s(s(s(o)))".equals(
                Termersetzung.toString(
                    Termersetzung.rewrite(
                        new Rule(
                            TermersetzungTest.createTerm("plus", TermersetzungTest.O, TermersetzungTest.Y),
                            TermersetzungTest.Y
                        ),
                        TermersetzungTest.createTerm(
                            "s",
                            TermersetzungTest.createTerm("plus", TermersetzungTest.O, TermersetzungTest.SSO)
                        )
                    )
                )
            )
        );
        TermersetzungTest.test(
            "rewrite 3",
            () -> Termersetzung.rewrite(
                new Rule(
                    TermersetzungTest.createTerm("f", TermersetzungTest.X, TermersetzungTest.X),
                    TermersetzungTest.createTerm(
                        "f",
                        TermersetzungTest.X,
                        TermersetzungTest.createTerm("f", TermersetzungTest.X, TermersetzungTest.X)
                    )
                ),
                TermersetzungTest.createTerm(
                    "s",
                    TermersetzungTest.createTerm("plus", TermersetzungTest.O, TermersetzungTest.SSO)
                )
            ) == null
        );
        TermersetzungTest.test(
            "rewrite 4",
            () -> "f(f(o,f(o,o)),f(o,o))".equals(
                Termersetzung.toString(
                    Termersetzung.rewrite(
                        new Rule(
                            TermersetzungTest.createTerm("f", TermersetzungTest.X, TermersetzungTest.X),
                            TermersetzungTest.createTerm(
                                "f",
                                TermersetzungTest.X,
                                TermersetzungTest.createTerm("f", TermersetzungTest.X, TermersetzungTest.X)
                            )
                        ),
                        TermersetzungTest.createTerm(
                            "f",
                            TermersetzungTest.createTerm("f", TermersetzungTest.O, TermersetzungTest.O),
                            TermersetzungTest.createTerm("f", TermersetzungTest.O, TermersetzungTest.O)
                        )
                    )
                )
            )
        );
        if (TermersetzungTest.TEST_PREDEFINED) {
            System.out.println();
            TermersetzungTest.test("toString 5", () -> "[]".equals(Termersetzung.toString(new Substitution(null))));
            TermersetzungTest.test(
                "toString 6",
                () -> "[X/o]".equals(
                    Termersetzung.toString(new Substitution(new SubstitutionNode("X", TermersetzungTest.O, null)))
                )
            );
            TermersetzungTest.test(
                "toString 7",
                () -> "[X/s(o),Y/Z]".equals(
                    Termersetzung.toString(
                        new Substitution(
                            new SubstitutionNode(
                                "X",
                                TermersetzungTest.SO,
                                new SubstitutionNode("Y", TermersetzungTest.Z, null)
                            )
                        )
                    )
                )
            );
            TermersetzungTest.test(
                "toString 8",
                () -> "[X/A,Y/A,Z/A]".equals(
                    Termersetzung.toString(
                        new Substitution(
                            new SubstitutionNode(
                                "X",
                                TermersetzungTest.A,
                                new SubstitutionNode(
                                    "Y",
                                    TermersetzungTest.A,
                                    new SubstitutionNode("Z", TermersetzungTest.A, null)
                                )
                            )
                        )
                    )
                )
            );
            System.out.println();
            TermersetzungTest.test(
                "matcher 1",
                () -> "[]".equals(
                    Termersetzung.toString(Termersetzung.matcher(TermersetzungTest.FXY, TermersetzungTest.FXY))
                )
            );
            TermersetzungTest.test(
                "matcher 2",
                () -> "[X/s(Y)]".equals(
                    Termersetzung.toString(
                        Termersetzung.matcher(
                            TermersetzungTest.createTerm("plus", TermersetzungTest.O, TermersetzungTest.X),
                            TermersetzungTest.PLUSOSY
                        )
                    )
                )
            );
            TermersetzungTest.test(
                "matcher 3",
                () -> "[X/s(o),Y/s(Z)]".equals(
                    Termersetzung.toString(
                        Termersetzung.matcher(
                            TermersetzungTest.createTerm("plus", TermersetzungTest.SX, TermersetzungTest.Y),
                            TermersetzungTest.createTerm(
                                "plus",
                                TermersetzungTest.SSO,
                                TermersetzungTest.createTerm("s", TermersetzungTest.Z)
                            )
                        )
                    )
                )
            );
        }
    }

    private static Term createTerm(final String name, final Term... args) {
        return new Term(name, false, TermersetzungTest.toTermList(args));
    }

    private static void test(final String name, final Supplier<Boolean> assertion) {
        try {
            System.out.println(String.format("%s: %s", name, assertion.get() ? "PASSED" : "FAILED"));
        } catch (final Exception e) {
            System.out.println(String.format("%s: FAILED", name));
        }
    }

    private static TermList toTermList(final Term[] args) {
        TermListNode current = null;
        for (int i = args.length - 1; i >= 0; i--) {
            current = new TermListNode(args[i], current);
        }
        return new TermList(current);
    }


}
