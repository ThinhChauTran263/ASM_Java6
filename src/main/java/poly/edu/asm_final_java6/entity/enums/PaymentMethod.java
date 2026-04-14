package poly.edu.asm_final_java6.entity.enums;

public enum PaymentMethod {
    CARD("Credit/Debit Card"),
    COD("Cash on Delivery"),
    BANK("Bank Transfer");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
