package blessed.company.enums;

import lombok.Getter;

@Getter
public enum PlanType {
    ESSENTIAL(10),
    PROFESSIONAL(20),
    ENTERPRISE(-1);

    private final int maxUsers;

    PlanType(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public boolean isUnlimited() {
        return this.maxUsers == -1;
    }

    public boolean canAddUser(long currentUsers) {
        return isUnlimited() || currentUsers < this.maxUsers;
    }

}
