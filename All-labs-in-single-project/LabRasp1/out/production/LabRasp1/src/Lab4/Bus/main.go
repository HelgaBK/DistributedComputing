package main

import (
	"sync"
	"fmt"
	"time"
	"math/rand"
)

func print(Graph *[][]int) {
	size := len(*Graph)
	for i := 0; i < size; i++ {
		for j := 0; j < size; j++ {
			fmt.Print((*Graph)[i][j], " ")
		}
		fmt.Println()
	}
}

func changePrice(first, second int, Graph *[][]int) {
	if (first != second) {
		var newPrice = rand.Intn(100) + 1;
		(*Graph)[first][second] = newPrice
		(*Graph)[second][first] = newPrice
		fmt.Println("new Price.way form", first, " to ", second, " = ", newPrice);
		print(Graph)

	}
}

func deleteWay(first, second int, Graph *[][]int) {

	(*Graph)[first][second] = 0
	(*Graph)[second][first] = 0
	fmt.Println("delete. way form", first, " to ", second, " = ", 0);
	print(Graph)

}

func ChangePriceThread(mutex *sync.RWMutex, Graph *[][]int) {
	for {

		mutex.Lock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (*Graph)[first][second] == 0 {
				changePrice(first, second, Graph)
			}
		}
		mutex.Unlock()
		time.Sleep(370 * time.Millisecond)
		//fmt.Println("change price")
	}
}

func AddDeleteWayThread(mutex *sync.RWMutex, Graph *[][]int) {
	for {

		mutex.Lock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (*Graph)[first][second] == 0 {
				changePrice(first, second, Graph)
			} else {
				deleteWay(first, second, Graph)
			}
		}
		mutex.Unlock()
		time.Sleep(300 * time.Millisecond)
		//	fmt.Println("add/delete Way")

	}
}

func AddDeleteTown(mutex *sync.RWMutex, Graph *[][]int) {
	for {
		mutex.Lock()
		numPlaces := len(*Graph)
		var wantToDel bool
		if rand.Intn(2)%2 == 0 {
			wantToDel = true
		} else {
			wantToDel = false
		}
		if numPlaces < 3 {
			wantToDel = false
		}
		if (wantToDel) {
			numPlaces--
			for i := 0; i < numPlaces; i++ {
				(*Graph)[i] = (*Graph)[i][:len((*Graph)[i])-1]
			}
			*Graph = (*Graph)[:len(*Graph)-1]
			fmt.Println("delete place, num = ", numPlaces)

		} else {
			numPlaces++
			var newLine []int
			for i := 0; i < numPlaces-1; i++ {
				(*Graph)[i] = append((*Graph)[i], 0)
				newLine = append(newLine, 0)
			}
			newLine = append(newLine, 0)
			*Graph = append(*Graph, newLine)
			fmt.Println("add new , num = ", numPlaces)

		}

		mutex.Unlock()
		time.Sleep(200 * time.Millisecond)

	}
}

func dfs(Graph *[][]int, first, second int) {
	var size = len(*Graph)
	var dist = make([]int, size)
	var visited = make([]int, size)
	var arrayVert = make([]int, size)
	var parent = make([]int, size)

	for i := 0; i < size; i++ {
		dist[i] = -1
		visited[i] = -1
		arrayVert[i] = -1;
		parent[i] = -1
	}
	dist[first] = 0
	visited[first] = 0
	parent[first] = -1
	arrayVert[0] = first;
	var ind = 0;
	var realSize = 1;
	for ind < realSize {
		var temp = arrayVert[ind]
		ind++
		visited[temp] = 1;
		for i := 0; i < size; i++ {
			if (*Graph)[temp][i] != 0 {
				if (visited[i] == -1) {
					parent[i] = temp;
					dist[i] = (*Graph)[temp][i] + dist[temp]
					arrayVert[realSize] = i
					realSize++
					visited[i] = 0
				} else if visited[i] == 0 {
					if (dist[i] > (*Graph)[temp][i]+dist[temp]) {
						dist[i] = (*Graph)[temp][i] + dist[temp]
						parent[i] = temp
					}
				}
			}
		}
	}
	fmt.Println(dist[second])
	for parent[second] != -1 {
		second = parent[second]
		fmt.Println(second, "->")

	}
	time.Sleep(1000 * time.Millisecond)
	//if (dist[second])

}

func FindWay(mutex *sync.RWMutex, Graph *[][]int) {

	for {
		mutex.RLock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (first != second) {
				if (*Graph)[first][second] != 0 {
					fmt.Println("find Way ", first, " to ", second, " = ", (*Graph)[first][second])

				} else {
					fmt.Println("no Way ", first, " to ", second);
					//dfs(Graph, first, second)
				}

			}

		}
		mutex.RUnlock()

		time.Sleep(150 * time.Millisecond)

	}

}

func main() {
	var mutex sync.RWMutex
	var Graph [][]int

	go AddDeleteTown(&mutex, &Graph)
	//	time.Sleep(1000)
	go ChangePriceThread(&mutex, &Graph)

	go AddDeleteWayThread(&mutex, &Graph)
	//
	go FindWay(&mutex, &Graph)

	var input string
	fmt.Scanln(&input)
}
