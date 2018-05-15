package by.epam.task2.reader;

import by.epam.task2.exception.ExtendedException;

public interface SourceReadable<T> {
    T readSource(String pointDataSource) throws ExtendedException, ExtendedException;
}
