package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Meal UMEAL1 = new Meal(START_SEQ+1+1, LocalDateTime.parse("2019-05-30 10:00:00", DATE_TIME_FORMATTER), "Завтрак", 500);
    public static final Meal UMEAL2 = new Meal(START_SEQ+1+2, LocalDateTime.parse("2019-05-30 14:00:00", DATE_TIME_FORMATTER), "Обед", 1010);
    public static final Meal UMEAL3 = new Meal(START_SEQ+1+3, LocalDateTime.parse("2019-05-30 19:00:00", DATE_TIME_FORMATTER), "Ужин", 500);
    public static final Meal UMEAL4 = new Meal(START_SEQ+1+4, LocalDateTime.parse("2019-05-31 09:00:00", DATE_TIME_FORMATTER), "Кашка", 600);
    public static final Meal UMEAL5 = new Meal(START_SEQ+1+5, LocalDateTime.parse("2019-05-31 13:00:00", DATE_TIME_FORMATTER), "Суп, котлеты, компот", 1400);
    public static final Meal UMEAL6 = new Meal(START_SEQ+1+6, LocalDateTime.parse("2019-05-31 21:00:00", DATE_TIME_FORMATTER), "Кефир", 50);
    public static final Meal UMEAL7 = new Meal(START_SEQ+1+7, LocalDateTime.parse("2019-06-01 10:00:00", DATE_TIME_FORMATTER), "Завтрак", 500);
    public static final Meal UMEAL8 = new Meal(START_SEQ+1+8, LocalDateTime.parse("2019-06-01 13:30:55", DATE_TIME_FORMATTER), "Обед", 500);
    public static final Meal UMEAL9 = new Meal(START_SEQ+1+9, LocalDateTime.parse("2019-06-01 20:00:00", DATE_TIME_FORMATTER), "Ужин", 800);
    public static final Meal ADM_MEAL1 = new Meal(START_SEQ+1+10, LocalDateTime.parse("2019-05-30 08:00:00", DATE_TIME_FORMATTER), "Завтрак админа", 500);
    public static final Meal ADM_MEAL2 = new Meal(START_SEQ+1+11, LocalDateTime.parse("2019-05-30 14:00:00", DATE_TIME_FORMATTER), "Обед админа", 1100);
    public static final Meal ADM_MEAL3 = new Meal(START_SEQ+1+12, LocalDateTime.parse("2019-05-30 21:30:35", DATE_TIME_FORMATTER), "Ужин", 500);
    public static final Meal ADM_MEAL4 = new Meal(START_SEQ+1+13, LocalDateTime.parse("2019-05-31 10:30:00", DATE_TIME_FORMATTER), "Еда", 500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
