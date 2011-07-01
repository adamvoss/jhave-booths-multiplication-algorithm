#!/bin/bash

INGEN='../../../../html_root/ingen'

if [ $1 == "--revert" ]
    then
        rm "$INGEN/boothsMultiplication.igs"
        cp ./boothsMultiplication.igs.bak $INGEN/boothsMultiplication.igs
        rm ./boothsMultiplication.igs
        cp ./boothsMultiplication.igs.bak ./boothsMultiplication.igs
        rm ./BoothsMultiplication.java
        rm ./boothsMultiplication.java
        cp ./BoothsMultiplication.java.bak ./BoothsMultiplication.java
        cp ./boothsMultiplication.java.bak ./boothsMultiplication.java

elif [ $1 == "--new" ]
    then
        rm "$INGEN/boothsMultiplication.igs"
        cp ./boothsMultiplication.igs.new $INGEN/boothsMultiplication.igs
        rm ./boothsMultiplication.igs
        cp ./boothsMultiplication.igs.new ./boothsMultiplication.igs
        rm ./BoothsMultiplication.java
        rm ./boothsMultiplication.java
        cp ./BoothsMultiplication.java.new ./BoothsMultiplication.java
        cp ./boothsMultiplication.java.new ./boothsMultiplication.java


        if [ ! -e "$INGEN/IGListeningTextField.java" ]
            then
                cp ./IGListeningTextField.java.temp "$INGEN/IGListeningField.java"
        fi
else
    echo "usage: $0 [--revert | --new]"
fi
