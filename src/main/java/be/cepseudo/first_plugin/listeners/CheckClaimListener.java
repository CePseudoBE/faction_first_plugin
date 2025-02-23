package be.cepseudo.first_plugin.listeners;

import be.cepseudo.first_plugin.entities.Faction;
import be.cepseudo.first_plugin.manager.ClaimManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static be.cepseudo.first_plugin.utils.CommandUtils.toPlayerTitle;

public class CheckClaimListener implements Listener {
    private final ClaimManager claimManager;

    public CheckClaimListener(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        Chunk fromChunk = from.getChunk();
        Chunk toChunk = to.getChunk();

        if (!fromChunk.equals(toChunk)) {
            Faction factionToChunk = claimManager.getFactionByChunk(toChunk);
            Faction factionFromChunk = claimManager.getFactionByChunk(fromChunk);

            if (factionToChunk != null && !factionToChunk.equals(factionFromChunk)) {
                toPlayerTitle(player, "<yellow>" + factionToChunk.getName(), "<green>Vous entrez");
            } else if (factionFromChunk != null && factionToChunk == null) {
                toPlayerTitle(player, "<yellow>" + factionFromChunk.getName(), "<red>Vous quittez");
            }
        }
    }
}
