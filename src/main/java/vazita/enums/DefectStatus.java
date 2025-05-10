package vazita.enums;


public enum DefectStatus {
    PENDING("P"),
    SUBMITTED("S"),
    REVIEWED("R"),
    REJECTED("X");
    
    private final String code;
    
    DefectStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public static DefectStatus fromCode(String code) {
        for (DefectStatus status : DefectStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No DefectStatus with code " + code);
    }
}