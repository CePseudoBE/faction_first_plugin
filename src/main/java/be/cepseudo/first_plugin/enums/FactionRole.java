package be.cepseudo.first_plugin.enums;

public enum FactionRole {
    LEADER, MEMBER, OFFICER;

    public boolean canInvite(){
        return this == LEADER || this == OFFICER;
    }

    public boolean canDisband() {
        return this == LEADER;
    }

    public boolean canClaim(){
        return this == LEADER || this == OFFICER;
    }
}
