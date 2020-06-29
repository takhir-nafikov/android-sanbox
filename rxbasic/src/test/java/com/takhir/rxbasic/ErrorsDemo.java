package com.takhir.rxbasic;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ErrorsDemo {

  @Test
  public void exceptionDemo() {

    Observable.just(5, 2, 4, 0, 3, 2, 8)
        .map(i -> 10 / i)
        .subscribe(i -> System.out.println("RECEIVED: " + i),
            e -> System.out.println("RECEIVED ERROR: " + e)
        );
  }

  @Test
  public void onErrorReturnItemDemo() {

    Observable.just(5, 2, 4, 0, 3, 2, 8)
        .map(i -> 10 / i)
        .onErrorReturnItem(1000)
        .subscribe(
            i -> System.out.println("RECEIVED: " + i),
            e -> System.out.println("RECEIVED ERROR: " + e)
        );
  }

  @Test
  public void onErrorReturnDemo() {

    Observable.just(5, 2, 4, 0, 3, 2, 8)
        .map(i -> 10 / i)
        .onErrorReturn(e ->
        {
          if (e.getMessage().contains("/ by zero")) {
            return -6;
          } else {
            return -2;
          }
        })
        .subscribe(
            i -> System.out.println("RECEIVED: " + i),
            e -> System.out.println("RECEIVED ERROR: " + e.getMessage())
        );
  }

  @Test
  public void onErrorResumeNextDemo() {

    Observable.just(5, 2, 1, 0, 3, 2, 8)
        .map(i -> 10 / i)
        .onErrorResumeNext(Observable.just(-1, -2, -3, -4, -5))
        .subscribe(
            i -> System.out.println("RECEIVED: " + i),
            e -> System.out.println("RECEIVED ERROR: " + e.getMessage())
        );
  }

  @Test
  public void retryDemo() {

    Observable.just(5, 2, 4, 0, 3, 2, 8)
        .map(i -> 10 / i)
        .retry(10)
        .subscribe(i -> System.out.println("RECEIVED: " + i),
            e -> System.out.println("RECEIVED ERROR: " + e)
        );
  }

  @Test
  public void repeatDemo() {

    Observable.just(5, 2, 4, 0, 3, 2, 8)

        .repeat(2)
        .subscribe(new Observer<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {
            System.out.println("onSubscribe");
          }

          @Override
          public void onNext(Integer integer) {
            System.out.println("onNext: " + integer);
          }

          @Override
          public void onError(Throwable e) {
            System.out.println("RECEIVED ERROR: ");
          }

          @Override
          public void onComplete() {
            System.out.println("onComplete()");
          }
        });
  }
}
