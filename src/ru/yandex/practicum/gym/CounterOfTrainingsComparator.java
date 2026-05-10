package ru.yandex.practicum.gym;

import java.util.Comparator;

public class CounterOfTrainingsComparator implements Comparator<CounterOfTrainings> {

    @Override
    public int compare(CounterOfTrainings coach1, CounterOfTrainings coach2) {
        return Integer.compare(coach1.getCountTrainingSessions(), coach2.getCountTrainingSessions());
    }
}
