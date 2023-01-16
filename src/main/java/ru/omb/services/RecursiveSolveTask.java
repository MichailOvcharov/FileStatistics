package ru.omb.services;

import lombok.Getter;
import lombok.Setter;
import ru.omb.Statistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
** Класс реализовывает рекурсивный обход дерева файловой системы
 */
@Setter
@Getter
public class RecursiveSolveTask implements Callable<HashMap<String, Statistics>> {
    private long                        sizeFiles          = 0l;
    private List<String>                lines              = new ArrayList<>();
    private long                        countLines         = 0l;
    private long                        countNotEmptyLines = 0l;
    private long                        comments           = 0l;
    private UtilityParameters           utilityParameters;
    private ArrayList<String>           includeExtension;
    private ArrayList<String>           excludeExtension;
    private ArrayList<String>           gitIgnoreExtension;
    private HashMap<String, Statistics> statistics         = new HashMap<>();

    public RecursiveSolveTask(UtilityParameters utilityParameters) {
        this.utilityParameters = utilityParameters;
        this.includeExtension = utilityParameters.getIncludeExt();
        this.excludeExtension = utilityParameters.getExcludeExt();
    }

    @Override
    public HashMap<String, Statistics> call() throws Exception {
        System.out.println("--------------------- Рекурсивный обход дерева файловой системы ---------------------");
        System.out.println("Стартовая директория обхода : " + utilityParameters.getStartingDir().toString());
        System.out.println();
        walk(utilityParameters.getStartingDir().toString());
        return getStatistics();
    }

    // Рекурсивный обход
    public void walk(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return;

        for (File file : list) {
            if (file.isDirectory()) {
                walk(file.getAbsolutePath());
            } else {
                String[] filePart = file.toPath().getFileName().toString().split("\\.");
                String extension = filePart[filePart.length - 1];
                // Расширение должно быть включено в список --include-ext
                boolean include_check = includeExtension.stream()
                                                        .filter(Objects::nonNull)
                                                        .anyMatch(_recursive -> _recursive.equals(extension));
                // Расширение не должно быть в списоке --exclude-ext
                boolean exclude_check = excludeExtension.stream()
                                                        .filter(Objects::nonNull)
                                                        .anyMatch(_recursive -> _recursive.equals(extension));
                if (include_check && !exclude_check) {
                    // Сбор статистики
                    sizeFiles = Files.size(file.toPath());
                    lines = Files.readAllLines(file.toPath());
                    countLines = lines.size();
                    countNotEmptyLines = lines.stream()
                                              .filter(string -> string.length() > 0)
                                              .count();
                    comments = lines.stream()
                                    .filter(string -> (string.startsWith("#") || string.startsWith("//") || string.startsWith("#!")))
                                    .count();
                    if (statistics.get(extension) == null) {
                        Statistics statisticsValue = new Statistics();
                        statisticsValue.setCount(1);
                        statisticsValue.setSize(sizeFiles);
                        statisticsValue.setCountAllLine(countLines);
                        statisticsValue.setCountNonEmptyLine(countNotEmptyLines);
                        statisticsValue.setCountCommentLine(comments);
                        statistics.put(extension, statisticsValue);
                    } else {
                        Statistics statisticsValue = statistics.get(extension);
                        statisticsValue.setCount(statisticsValue.getCount() + 1);
                        statisticsValue.setSize(statisticsValue.getSize() + sizeFiles);
                        statisticsValue.setCountAllLine(statisticsValue.getCountAllLine() + countLines);
                        statisticsValue.setCountCommentLine(statisticsValue.getCountCommentLine() + comments);
                        statisticsValue.setCountNonEmptyLine(statisticsValue.getCountNonEmptyLine() + countNotEmptyLines);
                        statistics.put(extension, statisticsValue);
                    }
                }
            }
        }
    }
}
