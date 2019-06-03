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
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumOnDate = new HashMap<>();
        List<UserMeal> filteredList = mealList.stream().map(um -> {
                    caloriesSumOnDate.computeIfPresent(um.getDateTime().toLocalDate(), (k, v) -> v + um.getCalories());
                    caloriesSumOnDate.putIfAbsent(um.getDateTime().toLocalDate(), um.getCalories());
                    return um;}).
                filter( um -> um.getDateTime().toLocalTime().isAfter(startTime) && um.getDateTime().toLocalTime().isBefore(endTime)).collect(Collectors.toList());
        List<UserMealWithExceed> result = filteredList.stream().
                map(um -> new UserMealWithExceed(
                        um.getDateTime(),
                        um.getDescription(),
                        um.getCalories(),
                        caloriesSumOnDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay? true : false)
                ).collect(Collectors.toList());
        return result;
    }
    

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        Map<LocalDate, Integer> caloriesSumOnDate = new HashMap<>();
        Map<LocalDate, ArrayList<UserMeal>> filteredUserMealsMap = new HashMap<>();
        for (UserMeal userMeal: mealList) {
            //storing sum of calories on each day in list
            if (caloriesSumOnDate.containsKey(userMeal.getDateTime().toLocalDate())){
                caloriesSumOnDate.put(userMeal.getDateTime().toLocalDate(),
                        caloriesSumOnDate.get(userMeal.getDateTime().toLocalDate())+userMeal.getCalories());
            }
            else {
                caloriesSumOnDate.put(userMeal.getDateTime().toLocalDate(),userMeal.getCalories());
            }
            //storing meals fitting interval and making them exceeded
            if (userMeal.getDateTime().toLocalTime().isAfter(startTime) &&
                userMeal.getDateTime().toLocalTime().isBefore(endTime)) {
                if (filteredUserMealsMap.containsKey(userMeal.getDateTime().toLocalDate())) {
                    filteredUserMealsMap.get(userMeal.getDateTime().toLocalDate()).add(userMeal);
                }
                else {
                    ArrayList<UserMeal> userMealList = new ArrayList<>();
                    userMealList.add(userMeal);
                    filteredUserMealsMap.put(userMeal.getDateTime().toLocalDate(), userMealList);
                }
            }
        }
        List<UserMealWithExceed> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : caloriesSumOnDate.entrySet()) {
            for (UserMeal userMeal: filteredUserMealsMap.get(entry.getKey())){
                result.add(new UserMealWithExceed(
                        userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        entry.getValue()>caloriesPerDay?true:false)
                );
            }
        }
        return result;
    }
}
