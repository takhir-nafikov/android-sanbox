package com.takhir.rxbasic;

import org.junit.Test;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CompletableTest {

  @Test
  public void completableFromCallableTest() throws InterruptedException {
    Completable completable = Completable.fromCallable(new Callable<Object>() {
      @Override
      public Object call() throws Exception {
        return null;
      }
    });

    completable.subscribe(new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onComplete() {
        System.out.println("CompletableObserver::onComplete");
      }

      @Override
      public void onError(Throwable e) {

      }
    });
  }

  @Test
  public void completableFromObservable() throws InterruptedException {
    Completable completable = Completable.fromObservable(new ObservableSource<Object>() {
      @Override
      public void subscribe(Observer<? super Object> observer) {
        //observer.onNext(5);
        observer.onError(new IllegalArgumentException());
        observer.onComplete();
      }
    });

    completable.subscribe(new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        System.out.println("CompletableObserver::onSubscribe");
      }

      @Override
      public void onComplete() {
        System.out.println("CompletableObserver::onComplete");
      }

      @Override
      public void onError(Throwable e) {
        System.out.println("CompletableObserver::onError");
        e.printStackTrace();
      }
    });
  }
}
