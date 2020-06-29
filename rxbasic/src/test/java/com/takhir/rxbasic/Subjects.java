package com.takhir.rxbasic;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import io.reactivex.subjects.UnicastSubject;

public class Subjects {


  @Test
  public void publishSubjectdDemo() {
    Subject<String> subject = PublishSubject.create();

    subject.map(String::length)
        .subscribe(System.out::println);

    subject.onNext("Alpha");
    subject.onNext("Beta");
    subject.onNext("Gamma");
    subject.onComplete();
  }

  @Test
  public void behaviorSubjectdDemo() {
    Subject<String> subject = BehaviorSubject.create();

    subject.subscribe(s -> System.out.println("Observer 1: " + s));

    subject.onNext("Alpha");
    subject.onNext("Beta");
    subject.onNext("Gamma");

    subject.subscribe(s -> System.out.println("Observer 2: " + s));
  }

  @Test
  public void replaySubjectdDemo() {
    Subject<String> subject = ReplaySubject.create();

    subject.subscribe(s -> System.out.println("Observer 1: " + s));

    subject.onNext("Alpha");
    subject.onNext("Beta");
    subject.onNext("Gamma");
    subject.onComplete();

    // Здесь подписываемся еще раз
    // и получаем все переданные события
    subject.subscribe(s -> System.out.println("Observer 2: " + s));
  }


  @Test
  public void asyncSubjectdDemo() {
    Subject<String> subject = AsyncSubject.create();
    subject.subscribe(
        s -> System.out.println("Observer 1: " + s),
        Throwable::printStackTrace,
        () -> System.out.println("Observer 1 done!")
    );

    subject.onNext("Alpha");
    subject.onNext("Beta");
    subject.onNext("Gamma");
    subject.onComplete();

    subject.subscribe(
        s -> System.out.println("Observer 2: " + s),
        Throwable::printStackTrace,
        () -> System.out.println("Observer 2 done!")
    );
  }


  @Test
  public void unicastcSubjectdDemo() {
    Subject<String> subject = UnicastSubject.create();

    Observable.interval(300, TimeUnit.MILLISECONDS)
        .map(l -> ((l + 1) * 300) + " milliseconds")
        .subscribe(subject);

    sleep(2000);

    subject.subscribe(s -> System.out.println("Observer 1: " + s));
    sleep(2000);
  }

  @Test
  public void noBackpressureDemo0() {
    Observable.range(1, 10000)
        .map(MyItem::new)
        .subscribe(myItem -> {
          sleep(50);
          System.out.println("Received MyItem " + myItem.id + " " + Thread.currentThread().getName());
        });
  }

  @Test
  public void backpressureDemo() {
    Observable.range(1, 100)
        .map(MyItem::new)
        .observeOn(Schedulers.io())
        .subscribe(myItem -> {
          sleep(1);
          System.out.println("Received MyItem " + myItem.id + " " + Thread.currentThread().getName());
        });

    sleep(10000);
  }

  // Теперь элементы будут обрабатываться по очереди

  @Test
  public void backpressureFlowabledDemo() {
    Flowable.range(1, 10000)
        .map(MyItem::new)
        .observeOn(Schedulers.io())
        .subscribe(myItem -> {
          sleep(50);
          System.out.println("Received MyItem " + myItem.id + " " + Thread.currentThread().getName());
        });
    
    sleep(10000);
  }

  @Test
  public void backpressureFlowableBufferdDemo0() {
    Flowable<Integer> source = Flowable.create(emitter -> {
      for (int i=0; i<=1000; i++) {
        if (emitter.isCancelled())
          return;
        emitter.onNext(i);
      }
      emitter.onComplete();
    }, BackpressureStrategy.BUFFER);
    source.observeOn(Schedulers.io())
        .subscribe(System.out::println);

    sleep(1000);

  }

  @Test
  public void backpressureFlowableonBackpressureBufferdDemo() {

    Flowable.interval(1, TimeUnit.MILLISECONDS)
        .onBackpressureBuffer()
        .observeOn(Schedulers.io())
        .subscribe(i -> {
          sleep(5);
          System.out.println(i);
        });
    sleep(5000);
  }


  @Test
  public void backpressureFlowableonBackpressureBufferdDemo_DROP_LATEST() {

    Flowable.interval(1, TimeUnit.MILLISECONDS)
        .onBackpressureBuffer(10,
            () -> System.out.println("overflow!"),
            BackpressureOverflowStrategy.DROP_LATEST)
        .observeOn(Schedulers.io())
        .subscribe(i -> {
          sleep(5);
          System.out.println(i);
        });
    sleep(5000);
  }




  @Test
  public void backpressureFlowableBufferdDemo() {
    Observable<Integer> source = Observable.range(1,1000);
    source.toFlowable(BackpressureStrategy.BUFFER)
        .observeOn(Schedulers.io())
        .subscribe(System.out::println);


    sleep(10000);
  }

  static final class MyItem {

    final int id;

    MyItem(int id) {
      this.id = id;
      System.out.println("Constructing MyItem " + id + " "+ Thread.currentThread().getName());
    }
  }


  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
