package com.artemnikitin.tts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class Filter {

    private final Locale[] locales;

    private final Type type;

    public Filter(Locale[] locales, Type type) {
        this.locales = locales;
        this.type = type;
    }

    public List<String> getListOfLocales() {
        List<String> res = processLocales(filter(locales, type));
        Collections.sort(res);
        return res;
    }

    private Locale[] filter(Locale[] locales, Type type) {
        if (locales == null) {
            return new Locale[0];
        }
        List<Locale> result = new ArrayList<>();
        for (Locale locale : locales) {
            if (type.equals(Type.SAMSUNG)) {
                if (locale != null && locale.toString().contains("_")) {
                    result.add(locale);
                }
            }
            if (locale != null && locale.toString().contains("_")) {
                result.add(locale);
            }
        }
        Locale[] array = new Locale[result.size()];
        return result.toArray(array);
    }

    private List<String> processLocales(Locale[] locales) {
        List<String> result = new ArrayList<>(locales.length);
        for (Locale loc : locales) {
            result.add(loc.getDisplayName() + " (" + loc.toString() + ")");
        }
        return result;
    }

    public enum Type {
        SAMSUNG,
        GENERIC
    }

}
