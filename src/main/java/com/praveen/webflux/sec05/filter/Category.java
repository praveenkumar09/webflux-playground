package com.praveen.webflux.sec05.filter;

public enum Category {

    STANDARD("secret123"),
    PRIME("secret456");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Category fromToken(String token) {
        for (Category category : values()) {
            if (category.value.equals(token)) {
                return category;
            }
        }
        return null;
    }
}
