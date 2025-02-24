package be.cepseudo.first_plugin.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum Message {
    NO_PERMISSION("<red> Vous n'avez pas la permission de faire ça."),
    FACTION_EXISTS("<red> Une faction avec ce nom existe déjà."),
    FACTION_CREATED("<green> Faction créée avec succès !"),
    CHUNK_CLAIMED("<green> Vous avez claim ce chunk avec succès."),
    CHUNK_ALREADY_CLAIMED("<red> Ce chunk est déjà claim."),
    NOT_IN_FACTION("<yellow> Vous n'êtes dans aucune faction.");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public Component get() {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
