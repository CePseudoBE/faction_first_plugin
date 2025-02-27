package be.cepseudo.first_plugin.manager;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.enums.FactionRole;
import be.cepseudo.first_plugin.storage.MemoryFactionStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionManager {
    private final MemoryFactionStorage storage;

    public FactionManager(MemoryFactionStorage storage) {
        this.storage = storage;
    }

    /**
     * Vérifie si une faction existe en fonction de son nom.
     */
    public boolean factionExists(String name) {
        return storage.getByName(name).isPresent();
    }

    /**
     * Vérifie si un joueur appartient déjà à une faction.
     */
    public boolean isPlayerInFaction(UUID playerUUID) {
        return storage.getByUUID(playerUUID).isPresent();
    }

    /**
     * Vérifie si un joueur peut créer une faction.
     */
    public String canCreateFaction(String name, UUID leader) {
        if (factionExists(name)) return "La faction '" + name + "' existe déjà.";
        if (isPlayerInFaction(leader)) return "Vous faites déjà partie d'une faction.";
        return null;
    }

    /**
     * Crée une faction et l'enregistre en mémoire.
     */
    public void createFaction(String name, UUID leader) {
        Faction faction = new Faction(name, leader);
        storage.save(faction);
    }

    /**
     * Récupère l'UUID du leader d'une faction.
     */
    public UUID getFactionLeader(String factionName) {
        return storage.getByName(factionName).map(Faction::getLeader).orElse(null);
    }

    /**
     * Récupère la faction d'un joueur.
     */
    public Faction getFactionByPlayer(UUID playerUUID) {
        return storage.getByUUID(playerUUID).orElse(null);
    }

    /**
     * Supprime une faction et retire tous ses membres.
     */
    public void deleteFaction(UUID playerUUID) {
        Faction faction = getFactionByPlayer(playerUUID);
        if (faction == null) return;

        if (faction.getLeader().equals(playerUUID)) {
            storage.delete(faction.getName());
        }
    }

    /**
     * Permet à un joueur de quitter sa faction.
     */
    public void leaveFaction(UUID playerUUID) {
        Faction faction = getFactionByPlayer(playerUUID);
        if (faction == null) return;

        if (faction.getLeader().equals(playerUUID)) {
            sendMessage(playerUUID, "<red>⛔ Vous êtes le leader, utilisez /f disband ou promouvez un autre joueur.");
            return;
        }

        faction.removeMember(playerUUID);
        storage.save(faction);
    }

    /**
     * Envoie un message à tous les membres d'une faction.
     */
    public void broadcastToFaction(Faction faction, String message) {
        if (faction == null) return;

        Component formattedMessage = MiniMessage.miniMessage().deserialize(message);
        faction.getMembers().forEach((memberUUID, role) -> {
            Player player = Bukkit.getPlayer(memberUUID);
            if (player != null && player.isOnline()) {
                player.sendMessage(formattedMessage);
            }
        });
    }

    /**
     * Envoie une invitation à un joueur pour rejoindre une faction.
     */
    public void inviteToFaction(UUID leaderUUID, UUID targetUUID) {
        Faction faction = getFactionByPlayer(leaderUUID);
        if (faction == null || !faction.getMembers().get(leaderUUID).canInvite()) return;

        if (isPlayerInFaction(targetUUID)) {
            sendMessage(leaderUUID, "<yellow>⚠ Ce joueur fait déjà partie d'une faction.");
            return;
        }

        sendMessage(targetUUID, "<green>📩 Vous avez été invité à rejoindre la faction <gold>" + faction.getName() + "</gold>.");
        sendMessage(targetUUID, "<yellow>💡 Tapez /f join " + faction.getName() + " pour accepter.");
    }

    /**
     * Récupère l'UUID d'un joueur via son pseudo.
     */
    public UUID getUUIDFromPlayerName(String playerName) {
        Player target = Bukkit.getPlayerExact(playerName);
        if (target != null) return target.getUniqueId();

        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(playerName);
        return offlineTarget.hasPlayedBefore() ? offlineTarget.getUniqueId() : null;
    }

    /**
     * Vérifie si un joueur a été invité.
     */
    public boolean isInvited(UUID playerUUID) {
        return storage.getByUUID(playerUUID).isPresent();
    }

    /**
     * Ajoute un joueur dans une faction suite à une invitation acceptée.
     */
    public void newPlayerInFaction(UUID playerUUID) {
        Faction faction = storage.getByUUID(playerUUID).orElse(null);
        if (faction == null) return;

        faction.addMember(playerUUID);
        storage.save(faction);
    }

    /**
     * Vérifie si un joueur peut revendiquer un chunk.
     */
    public boolean canClaim(UUID playerUUID) {
        Faction faction = getFactionByPlayer(playerUUID);
        if (faction == null) return false;

        return faction.getMembers().getOrDefault(playerUUID, FactionRole.MEMBER).canClaim();
    }

    /**
     * Envoie un message à un joueur spécifique.
     */
    private void sendMessage(UUID playerUUID, String message) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        }
    }
}
