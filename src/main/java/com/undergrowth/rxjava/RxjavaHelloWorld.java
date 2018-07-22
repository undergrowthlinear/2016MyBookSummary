package com.undergrowth.rxjava;

/**
 * To use RxJava you create Observables (which emit data items),
 * transform those Observables in various ways to get the precise data items that interest you
 * (by using Observable operators), and then observe and react to these sequences of interesting items
 * (by implementing Observers or Subscribers and then subscribing them to the resulting transformed Observables).
 */

import com.google.common.collect.Lists;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class RxjavaHelloWorld {

    public static void main(String[] args) {
        System.out.println("主线程---->" + Thread.currentThread().getName());
        // create Observable
        createObservable();
        // helloStr
        // helloStr();
        // parallel
        // parallel();
        // concurreny
        // concurreny();
        // 主题/观察者 推/拉
        // backgroundComputa();
        // backgroundComputa2();
        // hello
        // helloFlowable();
        // Assembly time
        // assemblyFlowable();
        //
        // runtime();
    }

    private static void createObservable() {
        Observable<String> o = Observable.just("Hello World");
        //Observable.create()
    }

    private static void helloStr() {
        List names = Lists.newArrayList();
        names.add("Ben");
        names.add("George");
        Observable.fromArray(names.toArray()).subscribe(new ConsumerImpl());
    }

    private static void parallel() {
        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(v -> v * v)
                .sequential()
                .blockingSubscribe(new ConsumerImpl());

    }

    private static void concurreny() {
        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> v * v)
                .subscribe(new ConsumerImpl());
        //.blockingSubscribe(new ConsumerImpl());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void backgroundComputa2() {
        Flowable<String> source = Flowable.fromCallable(() -> {
            Thread.sleep(1000); //  imitate expensive computation
            return "Done";
        });

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

        showForeground.subscribe(new ConsumerImpl(), new ConsumerImpl());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void backgroundComputa() {
        Flowable.fromCallable(() -> {
            Thread.sleep(1000); //  imitate expensive computation
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new ConsumerImpl(), new ConsumerImpl());

        try {
            Thread.sleep(2000); // <--- wait for the flow to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void runtime() {
        Observable.create(emitter -> {
            while (!emitter.isDisposed()) {
                long time = System.currentTimeMillis();
                emitter.onNext(time);
                if (time % 2 != 0) {
                    emitter.onError(new IllegalStateException("Odd millisecond!"));
                    break;
                }
            }
        })
                .subscribe(new ConsumerImpl(), new ConsumerImpl());

    }

    private static void assemblyFlowable() {
        Flowable<Integer> flow = Flowable.range(1, 5)
                .map(v -> v * v)
                .filter(v -> v % 3 == 0);
        // Subscription time
        flow.subscribe(new ConsumerImpl());
    }

    private static void helloFlowable() {
        Flowable.just("Hello world").subscribe(new ConsumerImpl());
    }

    static class ConsumerImpl implements Consumer {

        @Override
        public void accept(Object o) throws Exception {
            System.out.println("线程---->" + Thread.currentThread().getName() + "\t" + o);
        }
    }

}
