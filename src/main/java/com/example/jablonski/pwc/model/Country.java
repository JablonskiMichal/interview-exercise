package com.example.jablonski.pwc.model;

import java.util.List;

public record Country(String cca3, List<String> borders, Name name) {
}


