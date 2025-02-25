package be.cepseudo.first_plugin.storage;

import be.cepseudo.first_plugin.entities.Faction;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemoryFactionStorage implements Storage<Faction> {
    private final Map<String, Faction> factions = new HashMap<>();
    private final Map<UUID, String> playerFactionMap = new HashMap<>();

    @Override
    public Optional<Faction> getById(String id) {
        return Optional.ofNullable(factions.get(id));
    }

    public Optional<Faction> getByName(String name) {
        return Optional.ofNullable(factions.get(name));
    }

    public Optional<Faction> getByUUID(UUID uuid) {
        String factionName = playerFactionMap.get(uuid);
        return factionName != null ? Optional.ofNullable(factions.get(factionName)) : Optional.empty();
    }

    @Override
    public void save(Faction faction) {
        factions.put(faction.getName(), faction);
        faction.getMembers().forEach(member -> playerFactionMap.put(member, faction.getName()));
    }

    @Override
    public void delete(String id) {
        Faction faction = factions.remove(id);
        if (faction != null) {
            faction.getMembers().forEach(playerFactionMap::remove);
        }
    }
}
