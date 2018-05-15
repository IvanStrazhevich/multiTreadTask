package by.epam.task2.factory;

import java.util.ArrayList;

public interface RouteFactory<T> {
    ArrayList<T> createRoutes(ArrayList<String> figuresList);
}
