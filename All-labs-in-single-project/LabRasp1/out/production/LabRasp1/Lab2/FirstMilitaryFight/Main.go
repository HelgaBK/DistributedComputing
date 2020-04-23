package main;

import (
	"math/rand"
	"time"
	"fmt"
)

var s = rand.NewSource(time.Now().UnixNano())
var r = rand.New(s)

func main() {

	workIsDone := make(chan bool, 1)

	sizeOfStuff := 3
	stock := NewStock(sizeOfStuff)
	truck := NewEmptyTruck()

	fmt.Println("Init stock.stuff: ", stock.stuff)

	go Ivanov(*stock)
	go Petrov(*truck)
	go Necheporchuk(workIsDone)

	<-workIsDone
}

type Truck struct {
	cargo []int
}

func NewEmptyTruck() *Truck {
	return &Truck{make([]int, 0)}
}

type Stock struct {
	stuff []int
}

func GetFirstElement(stuff *[]int) int {
	result := (*stuff)[0]
	*stuff = (*stuff)[1:]
	return result
}

func NewStuff(size int) *[]int {
	var stuff []int = make([]int, size)
	for i, _ := range stuff {
		stuff[i] = r.Intn(500) + 350
	}
	return &stuff
}

func NewStock(size int) *Stock {
	return &Stock{*NewStuff(size)}
}

var buffer []int = make([]int, 0)
var isFirstElementCounted bool = false
var isRunningPetrov bool = true
var isRunningNecheporchuk bool = true

func Ivanov(stock Stock) {
	for range stock.stuff {
		buffer = append(buffer, GetFirstElement(&stock.stuff))
		fmt.Println("\nIvanov get one more and put it in the buffer.\nStock: ", stock.stuff, "\nBuffer: ", buffer)
	}
	isRunningPetrov = false
}

func Petrov(truck Truck) {
	for isRunningPetrov {
		if (isFirstElementCounted && len(buffer) > 0) {
			element := GetFirstElement(&buffer)
			truck.cargo = append(truck.cargo, element)
			fmt.Println("\nPetrov get ", element, " and put it in the truck.\ntruck.cargo: ", truck.cargo)
			isFirstElementCounted = false;
		} else {
			time.Sleep(time.Millisecond * 200)
		}
	}
	for (len(buffer) > 0) {
		if (isFirstElementCounted) {
			element := GetFirstElement(&buffer)
			truck.cargo = append(truck.cargo, element)
			fmt.Println("\nPetrov get ", element, " and put it in the truck.\nbuffer: ", buffer, "\ntruck.cargo: ", truck.cargo)
			isFirstElementCounted = false;
			//i++
		} else {
			time.Sleep(time.Millisecond * 200)
		}
	}
	isRunningNecheporchuk = false
	fmt.Println("\nFinally. Truck cargo: ", truck.cargo)
}

func Necheporchuk(workIsDone chan bool) {
	var totalCost int = 0
	for isRunningNecheporchuk {
		if (!isFirstElementCounted && len(buffer) > 0) {

			totalCost += buffer[0];
			fmt.Println("\nNecheporchuk works. Just assessed element: ", buffer[0], "\ntotalCost =", totalCost)

			isFirstElementCounted = true;
		} else {
			time.Sleep(time.Millisecond * 200)
		}
	}
	fmt.Println("\nTotal cost: ", totalCost)
	workIsDone <- true
}
