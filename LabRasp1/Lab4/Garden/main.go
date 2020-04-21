package main

import (
	"fmt"
	"sync"
	"math/rand"
	"time"
	"os"
)

const N = 5
const M = 5

var seed = rand.NewSource(time.Now().UnixNano());

var random = rand.New(seed);

func Nature(n int, m int, garden *[N][M]int, rwlock *sync.RWMutex) {
	for {
		rwlock.Lock()
		var row = random.Intn(n)
		var col = random.Intn(m)
		var value = random.Intn(3)

		garden[col][row] = value

		rwlock.Unlock()
		time.Sleep(100 * time.Millisecond)
	}
}

func Gardener(n int, m int, garden *[N][M]int, rwlock *sync.RWMutex) {
	for {
		rwlock.Lock()
		for i:= 0; i < n; i++ {
			for j := 0; j < m; j++ {
				garden[i][j]++
			}
		}

		rwlock.Unlock()
		time.Sleep(2000 * time.Millisecond)
	}
}

func ConsoleMonitor(n int, m int, garden *[N][M]int, rwlock *sync.RWMutex) {
	for {
		rwlock.RLock()
		for i := 0; i < n; i++ {
			for j := 0; j < m; j++ {
				fmt.Print(garden[j][i])
			}
			fmt.Println()
		}
		fmt.Println("_________")
		rwlock.RUnlock()
		time.Sleep(3000 * time.Millisecond)
	}
}
var (
	newFile *os.File
	err     error

)

func FileMonitor(n int, m int, garden *[N][M]int, rwlock *sync.RWMutex) {
	for {
		rwlock.RLock()
		f, _ := os.OpenFile("input.txt", os.O_APPEND|os.O_WRONLY, os.ModeAppend)
		var toWrite = ""
		for i := 0; i < n; i++ {
			for j := 0; j < m; j++ {
				toWrite += string(garden[j][i] + '0')
			}
			toWrite += "\n"
		}
		f.WriteString(toWrite + "\n_____________\n")
		f.Close()
		rwlock.RUnlock()
		time.Sleep(1000 * time.Millisecond)
	}
}

func main() {
	var garden [N][M]int
	newFile, err = os.Create("input.txt")
	var rwlock = &sync.RWMutex{}
	var wg = sync.WaitGroup{}
	wg.Add(4)

	for i:= 0; i < N; i++ {
		for j := 0; j < M; j++ {
			garden[j][i] = random.Intn(3)
		}
	}

	go Nature(N, M, &garden, rwlock)
	go Gardener(N, M, &garden, rwlock)
	go ConsoleMonitor(N, M, &garden, rwlock)
	go FileMonitor(N, M, &garden, rwlock)
	wg.Wait()

}