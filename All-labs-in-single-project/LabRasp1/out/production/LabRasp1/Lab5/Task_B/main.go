package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

func switchSymbol(str *[]byte) {
	ind := random.Intn(len(*str))
	switch (*str)[ind] {
	case 'A':
		(*str)[ind] = 'C'
		break
	case 'B':
		(*str)[ind] = 'D'
		break
	case 'C':
		(*str)[ind] = 'A'
		break
	case 'D':
		(*str)[ind] = 'B'
		break
	}
}

func routine(initStr *[]byte, wg *sync.WaitGroup, finish chan bool, again chan bool) {
	for {
		switchSymbol(initStr)
		wg.Done()

		select {
		case <-finish:
			return
		case <-again:
			continue
		}
	}
}

func calc(str *[]byte) int {
	res := 0
	for _, char := range *str {
		if char == 'A' || char == 'B' {
			res++
		}
	}
	return res
}

func checker(strings *[][]byte, wg *sync.WaitGroup, finish chan bool, again chan bool) {
	for {
		wg.Wait()

		calcDict := make(map[int]int, len(*strings))
		for _, str := range *strings {
			if _, ok := calcDict[calc(&str)]; ok {
				calcDict[calc(&str)] += 1
			} else {
				calcDict[calc(&str)] = 1
			}
		}

		for _, v := range calcDict {
			if v >= 3 {
				for range *strings {
					finish <- true
				}
				fmt.Println("\nBarrier finished work:")
				printAsStrings(strings)
				return
			}
		}

		fmt.Println("\nBarrier was restarted:")
		printAsStrings(strings)
		wg.Add(len(*strings))
		for range *strings {
			again <- true
		}
	}
}

func printAsStrings(strings *[][]byte) {
	for _, str := range *strings {
		fmt.Println(string(str))
	}
}

func main() {
	wg := &sync.WaitGroup{}

	finish := make(chan bool)
	again := make(chan bool)

	wg.Add(4)

	strings := [][]byte{[]byte("AABCD"), []byte("BABCD"), []byte("CABCD"), []byte("DABCD")}

	go routine(&strings[0], wg, finish, again)
	go routine(&strings[1], wg, finish, again)
	go routine(&strings[2], wg, finish, again)
	go routine(&strings[3], wg, finish, again)
	checker(&strings, wg, finish, again)
}
