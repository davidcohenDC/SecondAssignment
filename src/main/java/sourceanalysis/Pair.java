package sourceanalysis;

public record Pair<L, R>(L left, R right) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        if (!left.equals(pair.left)) {
            return false;
        }
        return right.equals(pair.right);
    }

    @Override
    public String toString() {
        return "walker.Pair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
