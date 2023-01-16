package ru.omb.services;

import ru.omb.Output;
import ru.omb.Statistics;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;

public interface PrintSolveTask {
    void printResult(Output output, HashMap<String, Statistics> statistic) throws JAXBException, IOException;
}
