package be.cepseudo.first_plugin.manager;

import be.cepseudo.first_plugin.entities.Faction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gère les factions dans le serveur.
 * Utilise deux HashMaps :
 * - `factions` pour stocker les factions par leur nom.
 * - `playerFactionMap` pour associer chaque joueur à sa faction.
 */
public class FactionManager {
    private final Map<String, Faction> factions = new HashMap<>(); // Stocke les factions par nom
    private final Map<UUID, String> playerFactionMap = new HashMap<>(); // Associe chaque joueur à sa faction
    private final Map<UUID, Faction> factionInvite = new HashMap<>();

    /**
     * Vérifie si une faction existe en fonction de son nom.
     *
     * @param name Nom de la faction.
     * @return true si la faction existe, false sinon.
     */
    public boolean factionExists(String name) {
        return factions.containsKey(name);
    }

    /**
     * Vérifie si un joueur appartient déjà à une faction.
     *
     * @param playerUUID UUID du joueur.
     * @return true si le joueur est dans une faction, false sinon.
     */
    public boolean isPlayerInFaction(UUID playerUUID) {
        return playerFactionMap.containsKey(playerUUID);
    }

    /**
     * Vérifie si un joueur peut créer une faction.
     * Retourne un message d'erreur s'il y a un problème, ou null si la création est possible.
     *
     * @param name   Nom de la faction.
     * @param leader UUID du joueur qui veut créer la faction.
     * @return Message d'erreur ou null si la création est valide.
     */
    public String canCreateFaction(String name, UUID leader) {
        if (factionExists(name)) {
            return "La faction '" + name + "' existe déjà.";
        }
        if (isPlayerInFaction(leader)) {
            return "Vous faites déjà partie d'une faction.";
        }
        return null; // Aucune erreur, la faction peut être créée.
    }

    /**
     * Crée une faction avec le leader spécifié.
     * Cette méthode suppose que les vérifications ont déjà été effectuées.
     *
     * @param name   Nom de la faction.
     * @param leader UUID du leader de la faction.
     */
    public void createFaction(String name, UUID leader) {
        Faction faction = new Faction(name, leader);
        factions.put(name, faction);
        playerFactionMap.put(leader, name);
    }

    /**
     * Récupère l'UUID du leader d'une faction donnée.
     *
     * @param factionName Nom de la faction.
     * @return L'UUID du leader si la faction existe, null sinon.
     */
    public UUID getFactionLeader(String factionName) {
        Faction faction = factions.get(factionName);
        return (faction != null) ? faction.getLeader() : null;
    }

    /**
     * Récupère la faction d'un joueur.
     *
     * @param playerUUID UUID du joueur.
     * @return La faction du joueur s'il en a une, null sinon.
     */
    public Faction getFactionByPlayer(UUID playerUUID) {
        String factionName = playerFactionMap.get(playerUUID);
        return (factionName != null) ? factions.get(factionName) : null;
    }

    public void deleteFaction(UUID playerUUID) {
        String factionName = playerFactionMap.get(playerUUID);
        if (factionName == null) return; // Sécurité si le joueur n'a pas de faction

        Faction faction = factions.remove(factionName);
        if (faction == null) return; // La faction n'existe pas, rien à faire

        // Supprime tous les membres de la faction de playerFactionMap
        for (UUID member : faction.getMembers()) {
            playerFactionMap.remove(member);
        }
    }

    public void leaveFaction(UUID playerUUID) {
        String factionName = playerFactionMap.get(playerUUID);
        if (factionName == null) return;
        Faction faction = factions.get(factionName);
        if (faction == null) return;
        faction.removeMember(playerUUID);
        playerFactionMap.remove(playerUUID);
    }

    public void broadcastToFaction(Faction faction, String message) {
        if (faction == null) return;

        // Convertir le message en Component
        Component formattedMessage = MiniMessage.miniMessage().deserialize(message);

        for (UUID memberUUID : faction.getMembers()) {
            Player player = Bukkit.getPlayer(memberUUID); // Vérifie si le membre est en ligne
            if (player != null && player.isOnline()) {
                player.sendMessage(formattedMessage); // Envoie le message aux membres connectés
            }
        }
    }

    public void inviteToFaction(UUID leaderUUID, Faction faction, String playerName, PlayerManager playerManager) {
        if (faction == null) return;

        UUID trueLeaderUUID = getFactionLeader(faction.getName());
        if (!leaderUUID.equals(trueLeaderUUID)) return; // Vérifie que le joueur est bien le leader

        UUID targetUUID = getUUIDFromPlayerName(playerName);
        if (targetUUID == null) {
            Bukkit.getPlayer(leaderUUID).sendMessage(Component.text("❌ Ce joueur n'a jamais rejoint le serveur."));
            return;
        }

        if (isPlayerInFaction(targetUUID)) {
            Bukkit.getPlayer(leaderUUID).sendMessage(Component.text("⚠ Ce joueur est déjà dans une faction."));
            return;
        }

        factionInvite.put(targetUUID, faction);
        Bukkit.getPlayer(targetUUID).sendMessage(Component.text("📩 Vous avez été invité à rejoindre la faction " + faction.getName() + ". Faites /f join " + faction.getName() + " pour accepter."));
    }

    public UUID getUUIDFromPlayerName(String playerName) {
        // Vérifie si le joueur est en ligne
        Player target = Bukkit.getPlayerExact(playerName);
        if (target != null) return target.getUniqueId();

        // Vérifie si le joueur est hors ligne mais a déjà rejoint
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(playerName);
        if (offlineTarget.hasPlayedBefore()) return offlineTarget.getUniqueId();

        return null; // Le joueur n'existe pas dans le serveur
    }

    public boolean isInvited(UUID playerUUID) {
        String factionName = playerFactionMap.get(playerUUID);
        if (factionName == null) return false;
        return factionInvite.containsKey(playerUUID);
    }

    public void newPlayerInFaction(UUID playerUUID) {
        String factionName = playerFactionMap.get(playerUUID);
        if (factionName == null) return;
        Faction faction = factions.get(factionName);
        if (faction == null) return;
        faction.addMember(playerUUID);
        factionInvite.remove(playerUUID);
    }

}
