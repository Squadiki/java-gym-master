package ru.yandex.practicum.gym;

public class CounterOfTrainings {

    private Coach coach;
    private int countTrainingSessions;

    public CounterOfTrainings(Coach coach, int countTrainingSessions) {
        this.coach = coach;
        this.countTrainingSessions = countTrainingSessions;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCountTrainingSessions() {
        return countTrainingSessions;
    }

    public void setCountTrainingSessions(int countTrainingSessions) {
        this.countTrainingSessions = countTrainingSessions;
    }

    @Override
    public String toString() {
        return "Тренер: " + coach + ", количество тренировок: " + countTrainingSessions + ".";
    }
}
