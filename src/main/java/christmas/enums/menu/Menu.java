package christmas.enums.menu;

public enum Menu {
    양송이수프(6000, Category.에피타이저),
    타파스(5500, Category.에피타이저),
    시저샐러드(8000, Category.에피타이저),

    티본스테이크(55000, Category.메인),
    바비큐립(54000, Category.메인),
    해산물파스타(35000, Category.메인),
    크리스마스파스타(25000, Category.메인),

    초코케이크(15000, Category.디저트),
    아이스크림(5000, Category.디저트),

    제로콜라(3000, Category.음료),
    레드와인(60000, Category.음료),
    샴페인(25000, Category.음료);

    private final int price;
    private final Category category;

    Menu(int price, Category category) {
        this.price = price;
        this.category = category;
    }

    public int price() {
        return price;
    }

    public Category category() {
        return this.category;
    }
}
