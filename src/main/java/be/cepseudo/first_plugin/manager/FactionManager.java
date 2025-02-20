package be.cepseudo.first_plugin.manager;

import be.cepseudo.first_plugin.entities.Faction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FactionManager {
    private final Map<String, Faction> factions = new HashMap<>();

    public boolean factionExists(String name) {
        return factions.containsKey(name);
    }

    public void createFaction(String name, UUID leader) {
        if (factionExists(name)) {
            throw new IllegalArgumentException("Une faction avec ce nom existe déjà.");
        }
        Faction faction = new Faction(name, leader);
        factions.put(name, faction);
    }

    public UUID getFactionLeader(String factionName) {
        Faction faction = factions.get(factionName);
        if (faction != null) {
            return faction.getLeader();
        }
        return null;
    }
}
