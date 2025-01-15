package com.weavus.weavusys.enums;

public enum ScheduleType {
    면접(0),
    기관(1),
    이벤트(2),
    기타(3),
    회계(4)
    ;

    private final int value;

    ScheduleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ScheduleType fromValue(int value) {
        for (ScheduleType status : ScheduleType.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }
}
