package com.weavus.weavusys.enums;

public enum AdmissionStatus {
   대기중(0),
    합격(1),
    불합격(2);

   private final int value;

    AdmissionStatus(int value) {
         this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AdmissionStatus fromValue(int value) {
        for (AdmissionStatus status : AdmissionStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }

}
