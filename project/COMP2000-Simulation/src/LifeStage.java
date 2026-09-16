public final class LifeStage {
    public static final int SEED = 0;
    public static final int SEEDLING = 1;
    public static final int JUVENILE = 2;
    public static final int ADULT = 3;
    public static final int DEAD = 4;

    public static final int STAGE_COUNT = 5;

    private LifeStage() {
        // Non-instantiable utility class
    }

    public static void validate(int stage) throws IllegalArgumentException {
        if (stage < 0 || stage >= STAGE_COUNT) {
            throw new IllegalArgumentException("Invalid lifecycle stage: " + stage);
        }
    }

    public static boolean isDead(int stage) {
        return stage == DEAD;
    }
}