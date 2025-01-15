package com.weavus.weavusys.enums;

public enum OfferStatus {
    대기중(0),
    내정(1),
    내정취소(2);

    private final int value;

    OfferStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OfferStatus fromValue(int value) {
        for (OfferStatus status : OfferStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }
}
