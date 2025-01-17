package com.weavus.weavusys.enums;

public enum Gender {
    남성(0),
    여성(1);
    private final int value;

    Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Gender fromValue(int value) {
        for (Gender status : Gender.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }
}
