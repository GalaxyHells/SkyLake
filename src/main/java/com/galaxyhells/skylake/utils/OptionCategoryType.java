package com.galaxyhells.skylake.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionCategoryType {
    GENERAL("Geral"),
    GRAPHICAL("Gráficos"),
    KEYBIND("Controle");

    private final String display;
}