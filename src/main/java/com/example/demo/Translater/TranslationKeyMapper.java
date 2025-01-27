package com.example.demo.Translater;

import java.util.HashMap;

class TranslationKeyMapper {
    // mapping for Pokemon API
    private static HashMap<String, String> keyMap = new HashMap<String, String>();
    static {
        keyMap.put("en", "en");
        keyMap.put("de", "de");
        keyMap.put("ko", "ko");
        keyMap.put("fr", "fr");
        keyMap.put("es", "es");
        keyMap.put("it", "it");
        keyMap.put("zh", "zh-hant");
        keyMap.put("zh-hant", "zh-hant");
        keyMap.put("zh-TW", "zh-hant");
        keyMap.put("ja", "ja");
    }

    public static String getKey(String languageParam) {
        return keyMap.get(languageParam);
    }
}