package by.epam.task2.factory;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import by.epam.task2.parser.SourceParsable;
import by.epam.task2.parser.SourceParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RouteBuilder implements RouteBuildable {
    private static final String DIVIDER = "\\.\\s";
    private static final String NUMBER = "\\d+?";
    private static Logger logger = LogManager.getLogger();
    private BusStopBuildable busStopBuilder;
    private SourceParsable sourceParser;

    private RouteBuilder(BusStopBuildable busStopBuilder, SourceParsable sourceParser) {
        this.busStopBuilder = busStopBuilder;
        this.sourceParser = sourceParser;
    }

    private static RouteBuilder instance;

    public static RouteBuilder getInstance() {
        if (null == instance) {
            instance = new RouteBuilder(BusStopBuilder.getInstance(), new SourceParser());
        }
        return instance;
    }

    @Override
    public ArrayList<BusRoute> createRoutes(ArrayList<String> routesData) throws ExtendedException {
        if (routesData != null) {
            logger.info(routesData + " Data for route");
            ArrayList<BusRoute> busRoutes = new ArrayList<>();
            for (String element : routesData
                    ) {
                String[] singleRouteData = element.split(DIVIDER);
                List<String> cleanData = Arrays.asList(singleRouteData);
                logger.info(cleanData);
                BusRoute route = new BusRoute();
                route.setBusStops(new ArrayDeque<>());
                int busStopOrderId = 0;
                for (String routeData : cleanData
                        ) {
                    if (!routeData.matches(NUMBER)) {
                        route.getBusStops().add(busStopBuilder.createBusStop(routeData, ++busStopOrderId));
                    } else {
                        route.setRouteNumber(Integer.parseInt(routeData));
                    }
                }
                busRoutes.add(route);
            }
            logger.info(busRoutes.toString());
            return busRoutes;
        } else {
            throw new ExtendedException(" Incorrect data");
        }
    }

    public void setBusStopBuildable(BusStopBuildable busStopBuilder) {
        this.busStopBuilder = busStopBuilder;
    }

    public void setSourceParser(SourceParsable sourceParser) {
        this.sourceParser = sourceParser;
    }
}
