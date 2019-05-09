package main

//	go test -bench . -benchmem
//go test -bench . -benchmem -cpuprofile=cpu.out -memprofile=mem.out -memprofilerate=1
//go tool pprof mem.out -inuse_space / -inuse_objects / -alloc_space / -alloc_objects

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io"
	"os"
	"strings"
	"sync"

	easyjson "github.com/mailru/easyjson"
	jlexer "github.com/mailru/easyjson/jlexer"
	jwriter "github.com/mailru/easyjson/jwriter"
)

type User struct {
	Browsers []string `json:"browsers"`
	Email    string   `json:"email"`
	Name     string   `json:"name"`
}

func FastSearch(out io.Writer) {
	file, _ := os.Open(filePath)
	scanner := bufio.NewScanner(file)
	var user User
	var i int
	var isAndroid, isMSIE bool
	seenBrowsers := make(map[string]struct{}, 150)

	for scanner.Scan() {
		err := user.UnmarshalJSON(scanner.Bytes())
		if err != nil {
			panic(err)
		}
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
			if i == 1 {
				fmt.Fprintln(out, "found users:")
			}
			fmt.Fprintf(out, "[%d] %s <%s>\n", i, user.Name, strings.Replace(user.Email, "@", " [at] ", 1))
		}
		i++
	}
	file.Close()
	fmt.Fprintln(out, "")
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
func putUserPool(b interface{}) {
	switch b := b.(type) {
	case *User:
		b.reset()
		userPool.Put(b)
	}
}

var browsersPool = sync.Pool{
	New: func() interface{} {
		return new(string)
	},
}

// suppress unused package warning
var (
	_ *json.RawMessage
	_ *jlexer.Lexer
	_ *jwriter.Writer
	_ easyjson.Marshaler
)

func easyjson63819b0fDecodeCourseraHw3Bench(in *jlexer.Lexer, out *User) {
	isTopLevel := in.IsStart()
	if in.IsNull() {
		if isTopLevel {
			in.Consumed()
		}
		in.Skip()
		return
	}
	in.Delim('{')
	for !in.IsDelim('}') {
		key := in.UnsafeString()
		in.WantColon()
		if in.IsNull() {
			in.Skip()
			in.WantComma()
			continue
		}
		switch key {
		case "browsers":
			if in.IsNull() {
				in.Skip()
				out.Browsers = nil
			} else {
				in.Delim('[')
				if out.Browsers == nil {
					if !in.IsDelim(']') {
						out.Browsers = make([]string, 0, 4)
					} else {
						out.Browsers = []string{}
					}
				} else {
					out.Browsers = (out.Browsers)[:0]
				}
				for !in.IsDelim(']') {
					var v1 string
					v1 = string(in.String())
					out.Browsers = append(out.Browsers, v1)
					in.WantComma()
				}
				in.Delim(']')
			}
		case "email":
			out.Email = string(in.String())
		case "name":
			out.Name = string(in.String())
		default:
			in.SkipRecursive()
		}
		in.WantComma()
	}
	in.Delim('}')
	if isTopLevel {
		in.Consumed()
	}
}
func easyjson63819b0fEncodeCourseraHw3Bench(out *jwriter.Writer, in User) {
	out.RawByte('{')
	first := true
	_ = first
	{
		const prefix string = ",\"browsers\":"
		if first {
			first = false
			out.RawString(prefix[1:])
		} else {
			out.RawString(prefix)
		}
		if in.Browsers == nil && (out.Flags&jwriter.NilSliceAsEmpty) == 0 {
			out.RawString("null")
		} else {
			out.RawByte('[')
			for v2, v3 := range in.Browsers {
				if v2 > 0 {
					out.RawByte(',')
				}
				out.String(string(v3))
			}
			out.RawByte(']')
		}
	}
	{
		const prefix string = ",\"email\":"
		if first {
			first = false
			out.RawString(prefix[1:])
		} else {
			out.RawString(prefix)
		}
		out.String(string(in.Email))
	}
	{
		const prefix string = ",\"name\":"
		if first {
			first = false
			out.RawString(prefix[1:])
		} else {
			out.RawString(prefix)
		}
		out.String(string(in.Name))
	}
	out.RawByte('}')
}

// MarshalJSON supports json.Marshaler interface
func (v User) MarshalJSON() ([]byte, error) {
	w := jwriter.Writer{}
	easyjson63819b0fEncodeCourseraHw3Bench(&w, v)
	return w.Buffer.BuildBytes(), w.Error
}

// MarshalEasyJSON supports easyjson.Marshaler interface
func (v User) MarshalEasyJSON(w *jwriter.Writer) {
	easyjson63819b0fEncodeCourseraHw3Bench(w, v)
}

// UnmarshalJSON supports json.Unmarshaler interface
func (v *User) UnmarshalJSON(data []byte) error {
	r := jlexer.Lexer{Data: data}
	easyjson63819b0fDecodeCourseraHw3Bench(&r, v)
	return r.Error()
}

// UnmarshalEasyJSON supports easyjson.Unmarshaler interface
func (v *User) UnmarshalEasyJSON(l *jlexer.Lexer) {
	easyjson63819b0fDecodeCourseraHw3Bench(l, v)
}
