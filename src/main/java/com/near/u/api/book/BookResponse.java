package com.near.u.api.book;

public record BookResponse (
        Long id,
        String title,
        String author,
        boolean borrowed
){ }
