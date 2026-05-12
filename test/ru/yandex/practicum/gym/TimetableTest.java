package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(1, mondaySessions.size());
        assertSame(singleTrainingSession, mondaySessions.firstEntry().getValue().get(0));

        //Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(1, mondaySessions.size());
        assertSame(mondayChildTrainingSession, mondaySessions.firstEntry().getValue().get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        assertEquals(2, thursdaySessions.size());

        assertSame(thursdayChildTrainingSession, thursdaySessions.firstEntry().getValue().get(0));

        assertSame(thursdayAdultTrainingSession, thursdaySessions.lastEntry().getValue().get(0));

        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessionsAt13 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, mondaySessionsAt13.size());
        assertSame(singleTrainingSession, mondaySessionsAt13.get(0));

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }

    @Test
    void testGetTrainingSessionsForDaySeveralSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Пётр", "Алексеевич");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        // Проверить, что за понедельник вернулось одно время тренировки
        assertEquals(1, mondaySessions.size());

        List<TrainingSession> sessionsAt18 = mondaySessions.get(new TimeOfDay(18, 0));

        // Проверить, что в 18:00 вернулось два занятия
        assertEquals(2, sessionsAt18.size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeSeveralSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Петров", "Иван", "Сергеевич");

        Group group1 = new Group("Бокс", Age.ADULT, 90);
        Group group2 = new Group("Кикбоксинг", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 0));

        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> result =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, new TimeOfDay(19, 0));

        // Проверить, что за пятницу в 19:00 вернулось два занятия
        assertEquals(2, result.size());
    }

    @Test
    void testGetTrainingSessionsForDayCorrectOrder() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Алексей", "Олегович");
        Group group = new Group("Плавание", Age.CHILD, 45);

        TrainingSession earlySession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));

        TrainingSession lateSession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(lateSession);
        timetable.addNewTrainingSession(earlySession);

        TreeMap<TimeOfDay, List<TrainingSession>> result = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);

        // Проверить, что первой идёт тренировка в 10:00
        assertEquals(10, result.firstKey().getHours());

        // Проверить, что последней идёт тренировка в 18:00
        assertEquals(18, result.lastKey().getHours());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить, что для пустого расписания вернулся пустой список
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(15, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить, что вернулся один тренер
        assertEquals(1, result.size());

        // Проверить, что у тренера две тренировки
        assertEquals(2, result.get(0).getCountTrainingSessions());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить, что вернулось два тренера
        assertEquals(2, result.size());

        // Проверить, что у первого тренера две тренировки
        assertEquals(2, result.get(0).getCountTrainingSessions());

        // Проверить, что у второго тренера одна тренировка
        assertEquals(1, result.get(1).getCountTrainingSessions());
    }
}