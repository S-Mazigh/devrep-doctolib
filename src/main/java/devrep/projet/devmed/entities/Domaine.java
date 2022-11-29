package devrep.projet.devmed.entities;

public enum Domaine {
    Generaliste("Generaliste"),
    Ophtalmologue("Ophtalmologue"),
    Pediatre("Pediatre"),
    Addictologue("Addictologue"),
    Allergologue("Allergologue"),        
    Dermatologue("Dermatologue"),
    Diabetologue("Diabetologue"), 
    Gynecologue("Gynecologue"), 
    Hematologue("Hematologue"); 
    // quand on definit les valeurs par nous-même on doit ajouter l'attribut, un constructeur et un getter
    String code;
    Domaine(String code){
        this.code = code;
    }
    String getCode() {
        return this.code;
    }
}
