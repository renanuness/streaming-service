package infra.core.entity;

public enum PlaylistVisibilityEntity {
    PUBLIC("Pública"),
    PRIVATE("Privada"),
    SHARED("Compartilhada"),
    DELETED("Deletada");

    private final String description;
    PlaylistVisibilityEntity(String description) {
        this.description = description;
    }

    public String description(){
        return description;
    }
}
