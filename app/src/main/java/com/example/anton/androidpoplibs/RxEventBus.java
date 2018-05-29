package com.example.anton.androidpoplibs;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {
    private PublishSubject<Object> bus = PublishSubject.create();

    public RxEventBus() {
    }

    ;

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> getObservable() {
        return bus;
    }
}
