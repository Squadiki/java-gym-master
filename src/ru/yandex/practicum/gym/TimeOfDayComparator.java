package ru.yandex.practicum.gym;

import java.util.Comparator;

public class TimeOfDayComparator implements Comparator<TimeOfDay>{

    @Override
    public int compare(TimeOfDay time1, TimeOfDay time2) {
        if (time1.getHours() != time2.getHours()) {
            return Integer.compare(time1.getHours(), time2.getHours());
        } else {
            return Integer.compare(time1.getMinutes(), time2.getMinutes());
        }
    };
}
