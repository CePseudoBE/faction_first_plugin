package be.cepseudo.first_plugin.entities;

import java.util.Objects;

public class Claim {
    private final int x;
    private final int z;

    public Claim(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Claim)) return false;
        Claim claim = (Claim) obj;
        return x == claim.x && z == claim.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
