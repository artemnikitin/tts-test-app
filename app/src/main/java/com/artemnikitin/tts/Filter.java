package com.artemnikitin.tts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

class Filter {

    private final Locale[] locales;

    public Filter(Locale[] locales) {
       this.locales = locales;
    }

    public Locale[] getListOfLocales() {
        return sort(filter(locales));
    }

    private Locale[] filter(Locale[] locales) {
        List<Locale> result = new ArrayList<>();
        for (Locale locale : locales) {
            if (locale.toString().contains("_") && locale.toString().length() == 5) {
                result.add(locale);
            }
        }
        Locale[] array = new Locale[result.size()];
        return result.toArray(array);
    }

    private Locale[] sort(Locale[] locales) {
        Arrays.sort(locales, comparator);
        return locales;
    }

    private Comparator<Locale> comparator = new Comparator<Locale>() {
        @Override
        public int compare(Locale lhs, Locale rhs) {
            return lhs.toString().compareToIgnoreCase(rhs.toString());
        }
    };

}
