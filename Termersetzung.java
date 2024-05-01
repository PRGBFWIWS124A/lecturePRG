
public class Termersetzung {

    static Term apply(final Term term, final Substitution substitution) {
        //TODO
        return null;
    }

    static boolean equals(final Term term1, final Term term2) {
        if (term1 == null) {
            return term2 == null;
        }
        if (term2 == null) {
            return false;
        }
        return term1.variable() == term2.variable()
            && term1.name().equals(term2.name())
            && Termersetzung.equals(term1.arguments().root(), term2.arguments().root());
    }

    static boolean equals(final TermListNode node1, final TermListNode node2) {
        if (node1 == null) {
            return node2 == null;
        }
        if (node2 == null) {
            return false;
        }
        return Termersetzung.equals(node1.term(), node2.term()) && Termersetzung.equals(node1.next(), node2.next());
    }

    static int getArity(final Term term) {
        return Termersetzung.getArity(term.arguments().root());
    }

    static int getArity(final TermListNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Termersetzung.getArity(node.next());
    }

    static Term getSubstitutedTerm(final String variable, final Substitution substitution) {
        return Termersetzung.getSubstitutedTerm(variable, substitution.root());
    }

    static Term getSubstitutedTerm(final String variable, final SubstitutionNode node) {
        if (node == null) {
            return null;
        }
        if (node.variable().equals(variable)) {
            return node.term();
        }
        return Termersetzung.getSubstitutedTerm(variable, node.next());
    }

    static Substitution matcher(final Term term, final Term toMatch) {
        return Termersetzung.matcher(term, toMatch, new Substitution(null));
    }

    static Substitution matcher(final Term term, final Term toMatch, final Substitution currentMatcher) {
        if (Termersetzung.equals(term, toMatch)) {
            return currentMatcher;
        }
        if (term.variable()) {
            return Termersetzung.union(
                currentMatcher,
                new Substitution(new SubstitutionNode(term.name(), toMatch, null))
            );
        }
        if (
            toMatch.variable()
            || !term.name().equals(toMatch.name())
            || Termersetzung.getArity(term) != Termersetzung.getArity(toMatch)
        ) {
            return null;
        }
        Substitution matcher = currentMatcher;
        TermListNode currentArgument = term.arguments().root();
        TermListNode currentArgumentToMatch = toMatch.arguments().root();
        while (matcher != null && currentArgument != null) {
            matcher =
                Termersetzung.union(
                    matcher,
                    Termersetzung.matcher(currentArgument.term(), currentArgumentToMatch.term())
                );
            currentArgument = currentArgument.next();
            currentArgumentToMatch = currentArgumentToMatch.next();
        }
        return matcher;
    }

    static SubstitutionNode reduce(final String variable, final SubstitutionNode node) {
        if (node == null) {
            return null;
        }
        if (node.variable().equals(variable)) {
            return node.next();
        }
        return new SubstitutionNode(node.variable(), node.term(), Termersetzung.reduce(variable, node.next()));
    }

    static Term rewrite(final Rule rule, final Term term) {
        //TODO
        return null;
    }

    static String toString(final Substitution substitution) {
        //TODO
        return null;
    }

    static String toString(final Term term) {
        //TODO
        return null;
    }

    static Substitution union(final Substitution s1, final Substitution s2) {
        if (s1.root() == null) {
            return s2;
        }
        if (s2.root() == null) {
            return s1;
        }
        final SubstitutionNode union = Termersetzung.union(s1.root(), s2.root());
        if (union == null) {
            return null;
        }
        return new Substitution(union);
    }

    static SubstitutionNode union(final SubstitutionNode node1, final SubstitutionNode node2) {
        if (node1 == null) {
            return node2;
        }
        final Term substituted = Termersetzung.getSubstitutedTerm(node1.variable(), node2);
        if (substituted == null) {
            final SubstitutionNode union = Termersetzung.union(node1.next(), node2);
            if (union == null) {
                return null;
            }
            return new SubstitutionNode(node1.variable(), node1.term(), union);
        }
        if (!Termersetzung.equals(substituted,node1.term())) {
            return null;
        }
        final SubstitutionNode reduced = Termersetzung.reduce(node1.variable(), node2);
        if (reduced == null) {
            return new SubstitutionNode(node1.variable(), substituted, node1.next());
        }
        return new SubstitutionNode(node1.variable(), substituted, Termersetzung.union(node1.next(), reduced));
    }
}
