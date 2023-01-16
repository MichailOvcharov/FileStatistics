package ru.omb.services;

import lombok.Getter;
import lombok.Setter;
import ru.omb.Statistics;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
** Класс реализовывает интерефес FileVisitor
 */
@Setter
@Getter
public class FileVisitorImpl extends SimpleFileVisitor<Path> {
    private long                        sizeFiles          = 0l;
    private List<String>                lines              = new ArrayList<>();
    private long                        countLines         = 0l;
    private long                        countNotEmptyLines = 0l;
    private long                        comments           = 0l;
    private ArrayList<String>           includeExtension;
    private ArrayList<String>           excludeExtension;
    private ArrayList<String>           gitIgnoreExtension;
    private HashMap<String, Statistics> statistics         = new HashMap<>();

    public FileVisitorImpl(ArrayList<String> includeExtension, ArrayList<String> excludeExtension) {
        this.includeExtension = includeExtension;
        this.excludeExtension = excludeExtension;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (file.toFile().isFile()) {
            String[] filePart = file.getFileName().toString().split("\\.");
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
                sizeFiles = Files.size(file);
                lines = Files.readAllLines(file);
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
        return CONTINUE;
    }
}
