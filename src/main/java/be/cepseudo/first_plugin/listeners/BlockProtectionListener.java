package be.cepseudo.first_plugin.listeners;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.ClaimManager;
import be.cepseudo.first_plugin.manager.FactionManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static be.cepseudo.first_plugin.utils.CommandUtils.sendMessage;

public class BlockProtectionListener implements Listener {
    private final ClaimManager claimManager;
    private final FactionManager factionManager;

    public BlockProtectionListener(ClaimManager claimManager, FactionManager factionManager) {
        this.claimManager = claimManager;
        this.factionManager = factionManager;
    }

    /**
     * Empêche de casser des blocs dans un chunk réclamé si le joueur n’est pas autorisé.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        Faction ownerFaction = claimManager.getFactionByChunk(chunk);
        if (ownerFaction == null) return;

        if (!factionManager.isPlayerInFaction(player.getUniqueId()) ||
                !factionManager.getFactionByPlayer(player.getUniqueId()).equals(ownerFaction)) {

            event.setCancelled(true);
            sendMessage(player, "<red>⛔ Vous ne pouvez pas casser des blocs dans ce territoire !");
        }
    }

    /**
     * Empêche de poser des blocs dans un chunk réclamé si le joueur n’est pas autorisé.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        Faction ownerFaction = claimManager.getFactionByChunk(chunk);
        if (ownerFaction == null) return;

        if (!factionManager.isPlayerInFaction(player.getUniqueId()) ||
                !factionManager.getFactionByPlayer(player.getUniqueId()).equals(ownerFaction)) {

            event.setCancelled(true);
            sendMessage(player, "<red>⛔ Vous ne pouvez pas construire ici !");
        }
    }
}
