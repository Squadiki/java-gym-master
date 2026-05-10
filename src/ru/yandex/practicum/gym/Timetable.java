package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    // Компаратор времени тренировки
    private final TimeOfDayComparator timeComparator = new TimeOfDayComparator();

    // Компаратор счётчика тренировок для тренеров
    private final CounterOfTrainingsComparator counterOfTrainingsComparator = new CounterOfTrainingsComparator();

    // Обратный компаратор счётчика тренировок
    Comparator<CounterOfTrainings> reversedCounterOfTrainingsComparator = counterOfTrainingsComparator.reversed();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek(); // День тренировки
        TimeOfDay time = trainingSession.getTimeOfDay(); // Время тренировки
        if (timetable.containsKey(day)) {
            TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionTreeMap = timetable.get(day);
            if (trainingSessionTreeMap.containsKey(time)) {
                trainingSessionTreeMap.get(time).add(trainingSession);
            } else {
                List<TrainingSession> trainingSessionList = new ArrayList<>();
                trainingSessionList.add(trainingSession);
                trainingSessionTreeMap.put(time, trainingSessionList);
            }
        } else {
            TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionTreeMap = new TreeMap<>(timeComparator);
            List<TrainingSession> trainingSessionList = new ArrayList<>();
            trainingSessionList.add(trainingSession);
            trainingSessionTreeMap.put(time, trainingSessionList);
            timetable.put(day, trainingSessionTreeMap);
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        } else {
            System.out.println("В этот день никаких занятий не запланировано.");
            return null;
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.containsKey(dayOfWeek)) {
            TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionTreeMap = timetable.get(dayOfWeek);
            if (trainingSessionTreeMap.containsKey(timeOfDay)) {
                return trainingSessionTreeMap.get(timeOfDay);
            } else {
                System.out.println("В это время никаких занятий не запланировано.");
                return null;
            }
        } else {
            System.out.println("В этот день никаких занятий не запланировано.");
            return null;
        }
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, CounterOfTrainings> counterMap = new HashMap<>();

        for (DayOfWeek dayOfWeek : timetable.keySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionTreeMap = timetable.get(dayOfWeek);
            for (TimeOfDay timeOfDay : trainingSessionTreeMap.keySet()) {
                List<TrainingSession> trainingSessionList = trainingSessionTreeMap.get(timeOfDay);
                for (TrainingSession trainingSession : trainingSessionList) {
                    Coach coach = trainingSession.getCoach();
                    CounterOfTrainings counterOfTrainings =
                            counterMap.getOrDefault(coach, new CounterOfTrainings(coach, 0));
                    counterOfTrainings.setCountTrainingSessions(counterOfTrainings.getCountTrainingSessions() + 1);
                    counterMap.put(coach, counterOfTrainings);
                }
            }
        }

        List<CounterOfTrainings> counterOfTrainingsList = new ArrayList<>(counterMap.values());
        counterOfTrainingsList.sort(reversedCounterOfTrainingsComparator);

        return counterOfTrainingsList;
    }
}