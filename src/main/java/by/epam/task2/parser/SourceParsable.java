package by.epam.task2.parser;

import java.util.ArrayList;

public interface SourceParsable<T> {

    ArrayList<String> createRouteDataListFromList(T routeDataList);

}

