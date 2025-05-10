package vazita.enums;


public enum UserRole {
    ADMIN(1, "ROLE_ADMIN"),
    INSPECTOR(2, "ROLE_INSPECTOR"),
    ADJOINT(3, "ROLE_ADJOINT");
    
    private final int codGrp;
    private final String securityRole;
    
    UserRole(int codGrp, String securityRole) {
        this.codGrp = codGrp;
        this.securityRole = securityRole;
    }
    
    public int getCodGrp() {
        return codGrp;
    }
    
    public String getSecurityRole() {
        return securityRole;
    }
    
    public static UserRole fromCodGrp(int codGrp) {
        for (UserRole role : UserRole.values()) {
            if (role.getCodGrp() == codGrp) {
                return role;
            }
        }
        throw new IllegalArgumentException("No UserRole with code " + codGrp);
    }
}
