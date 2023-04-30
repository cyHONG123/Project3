# Project3
* Name: Chen Xue, Hong Chongyuan
* NetID: cxue8, chong10
* Course: CSC172, Tue/Thur 3:25-4:40 
* Lab section:  CSC172-5    Monday/Wednesday 6:15-7:39
* UR email: cxue8@u.rochester.edu, chong10@u.rochester.edu

The code use special node and edge element to read the node and connection writen in the txt file. Then the edge which contain node would be drawn out 
by the JFrame screen, the screen which draw the map can be adjust on width and height. By entering directions method, user can find the shortest path from 
original point to specific point by using djkstra algorithm. Then, the shortest path would be drawn on the map by red line. The distance of shortest path would also be printed. We overcome the specific problem like drawing map in suitable size according to map's mac width and height. Also, the problem on finding shortest path and recording shortest distance has been solved.
ChenXue: creating node and edge component, most part of algorithm on finding shortest path of map and dijkstra algorithm. direction method code like cost recording and getcost method. cost initialize method, draw data pinpoint according to size of map and screen. Debug.
Chongyuan Hong: creating node and edge component. find-node and data extract method. Partial shortest path finding method, dijkstra and direction method. Drawing component and paint component, resizable screen and map. JFrame visual. Read txt file method. Debug.

The code can be run according to methods what project pdf mentioned, like "java streetmapping ur.txt --show". "--directions + id" would form a shortest path from origin point to given point and give out distance of shortest data.

Our code cite and implement the Haversine Distance Algorithm on finding the distance between node. This method convert tow node of longitude and latitude data to distance betwee two. source: https://gist.github.com/vananth22/888ed9a22105670e7a4092bdcf0d72e4
