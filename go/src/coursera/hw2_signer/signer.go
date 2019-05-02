package main

import (
	"fmt"
	"sort"
	"strconv"
	"sync"
)

func ExecutePipeline(jobs ...job) {
	in := make(chan interface{})
	out := make(chan interface{})
	wg := &sync.WaitGroup{}

	for _, j := range jobs {
		wg.Add(1)
		go func(j job, in, out chan interface{}) {
			defer wg.Done()
			j(in, out)
			close(out)
		}(j, in, out)
		in = out
		out = make(chan interface{})
	}
	wg.Wait()
}

func SingleHash(in, out chan interface{}) {
	wgMain := &sync.WaitGroup{}
	mutex := &sync.Mutex{}
	for raw := range in {
		data := strconv.Itoa(raw.(int))
		wgMain.Add(1)
		go func(input string, out chan interface{}, mutex *sync.Mutex) {
			var crc32 string
			var crc32md5 string

			defer wgMain.Done()
			wgInner := &sync.WaitGroup{}
			wgInner.Add(2)

			go func(crc32 *string, wgInner *sync.WaitGroup) {
				defer wgInner.Done()

				*crc32 = DataSignerCrc32(data)
				fmt.Println("input: "+data+" SingleHash crc32(data) ", crc32)
			}(&crc32, wgInner)

			go func(crc32md5 *string, wgInner *sync.WaitGroup, mutex *sync.Mutex) {
				defer wgInner.Done()

				mutex.Lock()
				md5 := DataSignerMd5(data)
				fmt.Println("input: "+data+" SingleHash md5(data) ", md5)
				mutex.Unlock()
				*crc32md5 = DataSignerCrc32(md5)
				fmt.Println("input: "+data+" SingleHash crc32(md5(data)) ", crc32md5)
			}(&crc32md5, wgInner, mutex)
			wgInner.Wait()

			singleHash := crc32 + "~" + crc32md5
			fmt.Println("SingleHash result ", singleHash)
			out <- singleHash
		}(data, out, mutex)
	}
	wgMain.Wait()
}

func MultiHash(in, out chan interface{}) {
	const count int = 6
	wgMain := &sync.WaitGroup{}
	for raw := range in {
		strData := raw.(string)
		wgMain.Add(1)
		go func(strData string, wgMain *sync.WaitGroup) {
			defer wgMain.Done()
			maps := make(map[int]string, count)
			wgInner := &sync.WaitGroup{}
			var result string
			mutex := &sync.Mutex{}

			for i := 0; i < count; i++ {
				wgInner.Add(1)
				go func(input string, maps *map[int]string, i int, wgInner *sync.WaitGroup) {
					defer wgInner.Done()
					crc32 := DataSignerCrc32(strconv.Itoa(i) + strData)
					mutex.Lock()
					(*maps)[i] = crc32
					fmt.Println("input: " + strData + " MultiHash: crc32 step " + strconv.Itoa(i) + " result " + (*maps)[i])
					mutex.Unlock()
				}(strData, &maps, i, wgInner)
			}
			wgInner.Wait()

			for i := 0; i < count; i++ {
				result += maps[i]
			}
			fmt.Println("input: " + strData + " MultiHash result: " + result)
			out <- result
		}(strData, wgMain)
	}
	wgMain.Wait()
}

func CombineResults(in, out chan interface{}) {
	var results []string
	var result string
	for raw := range in {
		data := raw.(string)
		results = append(results, data)
	}
	sort.SliceStable(results, func(i, j int) bool { return results[i] < results[j] })
	for idx, i := range results {
		result += i
		if idx != len(results)-1 {
			result += "_"
		}
	}
	out <- result
}

func getCrc32(data string, crc32ch chan string) {
	crc32 := DataSignerCrc32(data)
	fmt.Println("input: "+data+" SingleHash crc32(data) ", crc32)
	crc32ch <- crc32
}

func getMd5(data string, crc32ch chan string, mutex *sync.Mutex) {
	mutex.Lock()
	md5 := DataSignerMd5(data)
	fmt.Println("input: "+data+" SingleHash md5(data) ", md5)
	mutex.Unlock()
	crc32 := DataSignerCrc32(md5)
	fmt.Println("input: "+data+" SingleHash crc32(md5(data)) ", crc32)
	crc32ch <- crc32
}
