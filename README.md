##Overview

The solution is an iterative version of Depth First Search (DFS) algorithm.

The API consist of a single endpoint:

`POST /labescape`

that accepts 3 arguments (it is assumed the input is well formed):
- staring coordinate x (horizontal lines)
- staring coordinate y (vertical lines)
- the labyrinth to escape

and is responsible for finding an escape path and returning: 
- the given labyrinth with the path applied to it.

If no escape available a response with "No escape path!" message body is returned.

Examples:

#####Example 1: Escape path not found

**Request url**
```
POST /labescape
```

*Request Body:*

    3 1
    OOOOOOOOOO
    O    O   O
    O OO O O O
    O  O O O O
    O OO   O  
    O OOOOOOOO
    O        O
    OOOOOOOOOO

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

 	

#####Example 2: Escape path impossible

**Request url**
```
POST /labescape
```

*Request Body:*

    3 1
    OOOOOOOOOO
    O    O   O
    O OO O O O
    O  O O O O
    O OO   O O
    O OOOOOOOO
    O        O
    OOOOOOOOOO

*Response Body:*


	No escape path!



|Status Code |Description             |
|------------|------------------------|
|200         | Escape path missing    |

##Testing instructions

1. For Unit tests run the following command:
    
        `./gradlew clean test`
    
2. For Integration tests run the following command:

        `./gradlew clean itest`
    
    There are test data files in `data` directory that are used in integration tests to simulate simultaneous requests with big data sets (see `large.txt`).
     
##Running instructions

Please run the following:

    `./gradlew run`

And the server will be available under the following address: `http://localhost:8081/labescape`