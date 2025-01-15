package com.weavus.weavusys.enums;

public enum VisaStatus {
    대기중(0),
    비자신청중(1),
    비자신청완료(2);

    private final int value;

    VisaStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VisaStatus fromValue(int value) {
        for (VisaStatus status : VisaStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }
}
