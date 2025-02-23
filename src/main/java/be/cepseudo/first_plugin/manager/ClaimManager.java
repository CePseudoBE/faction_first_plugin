package be.cepseudo.first_plugin.manager;

import be.cepseudo.first_plugin.entities.Claim;
import be.cepseudo.first_plugin.entities.Faction;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.Map;

public class ClaimManager {
    private final Map<Claim, Faction> claims = new HashMap<>();

    /**
     * Vérifie si un chunk est réclamé.
     */
    public boolean isClaimed(Chunk chunk) {
        return claims.containsKey(new Claim(chunk.getX(), chunk.getZ()));
    }

    /**
     * Récupère la faction propriétaire d’un chunk.
     */
    public Faction getFactionByChunk(Chunk chunk) {
        return claims.get(new Claim(chunk.getX(), chunk.getZ()));
    }

    /**
     * Réclame un chunk pour une faction.
     */
    public void claimChunk(Chunk chunk, Faction faction) {
        claims.put(new Claim(chunk.getX(), chunk.getZ()), faction);
    }

    /**
     * Annule le claim d’un chunk.
     */
    public void unclaimChunk(Chunk chunk) {
        claims.remove(new Claim(chunk.getX(), chunk.getZ()));
    }
}
