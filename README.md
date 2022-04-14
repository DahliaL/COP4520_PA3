# Dahlia's Minotaur Birthday Presents & Atmospheric Temperature Modules PA3

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)
My submission for COP4520 Programming Assignment 3. 

# How To Run
In the command prompt/terminal, make sure you are in the correct directory, where ```ThankYouCards.java``` ,```LockFreeList.java``` and ```TempReadingModule.java``` are located. Then write the following commands depending on which program you wish to run:
##### Problem 1 Execution
```
  javac ThankYouCards.java
  java ThankYouCards
```
##### Problem 2 Execution
```
  javac TempReadingModule.java
  java TempReadingModule
```
Both programs will prompt you to input a value n. For problem one, n refers to the number of gifts the Minotaur received and must sort through. For problem 2, n is the number of hours that you would like to simulate for the temperature reading module. 

# Summary of Approach and Efficiency of Design 
##### Problem 1: Minotaur Birthday Presents Party
For the Birthday Presents Party, I chose to make use of the Lock-Free Nonblocking algorithm for a linked list. I pretty much used the textbook code for this verbatim.  

A for-loop iterates through my array of servants, who each "roll a die" to determine whethere they are adding to the list or writing a thank you card and doing a removal. If the servant gets a 0, they are adding to the list, and the algorithm pretty much handles it from that point on. I then remove that gift from the unorderedBag shuffled list. Removals follow the same pattern, except a thank you card tracker gets incremented to ensure that n thank you cards are produced.

The LockFreeList has its own explanation in the textbook and I just mad euse of an implementation that was already given. 

##### Problem 2: Atmospheric Temperature Reading Module
My approach to this problem is quite simple. I create an array of 8 threads. This simulates our 8 sensors. 

A while loop iterates for as many n hours. Inside of the while loop, there's another while loop that simulates the minutes in an hour. A thread is then called to run and perform a simple random number generation and incrementation of a curr variable.

Once the hour is over, a thread is then called to generate the hourly report. This is where preserving the data becomes important. I'm not sure if this was allowed, but I blocked off the array and essentially made a copy that the thread sent to conduct the report would use to have accurate readings that are not being concurrently edited by threads at work with the next hour's readings. 

This approach seems to work, numbers are getting generated and I see no patterns in between hours. When debugging, I saw updates occuring as predicted and so, it seems to be pretty efficient. 

# Optimization
My approach to problem 1 is not optimal whatsoever. It starts to lag after 10,000 gifts, where 50,000 gifts was running about 229 seconds. I am unsure if it's due to my computer, but I had no other ideas to optimize my approach.


# Experimental Evaluation
I created a couple of methods to verify the correctness of both of my approaches.
###### Problem 1
For this problem, I made use of alot of print statements to ensure every gift was being added and removed from the list ina random and concurrent way. It seems that the algorithm is random, which is what we want, but it's not very concurrent, and appears to sequentially add as many gifts as possible before attempting too remove. 

The results were as follows:
| n     | ---- | Time in ms      |
| :---        |    :----:   |         :----: |
| 2      | x       |  4  |
| 10  | x        | 17      |
| 100     | x       | 90   |
| 1000      | x       | 352   |
| 10000     | x       | 4665   |
| 50000     | x       | 239000   |
| 100000     | x       | TBD   |


###### Problem 2
This program was a bit different to evaluate correctness, but I pretty much used print debugging as a means to ensure that hourly reports were done on the correct set of data. I did this by printing out each hour's temperatures and analyzing those, and manually checking for correctness of the program's outputs.

This was how I checked for effienciency and correctness!

#### Citations & Credits
###### Random Number Generation: 
https://www.javatpoint.com/how-to-generate-random-number-in-java

