package com.notessensei.serializer.bustest;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SampleCustomClass implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private Integer age;
  private String shape;
  private final Map<String, String> allEntries = new HashMap<>();

  private transient Date currentDate = new Date();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.allEntries.put("name", name);
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
    this.allEntries.put("age", String.valueOf(age));
  }

  public String getShape() {
    return shape;
  }

  public void setShape(String shape) {
    this.shape = shape;
    this.allEntries.put("shape", shape);
  }

  public Date getCurrentDate() {
    return currentDate;
  }

  public void setCurrentDate(Date currentDate) {
    this.currentDate = currentDate;
    this.allEntries.put("date", currentDate.toInstant().toString());
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Map<String, String> getAllEntries() {
    return allEntries;
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();

    b.append("serialVersionUID: ");
    b.append(serialVersionUID);
    b.append("\nname: ");
    b.append(name);
    b.append("\nage: ");
    b.append(age);
    b.append("\nshape: ");
    b.append(shape);
    b.append("\nallEntries: ");
    allEntries.forEach((k, v) -> {
      b.append("\n   ");
      b.append(k);
      b.append(": ");
      b.append(v);
    });
    b.append("\nDate: ");
    b.append(currentDate.toInstant());
    return b.toString();
  }



}
