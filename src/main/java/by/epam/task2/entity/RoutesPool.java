package by.epam.task2.entity;

import java.util.ArrayList;

public class RoutesPool {
    private static RoutesPool instance = new RoutesPool(new ArrayList<>());
    private ArrayList<BusRoute> routesPool;

    private RoutesPool(ArrayList<BusRoute> routesPool) {
        this.routesPool = routesPool;
    }

    public static RoutesPool getInstance() {
        return instance;
    }
}
