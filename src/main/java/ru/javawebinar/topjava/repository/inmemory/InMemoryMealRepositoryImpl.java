package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    {
        log.info("Meals list initialised.");
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        else {
            if (mealBelongsToAuthorisedUser(meal.getId())){
                // treat case: update, but absent in storage
                return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
            else {
                log.warn(java.time.LocalDateTime.now().toString()+" Unauthorised attempt to save a meal.");
                return null;
            }
        }

    }

    @Override
    public boolean delete(int id) {
        if (mealBelongsToAuthorisedUser(id)) {
            return repository.remove(id) != null;
        }
        else {
            log.warn(java.time.LocalDateTime.now().toString()+" Unauthorised attempt to delete a meal.");
            return false;
        }
    }

    @Override
    public Meal get(int id) {

        if (mealBelongsToAuthorisedUser(id))
        {
            return repository.get(id);
        }
        else {
            log.warn(java.time.LocalDateTime.now().toString()+" Unauthorised attempt to retrieve a meal.");
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll() {
        int authorisedUserId = SecurityUtil.authUserId();
        return repository.values().stream()
                .filter(m -> m.getUserId() == authorisedUserId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean mealBelongsToAuthorisedUser(int mealId) {
        Meal meal = repository.get(mealId);
        if (meal.getUserId() == SecurityUtil.authUserId())
        {
            return true;
        }
        else {
            return false;
        }
    }
}

