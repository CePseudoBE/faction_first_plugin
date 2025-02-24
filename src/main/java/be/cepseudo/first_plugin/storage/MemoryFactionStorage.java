package be.cepseudo.first_plugin.storage;

import be.cepseudo.first_plugin.entities.Faction;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryFactionStorage implements Storage<Faction> {
    private final Map<String, Faction> factions = new HashMap<>();

    @Override
    public Optional<Faction> getById(String id) {
        return Optional.ofNullable(factions.get(id));
    }

    @Override
    public void save(Faction faction) {
        factions.put(faction.getName(), faction);
    }

    @Override
    public void delete(String id) {
        factions.remove(id);
    }
}
