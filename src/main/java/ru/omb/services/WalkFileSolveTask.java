package ru.omb.services;

import ru.omb.Statistics;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.Callable;


public class WalkFileSolveTask implements Callable<HashMap<String,Statistics>> {

    private UtilityParameters utilityParameters;

    public WalkFileSolveTask(UtilityParameters utilityParameters) {
        this.utilityParameters = utilityParameters;
    }

    @Override
    public HashMap<String,Statistics> call() throws Exception {
        System.out.println("---------------- Обход дерева файловой системы с помощью Files.walkFileTree ---------------------");
        System.out.println("Стартовая директория обхода : " + utilityParameters.getStartingDir().toString());
        System.out.println();
        FileVisitorImpl visitor = new FileVisitorImpl(utilityParameters.getIncludeExt(), utilityParameters.getExcludeExt());
        EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(utilityParameters.getPath(), options, utilityParameters.getMaxDepth(), visitor);
        return visitor.getStatistics();
    }
}
