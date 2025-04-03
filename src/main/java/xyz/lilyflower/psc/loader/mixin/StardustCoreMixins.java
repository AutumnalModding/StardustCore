package xyz.lilyflower.psc.loader.mixin;

public enum StardustCoreMixins {

    ;

    public final String mixinClass;
    private final Side side;
    public final Phase phase;

    StardustCoreMixins(String mixinClass, Side side, Phase phase) {
        this.mixinClass = mixinClass;
        this.side = side;
        this.phase = phase;
    }

    StardustCoreMixins(String mixinClass) {
        this.mixinClass = mixinClass;
        this.side = Side.BOTH;
        this.phase = Phase.NORMAL;
    }

    StardustCoreMixins(String mixinClass, Phase phase) {
        this.mixinClass = mixinClass;
        this.side = Side.BOTH;
        this.phase = phase;
    }

    public enum Side {
        BOTH,
        CLIENT,
        SERVER
    }

    public enum Phase {
        EARLY,
        NORMAL,
        LATE
    }
}

