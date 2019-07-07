package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;

import java.util.List;
import java.util.stream.Collectors;

public class MealServiceImpl implements MealService {

    private MealRepository repository=new InMemoryMealRepositoryImpl();
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    @Override
    public Meal create(Meal meal, int userId) {
        if (mealBelongsToAuthorisedUser(meal.getId(), userId)){
            return repository.save(meal);
        }
        else {
            String message = java.time.LocalDateTime.now().toString()+" Unauthorised attempt to save a meal.";
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        if (mealBelongsToAuthorisedUser(id, userId)) {
            if (!repository.delete(id)) throw new NotFoundException(String.format("Not found meal with id=%s for userId=%s", id, userId));
        }
        else {
            String message = java.time.LocalDateTime.now().toString()+" Unauthorised attempt to delete a meal.";
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        if (mealBelongsToAuthorisedUser(id, userId)) {
            return repository.get(id);
        }
        else throw new NotFoundException("Not found");
    }

    @Override
    public void update(Meal meal, int userId) {
        if (mealBelongsToAuthorisedUser(meal.getId(), userId)) {
            repository.save(meal);
        }
        else throw new NotFoundException("Not found");
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> mealList = repository.getAll().stream().filter(m -> mealBelongsToAuthorisedUser(m.getId(), userId)).collect(Collectors.toList());
        if (mealList != null) return mealList;
        else throw new NotFoundException("Not found");
    }

    private boolean mealBelongsToAuthorisedUser(int mealId, int userId) {
        Meal meal = repository.get(mealId);
        if (meal.getUserId() == userId)
        {
            return true;
        }
        else {
            return false;
        }
    }
}