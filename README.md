# Indexer

@author Lucas

@version Java 24

## Description

This is a console-based application that encodes/decodes text files using an encoding matrix as a base.

The default encoding file is expected to be in the root folder to the project, named encodings-10000.csv. The default output file is `out.txt`, also in the root folder. Both can be changed by selecting the right option in the menu.

## Limitations

Any word not found in the given csv file with the ciphers will be encoded to [???].

## Features

1. Specify the mapping file;
2. Specify the input file;
3. Specify the output file;
4. Swap input/output files;
5. Print path to files;
6. Encode file;
7. Decode file;


## Design

The project was broken down into 6 different classes to attend the Single Responsibility Principle (SRP). 

#### ConsoleColour

Enum with the only purpose to map colours that will be used to alter the output text look and feel.

#### Encoder

Probably the main class, where all the logic for encrypting/decrypting file is stored. It handles all the logic needed for these actions to happen.

#### Menu

Simple class for outputting the menu and calling the right functions given the option selected by the user.

#### ProgressBar

Simple class designed to output a progress bar the will be shown while a file is encrypted or decrypted.

#### Runner

Main class of the program that simply tells the program how to run.

#### TextFileReader

Handles the file reader operations, returning what is needed for an encrypted or decrypted file.

### How to run

```
cd src
javac ie/atu/sw/*.java
java ie.atu.sw.Runner
```