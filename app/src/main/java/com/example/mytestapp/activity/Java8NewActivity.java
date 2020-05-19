package com.example.mytestapp.activity;

import android.util.Log;

import com.example.mytestapp.entity.BaseItemEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Java8NewActivity extends BaseListActivity {


    @Override
    public void initData(List<BaseItemEntity> datas) {
        datas.add(new BaseItemEntity("onClick0", "0"));
        datas.add(new BaseItemEntity("onClick1", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                onClick0();
                break;

            case 1:
                click1();
                break;

            case 2:
                click2();
                break;

            case 3:
                click3();
                break;

            case 4:
                break;

            case 5:
                break;
        }
    }

    private void click3() {
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        Function<String, String> backToString1 = String::valueOf;

        backToString.apply("123");
        Integer apply = toInteger.apply("3423");
        backToString.apply("123");

        Optional<String> bam = Optional.of("bam");
        bam.ifPresent(s -> Log.d(TAG,"s = " + s));

        List<Integer> integers = Arrays.asList(1, 2, 3);
        integers.stream().filter((a) -> a > 10).forEach(System.out::println);


    }

    private void onClick0() {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a);
            }
        };

        formula.calculate(100);
        formula.sqrt(2);
        formula.add(10, 11);
    }

    private void click1() {
        List<String> strings = Arrays.asList("one", "two", "three");

        Collections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        Collections.sort(strings, (a, b) -> {
            return a.compareTo(b);
        });

        Collections.sort(strings, String::compareTo);
    }

    private void click2() {
        Converter<String, Integer> converter = Integer::valueOf;
        int num = 1;
        Converter<String, Integer> converter1 = (form) -> Integer.valueOf(form + num);
    }

    @FunctionalInterface
    interface Converter<F, T> {
        T convert(F from);
    }

    interface Formula {
        double calculate(int a);
        default double sqrt(int a) {
            return Math.sqrt(a);
        }

        default int add(int a, int b) {
            return a + b;
        }
    }
}
