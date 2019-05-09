package main

import "fmt"

//easyjson:json
type User123 struct {
	Browsers []string `json:"browsers"`
	Email    string   `json:"email"`
	Name     string   `json:"name"`
}

func main() {
	fmt.Println("")
}
