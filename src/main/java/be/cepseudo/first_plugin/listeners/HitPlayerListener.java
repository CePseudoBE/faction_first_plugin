package be.cepseudo.first_plugin.listeners;

import be.cepseudo.first_plugin.manager.FactionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitPlayerListener implements Listener {
    private final FactionManager factionManager;

    public HitPlayerListener(FactionManager factionManager) {
        this.factionManager = factionManager;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player attacker) || !(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (factionManager.isPlayerInFaction(attacker.getUniqueId()) &&
                factionManager.isPlayerInFaction(victim.getUniqueId()) &&
                factionManager.getFactionByPlayer(attacker.getUniqueId()).equals(factionManager.getFactionByPlayer(victim.getUniqueId()))) {

            event.setCancelled(true);
            attacker.sendMessage("❌ Vous ne pouvez pas frapper un membre de votre faction !");
        }
    }
}
