package com.example;

import com.example.image.TextGraphicsConverter;
import com.example.image.TextGraphicsConverterImage;
import com.example.server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new TextGraphicsConverterImage();

        GServer server = new GServer(converter);
        server.start();

    }
}
