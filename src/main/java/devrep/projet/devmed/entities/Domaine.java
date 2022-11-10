package devrep.projet.devmed.entities;

public enum Domaine {
    Genéraliste("Généraliste"),
    Ophtalmologue("Ophtalmologue"),
    Pédiatre("Pédiatre");

    // quand on définit les valeurs par nous-même on doit ajouter l'attribut, un constructeur et un getter
    String code;
    Domaine(String code){
        this.code = code;
    }
    String getCode() {
        return this.code;
    }
}
