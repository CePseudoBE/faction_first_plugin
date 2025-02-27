package be.cepseudo.first_plugin.entities;

import be.cepseudo.first_plugin.enums.FactionRole;

import java.util.HashMap;
import java.util.UUID;

public class Faction {
    private final String name;
    private final UUID leader;
    private final HashMap<UUID, FactionRole> members;

    public Faction(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
        this.members = new HashMap<>();
        this.members.put(leader, FactionRole.LEADER);
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public HashMap<UUID, FactionRole> getMembers() {
        return new HashMap<>(members);
    }

    public void addMember(UUID playerUUID) {
        if (members.containsKey(playerUUID)) return;
        members.put(playerUUID, FactionRole.MEMBER);
    }

    public void removeMember(UUID playerUUID) {
        if (playerUUID.equals(leader)) {
            throw new IllegalArgumentException("Le leader ne peut pas être retiré de la faction.");
        }
        members.remove(playerUUID);
    }

    public boolean isMember(UUID playerUUID) {
        return members.containsKey(playerUUID);
    }
}
