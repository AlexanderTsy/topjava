package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        getFilteredWithExceededOneStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOneStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class FilteredListOnDateWithCaloriesSum extends ArrayList<UserMeal> {
            private int sum = 0;
            @Override
            public boolean add(UserMeal um) {
                sum+=um.getCalories();
                if (TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                return super.add(um);
                else return true;
            }

            public List<UserMealWithExceed> getList() {
                return this.stream()
                        .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(), sum > caloriesPerDay))
                        .collect(Collectors.toList());
            }
        }
        return mealList.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getLocalDate, Collectors.toCollection(FilteredListOnDateWithCaloriesSum::new)))
                .values()
                .stream()
                .flatMap(f -> f.getList().stream())
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumOnDate = mealList.stream()
                .collect(Collectors.toMap(UserMealsUtil::getLocalDate, UserMeal::getCalories, (c1, c2) -> c1 + c2, HashMap::new));
        List<UserMealWithExceed> result = mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(
                        um.getDateTime(),
                        um.getDescription(),
                        um.getCalories(),
                        caloriesSumOnDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay)
                ).collect(Collectors.toList());
        return result;
    }


    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumOnDate = new HashMap<>();
        for (UserMeal um : mealList)
            caloriesSumOnDate.merge(um.getDateTime().toLocalDate(), um.getCalories(), (c1, c2) -> c1 + c2);
        List<UserMealWithExceed> result = new ArrayList<>();
        for (UserMeal um : mealList) {
            if (TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumOnDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    private static LocalDate getLocalDate(UserMeal um) {
        return um.getDateTime().toLocalDate();
    }
}
