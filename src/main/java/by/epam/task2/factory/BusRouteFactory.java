package by.epam.task2.factory;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BusRouteFactory<T> implements RouteFactory<BusRoute> {
    static Logger logger = LogManager.getLogger();
    private static BusRouteFactory<BusRoute> instance;

    public static BusRouteFactory<BusRoute> getInstance() {
        if (null == instance) {
            instance = new BusRouteFactory<>();
        }
        return instance;
    }

    @Override
    public ArrayList<BusRoute> createRoutes(ArrayList<String> routesList) {
        ArrayList<BusRoute> busRouteList = new ArrayList<>();
        RouteBuildable routeBuilder = RouteBuilder.getInstance();
        for (String route : routesList
                ) {
            try {
                BusRoute busRoute = (BusRoute) routeBuilder.createRoute(route);
                if (route != null) {
                    busRouteList.add(busRoute);
                } else {
                    logger.error(" Points does not build a plane");
                }
            } catch (ExtendedException e) {
                logger.error(" Builder failed", e);
            }
        }
        return busRouteList;
    }
}
