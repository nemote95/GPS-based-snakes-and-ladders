package com.example.negar.snakesladders.model;

public class Type {
    String movementType="standard";
    String movementStrategy = "standard";
    Board board;

    public Type(Board board){
        this.board=board;
        this.movementStrategy=movementStrategy;
        this.movementType=movementType;
    }

    public Type(String movementType, String movementStrategy,Board board){
        this.board=board;
        this.movementStrategy=movementStrategy;
        this.movementType=movementType;
    }

    void updateType(String movementType, String movementStrategy,Board board){
        this.board=board;
        this.movementStrategy=movementStrategy;
        this.movementType=movementType;
    }

}

