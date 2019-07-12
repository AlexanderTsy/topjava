package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100003, USER_ID);
        assertMatch(meal,UMEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundWrongId() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundWrongUser() throws Exception {
        service.get(100003, ADMIN_ID);
    }


    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(UMEAL1.getId(),USER_ID);
        service.get(UMEAL1.getId(),USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongUser() {
        service.delete(UMEAL1.getId(),ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate start = LocalDate.of(2019,6,1);
        LocalDate end = LocalDate.of(2019,6,1);
        List<Meal> filteredMeals = service.getBetweenDates(start, end, USER_ID);
        assertMatch(filteredMeals, UMEAL9, UMEAL8, UMEAL7);
    }

    @Test
    public void getBetweenDateTimes() {

    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, ADM_MEAL4, ADM_MEAL3, ADM_MEAL2, ADM_MEAL1);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(UMEAL1);
        updatedMeal.setDescription("Еда тест обновление");
        updatedMeal.setCalories(112211);
        updatedMeal.setDateTime(LocalDateTime.of(2019,06,9,11,20,00));
        service.update(updatedMeal,USER_ID);
        assertMatch(updatedMeal,service.get(updatedMeal.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongUser(){
        Meal updatedMeal = new Meal(UMEAL1);
        updatedMeal.setDescription("Еда тест обновление");
        updatedMeal.setCalories(112211);
        updatedMeal.setDateTime(LocalDateTime.of(2019,06,9,11,20,0));
        service.update(updatedMeal,ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(),"food", 500);

    }
}