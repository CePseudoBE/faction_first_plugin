package be.cepseudo.first_plugin;

import be.cepseudo.first_plugin.commands.*;
import be.cepseudo.first_plugin.listeners.BlockProtectionListener;
import be.cepseudo.first_plugin.listeners.HitPlayerListener;
import be.cepseudo.first_plugin.manager.ClaimManager;
import be.cepseudo.first_plugin.manager.FactionManager;
import be.cepseudo.first_plugin.storage.MemoryFactionStorage;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class FirstPlugin extends JavaPlugin {
    private FactionManager factionManager;
    private ClaimManager claimManager;
    private MemoryFactionStorage factionStorage;

    @Override
    public void onEnable() {
        factionStorage = new MemoryFactionStorage();
        factionManager = new FactionManager(factionStorage);
        claimManager = new ClaimManager();

        getServer().getPluginManager().registerEvents(new HitPlayerListener(factionManager), this);
        getServer().getPluginManager().registerEvents(new BlockProtectionListener(claimManager, factionManager), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(buildCommand());
        });
    }

    private LiteralCommandNode<CommandSourceStack> buildCommand() {
        // Création de la commande principale "/f"
        return LiteralArgumentBuilder.<CommandSourceStack>literal("f")
                .then(new CreateFactionCommand(factionManager).build()) // Ajout de /f create
                .then(new ShowFactionCommand(factionManager).build())   // Ajout de /f show
                .then(new DisbandFactionCommand(factionManager).build()) // Ajout de /f disband
                .then(new LeaveFactionCommand(factionManager).build()) // Ajout de /f leave
                .then(new InvitInFactionCommand(factionManager).build())
                .then(new JoinFactionCommand(factionManager).build())
                .then(new ClaimCommand(claimManager, factionManager).build())
                .then(new UnclaimCommand(claimManager, factionManager).build())
                .build();
    }
}
