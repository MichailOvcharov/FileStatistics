package ru.omb;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.omb.services.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        UtilityParameters utilityParameters = new UtilityParameters();
        utilityParameters = utilityParameters.getUtilityParameters(args);

        ExecutorService service = Executors.newFixedThreadPool(utilityParameters.getThreadCount());
        if (!utilityParameters.getRecursive()) {
            Future<HashMap<String, Statistics>> task = service.submit(new WalkFileSolveTask(utilityParameters));
            // ждем пока задача выполнится
            while (!task.isDone()) {
                Thread.sleep(1);
            }
            // пробуем получить результат задачи
            // получим или результат или исключение, если оно было при выполнении задачи
            try {
                HashMap<String, Statistics> result = task.get();
                PrintSolveTask printResult = new PrintResult();
                printResult.printResult(utilityParameters.getOutput(), result);
            } catch (Exception ie) {
                ie.printStackTrace(System.err);
            }
            // останавливаем ThreadPool.
            service.shutdown();
        } else {
            Future<HashMap<String, Statistics>> taskRec = service.submit(new RecursiveSolveTask(utilityParameters));
            // ждем пока задача выполнится
            while (!taskRec.isDone()) {
                Thread.sleep(1);
            }
            // пробуем получить результат задачи
            // получим или результат или исключение, если оно было при выполнении задачи
            try {
                HashMap<String, Statistics> result = taskRec.get();
                PrintSolveTask printResult = new PrintResult();
                printResult.printResult(utilityParameters.getOutput(), result);
            } catch (Exception ie) {
                ie.printStackTrace(System.err);
            }
            //5 останавливаем ThreadPool.
            service.shutdown();
        }
    }
}
