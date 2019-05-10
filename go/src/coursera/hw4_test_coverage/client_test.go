package main

import (
	"encoding/xml"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"os"
	"reflect"
	"sort"
	"strconv"
	"strings"
	"testing"
)

type user struct {
	Id        int    `xml:"id"`
	FirstName string `xml:"first_name"`
	LastName  string `xml:"last_name"`
	Age       int    `xml:"age"`
	About     string `xml:"about"`
	Gender    string `xml:"gender"`
}

type users struct {
	all []user `xml:"row"`
}

const (
	accessToken = "accessToken"
	fileName    = "dataset.xml"
)

type TestCase struct {
	Client SearchClient
	Rq     SearchRequest
	Rs     *SearchResponse
	Err    bool
}

func TestFindUsers(t *testing.T) {

	ts := httptest.NewServer(http.HandlerFunc(SearchServer))
	defer ts.Close()
	cases := []TestCase{

		// limit<0
		TestCase{

			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Limit: -1},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		// offset<0
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Offset: -1},
			Rs:     getEmptyResponse(),
			Err:    true,
		},
	}

	for i, testCase := range cases {
		resp, err := testCase.Client.FindUsers(testCase.Rq)
		if testCase.Err && err == nil {
			t.Errorf("case "+strconv.Itoa(i)+" error got=%v want=%v", err, testCase.Err)
		}
		if !reflect.DeepEqual(resp, testCase.Rs) {
			t.Errorf("case "+strconv.Itoa(i)+" response got=%v want=%v", resp, testCase.Rs)
		}
	}
}

func getEmptyResponse() *SearchResponse {
	return nil
}

func SearchServer(w http.ResponseWriter, r *http.Request) {
	if r.Header.Get("AccessToken") != accessToken {
		w.WriteHeader(http.StatusUnauthorized)
		return
	}

	limit, err := strconv.Atoi(r.FormValue("limit"))
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	offset, err := strconv.Atoi(r.FormValue("offset"))
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	orderBy, err := strconv.Atoi(r.FormValue("orderBy"))
	if err != nil || (-1 <= orderBy) && (orderBy <= 1) {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	xmlFile, err1 := os.Open(fileName)
	defer xmlFile.Close()
	byteValue, err2 := ioutil.ReadAll(xmlFile)
	var users users
	err3 := xml.Unmarshal(byteValue, users)
	if err1 != nil || err2 != nil || err3 != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	var findUsers []user
	for _, row := range users.all {
		if strings.Contains(row.FirstName+row.LastName, r.FormValue("query")) ||
			strings.Contains(row.About, r.FormValue("query")) {
			if offset != 0 {
				offset--
				continue
			}
			findUsers = append(findUsers, user{Id: row.Id, FirstName: row.FirstName, LastName: row.LastName,
				Age: row.Age, About: row.About, Gender: row.Gender})

			if len(findUsers) == limit {
				break
			}
		}

	}
	switch r.FormValue("orderField") {
	case "Id":
		sort.Slice(findUsers[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return findUsers[i].Id < findUsers[j].Id
			case -1:
				return findUsers[i].Id > findUsers[j].Id
			default:
				return true
			}
		})

	case "Name":
		sort.Slice(findUsers[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return findUsers[i].FirstName < findUsers[j].FirstName
			case -1:
				return findUsers[i].FirstName > findUsers[j].FirstName
			default:
				return true
			}
		})

	case "Age":
		sort.Slice(findUsers[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return findUsers[i].Age < findUsers[j].Age
			case -1:
				return findUsers[i].Age > findUsers[j].Age
			default:
				return true
			}
		})

	default:
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}
