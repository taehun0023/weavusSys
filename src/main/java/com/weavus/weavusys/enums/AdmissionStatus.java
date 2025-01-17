package com.weavus.weavusys.enums;

public enum AdmissionStatus {
   지원중(0),
    일차합격(1),
    이차합격(2),
    내정중(3),
    내정확정(4),
    합격(5),
    불합격(6);



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
