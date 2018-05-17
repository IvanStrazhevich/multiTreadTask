package by.epam.task2.factory;

import java.util.ArrayList;

public interface RouteFactory<T> {
    T createRoutes(ArrayList<String> figuresList);
}
