package com.example.image;

public class TextColorSchemaImage implements TextColorSchema {
    private char[] symbols;

    public TextColorSchemaImage(char[] symbols) {
        this.symbols = symbols;
    }

    @Override
    public char convert(int color) {
        int numberOfParts = 256 / symbols.length;
        int numSymbol = color / numberOfParts;
        return symbols[numSymbol];
    }
}

