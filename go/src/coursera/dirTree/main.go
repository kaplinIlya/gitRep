package main

import (
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"os"
	"sort"
	"strconv"
)

func main() {
	out := os.Stdout
	if !(len(os.Args) == 2 || len(os.Args) == 3) {
		panic("usage go run main.go . [-f]")
	}
	path := os.Args[1]
	printFiles := len(os.Args) == 3 && os.Args[2] == "-f"
	err := dirTree(out, path, printFiles)
	if err != nil {
		panic(err.Error())
	}
}

func dirTree(out io.Writer, path string, printFiles bool) error {
	var tree string
	tree, err := printTreeRecursive(path, "", 1, printFiles)
	if err != nil {
		fmt.Printf("Error occurs: " + err.Error())
	}
	out.Write([]byte(tree))
	return nil
}

func printTreeRecursive(path string, prevLine string, level int, printfiles bool) (string, error) {
	var treeRecursive string
	var fileName string
	var tabulation string
	var nextPrefix string

	files, err := ioutil.ReadDir(path)
	sort.SliceStable(files, func(k, j int) bool { return files[k].Name() < files[j].Name() })
	if err != nil {
		return "", errors.New("Error open path: " + err.Error())
	}

	if !printfiles {
		files = deleteFiles(files)
	}

	for _, f := range files {
		tabulation = ""
		if isNotLast(f, files) {
			fileName = "├───" + f.Name()
			nextPrefix = "│"
		} else {
			fileName = "└───" + f.Name()
			nextPrefix = ""
		}

		if !f.IsDir() {
			fileName += " " + printSize(f)
		}

		treeRecursive = treeRecursive + prevLine + fileName + "\n"
		tabulation = prevLine + nextPrefix + "\t"
		var subdir string
		if f.IsDir() {
			subdir, err = printTreeRecursive(path+string(os.PathSeparator)+f.Name(), tabulation, level+1, printfiles)
		}
		if err != nil {
			return "", errors.New("Error reading recursive: " + err.Error())
		}
		treeRecursive += subdir

	}
	return treeRecursive, nil
}

func deleteFiles(files []os.FileInfo) []os.FileInfo {
	dirs := make([]os.FileInfo, 0)
	for _, f := range files {
		if f.IsDir() {
			dirs = append(dirs, f)
		}
	}
	return dirs
}

func printSize(file os.FileInfo) string {
	var fileSize string
	size := file.Size()
	if size == 0 {
		fileSize = "(empty)"
	} else {
		fileSize = "(" + strconv.Itoa(int(size)) + "b)"
	}
	return fileSize
}

func isNotLast(file os.FileInfo, files []os.FileInfo) bool {
	return file != files[len(files)-1]
}
