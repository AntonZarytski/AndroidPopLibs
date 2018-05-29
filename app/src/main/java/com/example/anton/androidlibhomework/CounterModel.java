package com.example.anton.androidlibhomework;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class CounterModel {
    private List<Integer> counters;

    public CounterModel() {
        counters = new ArrayList<>();
        counters.add(0);
        counters.add(0);
        counters.add(0);
    }

    public int getAt(int index) {
        return counters.get(index);
    }


    public int setAt(int index, int value) {
        return counters.set(index, value);
    }

    public Observable<Integer> getObservable(int index) {
        Observable<Integer> observable = Observable.just(counters.get(index));
        return observable;
    }
}
