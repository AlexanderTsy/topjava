package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao {
    private static AtomicInteger maxId = new AtomicInteger(0);
    private static List<Meal> mealList = new CopyOnWriteArrayList<>();

    public static Meal getMealById(int id) throws NoSuchElementException {
        return mealList.stream().filter(meal -> meal.getId().get() == id).findFirst().get();
    }

    public static void addMeal(LocalDateTime dateTime, String description, int calories) {
        mealList.add(new Meal(maxId.incrementAndGet(), dateTime, description, calories));
    }

    public static void addAllMeals(List<Meal> meals) {
        meals.stream().forEach(m -> MealDao.addMeal(m.getDateTime(), m.getDescription(), m.getCalories()));
    }

    public static List<Meal> getAllMeals() {
        return mealList;
    }

    public static void deleteById(int id) {
        mealList.remove(getMealById(id));
    }
}
