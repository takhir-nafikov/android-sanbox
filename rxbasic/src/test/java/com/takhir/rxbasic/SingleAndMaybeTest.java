package com.takhir.rxbasic;

import org.junit.Test;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SingleAndMaybeTest {

  // Тестируем Single::fromCallable
  @Test
  public void singleFromCallableTest() throws InterruptedException {

    Single<Integer> single = Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        System.out.println("Callable::call: " + Thread.currentThread().getName());
        return 5;
      }
    }).subscribeOn(Schedulers.computation());

    single
        //.subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.newThread())
        .doOnSuccess(new Consumer<Integer>() {
          @Override
          public void accept(Integer integer) throws Exception {
            System.out.println("SingleObserver::doOnNext: " + Thread.currentThread().getName());
          }
        })
        .subscribe(new SingleObserver<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {
            System.out.println("SingleObserver::onSubscribe");
          }

          @Override
          public void onSuccess(Integer integer) {
            System.out.println("SingleObserver::onSuccess: " + Thread.currentThread().getName());
            System.out.println("Received value: " + integer.toString());
          }

          @Override
          public void onError(Throwable e) {

          }
        });

    Thread.sleep(3000);

  }

  // Тестируем Single::just
  @Test
  public void singleFromJustTest() throws InterruptedException {

    Single<Integer> single = Single.just(12);

    Disposable d = single.subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) throws Exception {
        System.out.println("Received value: " + integer.toString());
      }
    });

    Thread.sleep(3000);
  }

  // Тестируем Single::create
  @Test
  public void singleCreateTest() throws InterruptedException {

    Single<Integer> single = Single.create(new SingleOnSubscribe<Integer>() {
      @Override
      public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
        emitter.onError(new IllegalArgumentException());
      }
    });

    Disposable d = single.subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) throws Exception {
        System.out.println("Received value: " + integer.toString());
      }
    }, new Consumer<Throwable>() {
      @Override
      public void accept(Throwable throwable) throws Exception {
        System.out.println("Caught exception: ");
        throwable.printStackTrace();
      }
    });

    Thread.sleep(3000);
  }

  // Тестируем Maybe::fromCallable
  @Test
  public void maybeFromCallableTest() throws InterruptedException {
    Maybe<Integer> maybe = Maybe.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        return null;
      }
    });

    maybe.subscribe(new MaybeObserver<Integer>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onSuccess(Integer integer) {
        System.out.println("Received value: " + integer);
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {
        System.out.println("onComplete");
      }
    });
  }

  // Тестируем Maybe.empty()
  @Test
  public void maybeEmptyTest() throws InterruptedException {
    Maybe<Integer> mMaybe = Maybe.empty();

    mMaybe.subscribe(new MaybeObserver<Integer>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onSuccess(Integer integer) {
        System.out.println("Received value: " + integer);
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {
        System.out.println("onComplete");
      }
    });
  }
}