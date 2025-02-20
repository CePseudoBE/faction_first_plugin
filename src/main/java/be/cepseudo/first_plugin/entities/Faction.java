package be.cepseudo.first_plugin.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Faction {
    private final String name;
    private final UUID leader;
    private final Set<UUID> members;

    public Faction(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
        this.members = new HashSet<>();
        this.members.add(leader);
    }

    public String getName() {
        return name;
    }

    public UUID getLeader() {
        return leader;
    }

    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }

    public boolean addMember(UUID playerUUID) {
        return members.add(playerUUID);
    }

    public boolean removeMember(UUID playerUUID) {
        if (playerUUID.equals(leader)) {
            throw new IllegalArgumentException("Le leader ne peut pas être retiré de la faction.");
        }
        return members.remove(playerUUID);
    }

    public boolean isMember(UUID playerUUID) {
        return members.contains(playerUUID);
    }
}
