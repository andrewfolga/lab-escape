The solution is an iterative version of Depth First Search (DFS) algorithm.

The API consist of a single endpoint:

`POST /labescape`

that accepts 3 arguments (it is assumed the input is well formed):
- staring coordinate x (horizontal lines)
- staring coordinate y (vertical lines)
- the labyrinth to escape

and is responsible for finding an escape path and returning: 
- the given labyrinth with the path applied to it.

If no escape available a response with "Impossible!" message body is returned.

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
	O****O***O
	O*OO*O*O*O
	O* O*O*O*O
	O OO***O**
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

 
 Task notes:
 
 Using the framework(s) of your choice (if any) we'd like you to implement a relatively simple API over HTTP which accepts a "maze" structure as input, along with starting point, and returns the solution to said maze. 
  
 Please use the following repository as a starting point - https://drive.google.com/file/d/0B2lsiqNiQJt7UXhsandBRmxnb3M/view?usp=sharing - we'd like you to implement the drawPathForEscape method in the LabEscape class. The README.md covers what you should output, and I'd certainly like to see how you approach this from a unit testing angle.
