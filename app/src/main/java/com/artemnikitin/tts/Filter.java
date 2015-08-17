package com.artemnikitin.tts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Filter {

    private Locale[] locales;

    public Filter(Locale[] locales) {
       this.locales = locales;
    }

    public Locale[] getListOfLocales() {
        return filter(locales);
    }

    private Locale[] filter(Locale[] locales) {
        List<Locale> result = new ArrayList<>();
        for (int i = 0; i < locales.length; i++) {
            if (locales[i].toString().contains("_") && locales[i].toString().length() == 5) {
                result.add(locales[i]);
            }
        }
        Locale[] array = new Locale[result.size()];
        return result.toArray(array);
    }

}
