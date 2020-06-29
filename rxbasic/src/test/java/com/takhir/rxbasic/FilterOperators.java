package com.takhir.rxbasic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function9;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

public class FilterOperators {

  String result = "";

  ArrayList<String> cities = new ArrayList<String>(
      Arrays.asList("Москва", "Лондон", "Прага", "Новосибирск", "Минск", "Осло"));

  ArrayList<Integer> ages = new ArrayList<Integer>(
      Arrays.asList(12, 22, 54, 27, 33, 31, 38, 39));

  ArrayList<String> names = new ArrayList<String>(
      Arrays.asList("Никита", "Михаил", "Андрей", "Василий", "Давид", "Леонид"));

  ArrayList<String> surnames = new ArrayList<String>(
      Arrays.asList("Пушкин", "Калашников", "Лермонтов", "Маяковский"));

  @Before
  public void init() {
    result = "";
  }

  @Test
  public void mapTest() throws InterruptedException {

    Observable<Integer> mObservable = Observable.fromArray(1, 2, 3, 4);

    mObservable.map(new Function<Integer, Integer>() {
      @Override
      public Integer apply(Integer integer) throws Exception {
        return 10 * integer;
      }
    }).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer i) throws Exception {
        result += i + " ";
      }
    });

    assertEquals("10 20 30 40 ", result);
  }

  @Test
  public void filterTest() {

    Observable<Integer> mObservable = Observable.fromArray(2, 30, 22, 5, 60, 1);

    mObservable.filter(new Predicate<Integer>() {
      @Override
      public boolean test(Integer s) {
        return s > 10;
      }
    }).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer s) {
        result += s + " ";
      }
    });

    assertEquals("30 22 60 ", result);
  }

  @Test
  public void takeTest() {

    Observable<Integer> mObservable = Observable.fromArray(1, 2, 3, 4);

    mObservable.map(new Function<Integer, String>() {
      @Override
      public String apply(Integer integer) {
        return integer.toString();
      }
    })
        .take(2)
        .subscribe(new Consumer<String>() {
          @Override
          public void accept(String string) throws Exception {
            result += addItem(string);
          }
        });

    assertEquals("1 2 ", result);
  }

  @Test
  public void takeTestLambda() {

    Observable<Integer> mObservable = Observable.fromArray(1, 2, 3, 4);

    mObservable
        .map(integer -> integer.toString())
        .take(2)
        .subscribe(string -> result += addItem(string));

    assertEquals("1 2 ", result);
  }

  @Test
  public void skipTest() {

    Observable<Integer> mObservable = Observable.fromArray(1, 2, 3, 4);

    mObservable
        .map(integer -> integer.toString())
        .skip(2)
        .subscribe(string -> result += addItem(string));

    assertEquals("3 4 ", result);
  }

  String addItem(String item) {
    return item + " ";
  }

  @Test
  public void mergeTest() {

    Observable<Integer> source1 = Observable.fromArray(1, 2, 3, 4);
    Observable<Integer> source2 = Observable.fromArray(10, 20, 30, 40);

    Observable.merge(source1, source2)
        .subscribe(item -> result += addItem(item.toString()));

    assertEquals("1 2 3 4 10 20 30 40 ", result);
  }

  @Test
  public void concatTest() {

    Observable<Integer> source1 = Observable.fromArray(1, 2, 3, 4);
    Observable<Integer> source2 = Observable.fromArray(10, 20, 30, 40);

    Observable.merge(source1, source2)
        .subscribe(item -> result += addItem(item.toString()));

    assertEquals("1 2 3 4 10 20 30 40 ", result);
  }

  @Test
  public void concatDemoTest() {

    //emit every second, but only take 2 emissions
    Observable<String> source1 =
        Observable.interval(1, TimeUnit.SECONDS)
            .take(2)
            .map(l -> l + 1) // emit elapsed seconds
            .map(l -> "Source1: " + l + " seconds");

    //emit every 300 milliseconds
    Observable<String> source2 =
        Observable.interval(300, TimeUnit.MILLISECONDS)
            .map(l -> (l + 1) * 300) // emit elapsed milliseconds
            .map(l -> "Source2: " + l + " milliseconds");

    Observable.concat(source1, source2)
        .subscribe(i -> System.out.println("RECEIVED: " + i));

    //keep application alive for 5 seconds
    sleep(5000);

  }


  @Test
  public void mergeDemoTest() {

    //emit every second, but only take 2 emissions
    Observable<String> source1 =
        Observable.interval(1, TimeUnit.SECONDS)
            .take(2)
            .map(l -> l + 1) // emit elapsed seconds
            .map(l -> "Source1: " + l + " seconds");

    //emit every 300 milliseconds
    Observable<String> source2 =
        Observable.interval(300, TimeUnit.MILLISECONDS)
            .map(l -> (l + 1) * 300) // emit elapsed milliseconds
            .map(l -> "Source2: " + l + " milliseconds");

    Observable.merge(source1, source2)
        .subscribe(i -> System.out.println("RECEIVED: " + i));

    //keep application alive for 5 seconds
    sleep(5000);

  }

  @Test
  public void rangeDemo() {
    Observable<Integer> source2 = Observable.range(1, 10);

    source2.map(item -> item.toString())
        .subscribe(System.out::println);
  }

  @Test
  public void zipDemoTest() {

    Observable<String> source1 =
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

    Observable<Integer> source2 = Observable.range(1, 10);

    Observable.zip(source1, source2, new BiFunction<String, Integer, Object>() {
      @Override
      public Object apply(String s, Integer i) throws Exception {
        return s + "-" + i;
      }
    }).subscribe(System.out::println);
  }

  @Test
  public void zipLambdaDemoTest() {

    Observable<String> source1 =
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

    Observable<Integer> source2 = Observable.range(0, 6);

    Observable.zip(source1, source2, (s, i) -> s + "-" + i)
        .subscribe(System.out::println);
  }

  @Test
  public void zip3DemoTest() {

    Observable<String> source1 =
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

    Observable<Integer> source2 = Observable.range(0, 6);

    Observable.zip(source1, source2, source2, new Function3<String, Integer, Integer, Object>() {
      @Override
      public Object apply(String s, Integer i2, Integer i3) {
        return s + i2 + i3;
      }
    }).subscribe(System.out::println);

//        Observable.zip(source1, source2, (s,i) -> s + "-" + i)
//                .subscribe(System.out::println);
  }


  @Test
  public void distinctDemo() {

    Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
        .map(String::length)
        .distinct()
        .subscribe(i -> System.out.println("RECEIVED: " + i));
  }

  @Test
  public void distinctUntilChangedDemo() {

    Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
        .map(String::length)
        .distinctUntilChanged()
        .subscribe(i -> System.out.println("RECEIVED: " + i));
  }

  @Test
  public void operatorsTest() {

    Observable.fromIterable(createUsers())
        // Отфильровать только тех кто живет в Москве
        .filter(user -> user.getCity().equals("Москва"))
        // Отфильровать только тех кто старше 21
        .filter(user -> user.getAge() > 21)
        // Исключить однофамильцев
        .distinct(user -> user.getSecondName())
        // Вывести полное имя
        .map(user ->
            user.getFirstName() + " " + user.getSecondName())
        .subscribe(i -> System.out.println(i));
  }

  public ArrayList<User> createUsers() {
    ArrayList<User> users = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      users.add(new User(ages.get(rnd(ages.size())), names.get(rnd(names.size())), surnames.get(rnd(surnames.size())),
          cities.get(rnd(cities.size()))));

    }

    return users;

  }


  public int rnd(int size) {
    int rnd = new Random().nextInt(size);
    return rnd;
  }


  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void filterTest_divide2() throws InterruptedException {

    Observable<Integer> mObservable = Observable.fromArray(2, 30, 22, 5, 60, 1);

    mObservable.filter(new Predicate<Integer>() {
      @Override
      public boolean test(Integer s) {
        return s % 2 == 0;
      }
    }).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer s) throws Exception {
        result += s;
      }
    });

    assertEquals("246", result);

  }
}
