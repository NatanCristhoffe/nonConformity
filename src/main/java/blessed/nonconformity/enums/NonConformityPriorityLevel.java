package blessed.nonconformity.enums;

public enum NonConformityPriorityLevel {
    LOW("Baixa"),
    MEDIUM("Média"),
    HIGH("Alta"),
    CRITICAL("Crítica");

    private final String label;

    NonConformityPriorityLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
