package com.example.a108820027_final;

public class Card {
    final int id;
    final int imageResource;
    boolean isFaceUp;
    boolean isMatched;

    Card(int i, int imageRes){
        id = i;
        imageResource = imageRes;
        isMatched = false;
        isFaceUp = false;
    }

    int getSource(){
        return imageResource;
    }

}

