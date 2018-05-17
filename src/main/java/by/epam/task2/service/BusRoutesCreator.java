package by.epam.task2.service;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import by.epam.task2.factory.BusRouteFactory;
import by.epam.task2.factory.RouteFactory;
import by.epam.task2.parser.SourceParsable;
import by.epam.task2.parser.SourceParser;
import by.epam.task2.reader.SourceReadable;
import by.epam.task2.reader.SourceReader;

import java.util.ArrayList;

public class BusRoutesCreator {
    private ArrayList<BusRoute> routes;

    public ArrayList<BusRoute> serviseRoutes() {
        SourceReadable sourceReader = new SourceReader();
        SourceParsable sourceParser = new SourceParser();
        RouteFactory routeFactory = BusRouteFactory.getInstance();
        try {
            routes = routeFactory.createRoutes(sourceParser.createRouteDataListFromList
                    (sourceReader.readSource("data/busRoutes.txt")));
        } catch (ExtendedException e) {
            e.printStackTrace();
        }
        return routes;
    }
}
