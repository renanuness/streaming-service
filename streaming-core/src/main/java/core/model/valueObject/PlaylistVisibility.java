package core.model.valueObject;

public enum PlaylistVisibility {
    PUBLIC("Pública"),
    PRIVATE("Privada"),
    SHARED("Compartilhada"),
    DELETED("Deletada");

    private final String description;
    PlaylistVisibility(String description) {
        this.description = description;
    }

    public String description(){
        return description;
    }
}
