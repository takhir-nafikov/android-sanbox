package com.takhir.rxbasic;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class HotAndColdObservableTest {

  // Тестируем Cold observables
  @Test
  public void observableTest() throws InterruptedException {
    final Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        System.out.println("Observable::subscribe: " + Thread.currentThread().getName());
        emitter.onNext("First");
        emitter.onNext("Second");
      }
    }).subscribeOn(Schedulers.computation());

    observable
//        .doOnNext(new Consumer<String>() {
//          @Override
//          public void accept(String s) throws Exception {
//            System.out.println("doOnNext: " + Thread.currentThread().getName());
//          }
//        })
        .observeOn(Schedulers.newThread())
        .subscribe(new Observer<String>() {
          Disposable d;

          @Override
          public void onSubscribe(Disposable d) {
            this.d = d;
            System.out.println("Observer::onSubscribe: " + Thread.currentThread().getName());
          }

          @Override
          public void onNext(String s) {
            System.out.println("Observer::onNext: " + Thread.currentThread().getName());
            System.out.println("onNext: " + s);
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });

    Thread.sleep(3000);
  }

  // Тестируем ковертирование Cold в Hot observable с помощью ConnectableObservable
  @Test
  public void coldToHotObservableTestWithConnectableObservable() throws InterruptedException {
    final Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        System.out.println("Observable::subscribe: " + Thread.currentThread().getName());
        emitter.onNext("First");
        emitter.onNext("Second");
      }
    }).subscribeOn(Schedulers.computation());

    ConnectableObservable<String> connectableObservable = observable.publish();
    connectableObservable.connect();
    connectableObservable
        .observeOn(Schedulers.newThread())
        .subscribe(new Observer<String>() {

          Disposable d;

          @Override
          public void onSubscribe(Disposable d) {
            this.d = d;
            System.out.println("Observer::onSubscribe: " + Thread.currentThread().getName());
          }

          @Override
          public void onNext(String s) {
            System.out.println("Observer::onNext: " + Thread.currentThread().getName());
            System.out.println("onNext: " + s);
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });

//    connectableObservable.connect();
    Thread.sleep(3000);
  }

  // Тестируем ковертирование Cold в Hot observable с помощью Subject.
  @Test
  public void coldToHotObservableTestWithSubject() throws InterruptedException {
    final Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        System.out.println("Observable::subscribe: " + Thread.currentThread().getName());
        emitter.onNext("First");
        emitter.onNext("Second");
      }
    }).subscribeOn(Schedulers.computation());

    PublishSubject<String> publishSubject = PublishSubject.create();
    observable.subscribe(publishSubject);

    Thread.sleep(3000);
  }

  @Test
  public void subjectTest() throws InterruptedException {
    final PublishSubject<String> publishSubject = PublishSubject.create();
    publishSubject.onNext("First");
    publishSubject
        .doOnNext(new Consumer<String>() {
          @Override
          public void accept(String s) throws Exception {
            System.out.println("doOnNext: " + Thread.currentThread().getName());
          }
        })
        .observeOn(Schedulers.computation())
        .subscribe(new Observer<String>() {

          Disposable d;

          @Override
          public void onSubscribe(Disposable d) {
            this.d = d;
            System.out.println("Observer::onSubscribe: " + Thread.currentThread().getName());
          }

          @Override
          public void onNext(String s) {
            System.out.println("Observer::onNext: " + Thread.currentThread().getName());
            System.out.println("onNext: " + s);
            //this.d.dispose();
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });
    publishSubject.onNext("Second");
    publishSubject.onNext("Third");

    Thread.sleep(3000);
  }
}
