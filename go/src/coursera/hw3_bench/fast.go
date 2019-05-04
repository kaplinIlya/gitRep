package main

//	go test -bench . -benchmem

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io"
	"os"
	"strings"
	"sync"
)

type User struct {
	Browsers []string `json:"browsers"`
	Email    string   `json:"email"`
	Name     string   `json:"name"`
}

func FastSearch(out io.Writer) {
	file, err := os.OpenFile(filePath, os.O_RDONLY, 0100)
	if err != nil {
		panic(err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	//var user User
	var email string
	var i uint16 = 0
	var foundUsers string = ""
	var isAndroid, isMSIE bool
	seenBrowsers := make(map[string]struct{}, 0)

	for scanner.Scan() {
		user := userPool.Get().(*User)
		json.Unmarshal(scanner.Bytes(), user)
		isAndroid, isMSIE = false, false
		for _, browser := range user.Browsers {
			if strings.Contains(browser, "MSIE") {
				isMSIE = true
				seenBrowsers[browser] = struct{}{}
			}
			if strings.Contains(browser, "Android") {
				isAndroid = true
				seenBrowsers[browser] = struct{}{}
			}

		}
		if isAndroid && isMSIE {
			email = strings.Replace(user.Email, "@", " [at] ", 1)
			foundUsers += fmt.Sprintf("[%d] %s <%s>\n", i, user.Name, email)
		}
		putPool(user)
		i++
	}

	fmt.Fprintln(out, "found users:\n"+foundUsers)
	fmt.Fprintln(out, "Total unique browsers", len(seenBrowsers))
}

var userPool = sync.Pool{
	New: func() interface{} {
		return new(User)
	},
}

func getUserPool() (b interface{}) {
	mem := userPool.Get()
	if mem != nil {
		b = mem.(*User)
	}
	return
}
func (u *User) reset() {
	u.Browsers = []string{}
	u.Email = ""
	u.Name = ""
}

func putPool(b interface{}) {
	switch b := b.(type) {
	case *User:
		b.reset()
		userPool.Put(b)
	}
}
