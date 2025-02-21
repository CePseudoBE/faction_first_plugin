package be.cepseudo.first_plugin.manager;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

//TODO TEMPORAIRE : Ajouter cela dans Essentials

public class PlayerManager {
    private final Set<UUID> playerCache = new HashSet<>();

    public boolean hasPlayerJoinedBefore(UUID uuid) {
        return playerCache.contains(uuid);
    }

    public void addPlayerJoinedBefore(UUID uuid) {
        playerCache.add(uuid);
    }
}
