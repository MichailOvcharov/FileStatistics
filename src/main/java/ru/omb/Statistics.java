package ru.omb;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
* Класс описывает данные в разрезе расширения файлов
 */
@Getter
@Setter
@XmlType(name = "statistics")
@XmlRootElement
public class Statistics {
    // количество всего файлов
    private Integer count               = 0;
    // размер файлов
    private Long    size                = 0l;
    // количество строк всего
    private Long    countAllLine        = 0l;
    // количество непустых строк
    private Long    countNonEmptyLine   = 0l;
    // количество строк с комментариями
    private Long    countCommentLine    = 0l;

    public Statistics() {
    }
}
