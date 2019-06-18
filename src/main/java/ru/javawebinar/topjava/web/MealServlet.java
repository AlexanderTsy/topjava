package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init() {
        MealDao.addAllMeals(MealsUtil.getMealsList());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getRequestURI().substring(request.getContextPath().length()+1);
        log.debug("request to: "+path);
        if (path.compareToIgnoreCase("meals")==0) {
            if (!(request.getQueryString() == null || request.getQueryString().isEmpty())){
                if (request.getParameter("action").compareToIgnoreCase("delete")==0){
                    int id = Integer.parseInt(request.getParameter("id"));
                    log.debug("deleting meal with id="+id);
                    MealDao.deleteById(id);
                }
            }
            log.debug("redirect to meals.jsp");
            request.setAttribute("mealsList", MealsUtil.getFilteredWithExcess(MealDao.getAllMeals(),
                    LocalTime.of(00,00),
                    LocalTime.of(23,00, 59, 999999999),
                    2000));
            getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);
        }
        else if (path.compareToIgnoreCase("meal")==0) {
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("redirect to meal.jsp?id="+id);
            request.setAttribute("meal", MealDao.getMealById(id));
            getServletContext().getRequestDispatcher("/meal.jsp?id="+id).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Meal add or edit");
        request.setAttribute("meal", MealDao.getMealById(1));
        getServletContext().getRequestDispatcher("/meal.jsp").forward(request, response);
    }
}
