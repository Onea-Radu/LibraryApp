package com.mypackage.classes;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Provider<T extends Comparable> {
    private static final HashMap<Class, CSVService> instances = new HashMap<>();

    public CSVService<T> getInstance(Class c) {
        if (!instances.containsKey(c))
            instances.put(c, new CSVService(c));
        return instances.get(c);
    }

    static class CSVService<T extends Comparable> {

        private Class clas;

        public void Update(Set<T> set) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
            var writer = new CSVWriter(new FileWriter(clas.getSimpleName()));
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clas);
            var beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            beanToCsv.write(set.iterator());
            writer.close();
        }

        public Set<T> Read() throws IOException, CsvValidationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            var reader = new CSVReader(new FileReader(clas.getSimpleName()));
            var set = new TreeSet<T>();
            var i = reader.iterator();
            i.next();
            while (i.hasNext()) {
                var s = i.next();
                if (s.length == 1)
                    set.add((T) clas.getConstructor(new Class[]{String.class}).newInstance(s[0]));
                if (s.length == 2) {
                    var h = clas.getConstructor(new Class[]{String.class, String.class});
                    set.add((T) h.newInstance(s[0], s[1]));
                }
                if (s.length == 3)

                    set.add((T) clas.getConstructor(new Class[]{String.class, String.class, String.class})
                            .newInstance(s[0], s[1], s[2]));
            }
            return set;

        }


        private CSVService(Class c) {
            clas = c;
        }
    }

    static class Audit {
        public static void Write(String action) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

            var writer = new CSVWriter(new FileWriter("Audit", true));
            writer.writeNext(new String[]{Thread.currentThread().getName(), LocalDateTime.now().toString(), action});
            writer.close();
        }

    }
}
