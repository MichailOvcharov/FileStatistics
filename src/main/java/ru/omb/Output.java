package ru.omb;

import lombok.Getter;

/*
*  Перечисление формат ввода статистики
 */
@Getter
public enum Output {
    plain,
    xml,
    json
}
