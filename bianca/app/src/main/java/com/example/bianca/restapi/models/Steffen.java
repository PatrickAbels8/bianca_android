package com.example.bianca.restapi.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Steffen {
    private int id;
    private String name;
    private int age;
    private float whiskeyFill;
}
