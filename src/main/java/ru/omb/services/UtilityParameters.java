package ru.omb.services;

import lombok.Getter;
import lombok.Setter;
import ru.omb.Output;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс описывает параметры улилиты
 */
@Getter
@Setter
public class UtilityParameters {

    // путь до каталога, по которому надо выполнить сбор статистики
    private Path                startingDir;
    // путь каталога для сбора статистики
    private Path                path;
    // выполнять обход дерева рекурсивно
    private Boolean             recursive       = false;
    // глубина рекурсивного обхода
    private Integer             maxDepth;
    // количество потоков, используемых для обхода
    private Integer             threadCount     = 1;
    // обрабатывать файлы только с указанными расширениями
    private ArrayList<String>   includeExt      = new ArrayList<>();
    // не обрабатывать файлы с указанными расширениями
    private ArrayList<String>   excludeExt      = new ArrayList<>();
    // не обратывать файлы указанные в файле .gitignore
    private Boolean             gitIgnore       = true;
    // формат вывода статистики
    private Output              output          = Output.plain;

    public UtilityParameters() {
    }

    public static void main(String[] args) {
    }

    public UtilityParameters getUtilityParameters(String[] args) {
        UtilityParameters utilityParameters = new UtilityParameters();
        List<String> collection = Arrays.asList(args);
        // path
        utilityParameters.setPath(Paths.get(args[0]));
        utilityParameters.setStartingDir(Paths.get(args[0]));
        // --recursive
        Boolean recursive         = Arrays.stream(args)
                                          .filter(Objects::nonNull)
                                          .anyMatch(_recursive -> _recursive.equals("--recursive"));
        utilityParameters.setRecursive(recursive);
        // --max-depth
        String max_depth      = collection.stream()
                                          .filter((s) -> s.contains("--max-depth="))
                                          .findFirst()
                                          .get()
                                          .replace("--max-depth=","");
        utilityParameters.setMaxDepth(Integer.valueOf(max_depth));
        // --thread= orElseThrow
        String thread         = collection.stream()
                                          .filter(Objects::nonNull)
                                          .filter((s) -> s.contains("--thread="))
                                          .findFirst()
                                          .get()
                                          .replace("--thread=","");
        utilityParameters.setThreadCount(Integer.valueOf(thread));
        // --include-ext
        String[] include_ext = collection.stream()
                                         .filter(Objects::nonNull)
                                         .filter((s) -> s.contains("--include-ext="))
                                         .findFirst()
                                         .get()
                                         .replace("--include-ext=", "")
                                         .split(",");
        ArrayList<String> include_extension = new ArrayList<>(Arrays.asList(include_ext));
        utilityParameters.setIncludeExt(include_extension);
        // --exclude-ext
        String[] exclude_ext = collection.stream()
                                         .filter(Objects::nonNull)
                                         .filter((s) -> s.contains("--exclude-ext="))
                                         .findFirst()
                                         .get()
                                         .replace("--exclude-ext=", "")
                                         .split(",");
        ArrayList<String> exclude_extension = new ArrayList<>(Arrays.asList(exclude_ext));
        utilityParameters.setExcludeExt(exclude_extension);
        // --git-ignore
        Boolean git_ignore       = Arrays.stream(args)
                                         .filter(Objects::nonNull)
                                         .anyMatch(_recursive -> _recursive.equals("--git-ignore"));
        utilityParameters.setGitIgnore(git_ignore);
        // --output
        String output        = collection.stream()
                                         .filter(Objects::nonNull)
                                         .filter((s) -> s.contains("--output="))
                                         .findFirst()
                                         .get()
                                         .replace("--output=", "");
        utilityParameters.setOutput(Output.valueOf(output));
        return utilityParameters;
    }
}
