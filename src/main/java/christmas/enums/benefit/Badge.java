package christmas.enums.benefit;

public enum Badge {
    산타(20000),
    트리(10000),
    별(5000),
    없음(0);

    Badge(int standardPrice) {
        this.standardPrice = standardPrice;
    }
    private final int standardPrice;

    public int getStandardPrice() {
        return standardPrice;
    }
}
