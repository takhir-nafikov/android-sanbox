package com.takhir.rxbasic;

public class User {
  private Integer age;
  private String firstName;
  private String secondName;
  private String city;

  public User(Integer age, String firstName, String secondName, String city) {
    this.age = age;
    this.firstName = firstName;
    this.secondName = secondName;
    this.city = city;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String toString() {
    return "User{" +
        "age=" + age +
        ", firstName='" + firstName + '\'' +
        ", secondName='" + secondName + '\'' +
        ", city='" + city + '\'' +
        '}';
  }
}
