package devrep.projet.devmed.entities;

public class Etat {
    private boolean connected;
    private boolean badEmail; // can be used in signup
    private boolean isPro; // true: professionnel
    private boolean wrongPass;
    private Utilisateur who;
    
    public Etat() {
        this.badEmail = false;
        this.connected = false;
        this.wrongPass = false;
        this.who = new Utilisateur();
        this.who.setNom("Guest");
        this.isPro = false;
    }
    public Etat(Etat otherState) {
        this.badEmail = otherState.badEmail;
        this.wrongPass = otherState.wrongPass;
        this.connected = otherState.connected;
        this.isPro = otherState.isPro;
        this.who = otherState.who;
    }

    public boolean isConnected() {
        return connected;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public Utilisateur getWho() {
        return who;
    }
    public void setWho(Utilisateur who) {
        this.who = who;
    }
    public boolean isBadEmail() {
        return badEmail;
    }
    public void setBadEmail(boolean badEmail) {
        this.badEmail = badEmail;
    }
    public boolean isWrongPass() {
        return wrongPass;
    }
    public void setWrongPass(boolean wrongPass) {
        this.wrongPass = wrongPass;
    }
    public boolean isPro() {
        return isPro;
    }
    public void setPro(boolean isPro) {
        this.isPro = isPro;
    }
}
