package be.cepseudo.first_plugin.manager;

import be.cepseudo.first_plugin.entities.Faction;

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
}
