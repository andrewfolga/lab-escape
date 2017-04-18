Overview
========

The solution is an iterative version of Depth First Search (DFS) algorithm.

The API consist of four endpoints:


Create labyrinth
-------------------
Creates well labyrinth input of which is well formed. Stores it against repository keyed on the data that describes labyrinth (see example).
```
POST /labs
```

_Note: It is assumed the labyrinth itself is always well formed._

Examples:

#### Example 1: Create and store well formed labyrinth


**Request url**
```
POST /labs/
```

*Request Body:*

    1 3
	OOOOOOOOOO
	O    O   O
	O OO O O O
	O  O O O O
	O OO   O  
	O OOOOOOOO
	O        O
	OOOOOOOOOO

*Response Body:*

    13108

|Status Code |Description                                                                         |
|------------|----------------------------------------------------------------|
|200         | The key the lab is stored against startX+startY+sizeX+sizeY    |



Get labyrinth
-------------
Retrieves labyrinth with additional statistics like number of walls and number of white spaces.

```
GET /labs/{labKey}
```

#### Example 1: Get labyrinth


**Request url**
```
GET /labs/13108
```

*Response Body:*

    LabyrinthData{startPosX=1, startPosY=3, numberOfWalls=49, numberOfEmptySpaces=31, labyrinth=[[C@26fbb66a, [C@45e19cf3, [C@1717942a, [C@3de7e015, [C@46d44925, [C@f1b4308, [C@24062005, [C@26e38bd8]}

|Status Code |Description                                                                         |
|------------|----------------------------------------------------------------|
|200         | The labyrinth data including the additional statistics         |

#### Example 2: Labyrinth does not exist


**Request url**
```
GET /labs/13111
```
    

|Status Code |Description                                                                         |
|------------|-----------------------------------------------------------------|
|404         | The given labyrinth does not exist                              |



Find value in labyrinth
-----------------------
Finds a value in the labyrinth given the key, x and y coordinates.
```
GET /labs/{labKey}/coordX/{x}/coordY/{y}
```

#### Example 1: Find value for labyrinth


**Request url**
```
GET /labs/13108/coordX/0/coordY/0
```

*Response Body:*

    O

|Status Code |Description                                                                         |
|------------|-----------------------------------------------------------------|
|200         | The given labyrinth value positioned within x and y coordinates |


#### Example 2: Labyrinth does not exist


**Request url**
```
GET /labs/13111/coordX/0/coordY/0
```
    

|Status Code |Description                                                                         |
|------------|-----------------------------------------------------------------|
|404         | The given labyrinth does not exist                              |




Escape from a given labyrinth
-----------------------------
```
GET /labs/{labKey}/escape
```

Examples:

Example 1: Escape path not found
--------------------------------

**Request url**
```
GET /labs/31108/escape
```

*Response Body:*


	OOOOOOOOOO
	O••••O•••O
	O•OO•O•O•O
	O• O•O•O•O
	O OO•••O••
	O OOOOOOOO
	O        O
	OOOOOOOOOO



|Status Code |Description             |
|------------|------------------------|
|200         | Escape path found      |

 	

Example 2: Escape path impossible
---------------------------------

**Request url**
```
POST /labs/31108/escape
```

*Response Body:*


	No escape path!


|Status Code |Description             |
|------------|------------------------|
|200         | Escape path missing    |


Testing instructions
====================

1. For Unit tests run the following command:
    
```
./gradlew clean test
```
    
2. For Integration tests run the following command:

```
./gradlew clean itest
```
    
There are test data files in `data` directory that are used in integration tests to simulate simultaneous requests with big data sets (see `large.txt`).
     
Running instructions
======================

Please first run the following:

```
./gradlew clean build
```

which will build the standalone executable in `build/libs` and the follow with: 

```
java -jar build/libs/labescape-1.0-SNAPSHOT.jar
```

Another way is to just run the following:

```
./gradlew bootRun
```

And the server will be available under the following address: `http://localhost:8080/labescape`


### Additional task:


##### User Story 1


As a world famous explorer of Mazes I would like a maze to exist
So that I can explore it

Acceptance Criteria

* A Maze (as defined in Maze1.txt consists of walls 'X', Empty spaces ' ', one and only one Start point 'S' and one and only one exit 'F'
* After a maze has been created the number of walls and empty spaces should be available to me
* After a maze has been created I should be able to put in a co ordinate and know what exists at that point