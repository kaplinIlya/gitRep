package main

import (
	"encoding/json"
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
	"time"
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
	expectedOk := SearchResponse{
		Users: []User{
			{
				Id:     0,
				Name:   "Boyd Wolf",
				Age:    22,
				About:  "Nulla cillum enim voluptate consequat laborum esse excepteur occaecat commodo nostrud excepteur ut cupidatat. Occaecat minim incididunt ut proident ad sint nostrud ad laborum sint pariatur. Ut nulla commodo dolore officia. Consequat anim eiusmod amet commodo eiusmod deserunt culpa. Ea sit dolore nostrud cillum proident nisi mollit est Lorem pariatur. Lorem aute officia deserunt dolor nisi aliqua consequat nulla nostrud ipsum irure id deserunt dolore. Minim reprehenderit nulla exercitation labore ipsum.\n",
				Gender: "male",
			},
		},
		NextPage: false,
	}
	cases := []TestCase{

		// // limit<0
		// TestCase{

		// 	Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
		// 	Rq:     SearchRequest{Limit: -1},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// // offset<0
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
		// 	Rq:     SearchRequest{Limit: 50, Offset: -1},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// // wrong oderBy
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
		// 	Rq:     SearchRequest{Limit: 1, OrderBy: 2},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// // wrong token
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: "Unknown"},
		// 	Rq:     SearchRequest{Limit: 1},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },
		// // unknown error
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL + "wrong", AccessToken: accessToken},
		// 	Rq:     SearchRequest{Limit: 1},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// // timeout
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: "timeout"},
		// 	Rq:     SearchRequest{},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// // filename
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: "StatusInternalServerError"},
		// 	Rq:     SearchRequest{},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// //ErrorBadOrderField
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
		// 	Rq:     SearchRequest{OrderField: "bad"},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		// //ErrorUnknownOrderField
		// TestCase{
		// 	Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
		// 	Rq:     SearchRequest{OrderField: "unknown"},
		// 	Rs:     getEmptyResponse(),
		// 	Err:    true,
		// },

		//ok
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Query: "Boyd", Offset: 0, Limit: 1},
			Rs:     &expectedOk,
			Err:    false,
		},
	}

	for i, testCase := range cases {
		resp, err := testCase.Client.FindUsers(testCase.Rq)
		if testCase.Err && err == nil {
			t.Errorf("case "+strconv.Itoa(i)+" error got=%v want=%v", err, testCase.Err)
		}

		if resp != nil && !reflect.DeepEqual(resp, testCase.Rs) {
			t.Errorf("case " + strconv.Itoa(i))
		}
	}
}

func getEmptyResponse() *SearchResponse {
	return nil
}

func SearchServer(w http.ResponseWriter, r *http.Request) {
	filename := "dataset.xml"
	if r.Header.Get("AccessToken") == "timeout" {
		time.Sleep(3 * time.Second)
		return
	}
	if r.Header.Get("AccessToken") == "StatusInternalServerError" {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	if r.Header.Get("AccessToken") != accessToken {
		w.WriteHeader(http.StatusUnauthorized)
		return
	}

	orderField := r.FormValue("order_field")
	if orderField == "bad" {
		w.WriteHeader(http.StatusBadRequest)
		json, _ := json.Marshal(SearchErrorResponse{Error: "ErrorBadOrderField"})
		w.Write(json)
		return
	}
	if orderField == "unknown" {
		w.WriteHeader(http.StatusBadRequest)
		json, _ := json.Marshal(SearchErrorResponse{Error: "ErrorUnknownOrderField"})
		w.Write(json)
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
	orderBy, err := strconv.Atoi(r.FormValue("order_by"))
	if err != nil || (-1 <= orderBy) && (orderBy >= 1) {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	xmlFile, err1 := os.Open(filename)
	defer xmlFile.Close()
	byteFile, err2 := ioutil.ReadAll(xmlFile)
	var users users
	err3 := xml.Unmarshal(byteFile, &users)
	if err1 != nil || err2 != nil || err3 != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	var findUsers []User
	for _, row := range users.all {
		if strings.Contains(row.FirstName+row.LastName, r.FormValue("query")) ||
			strings.Contains(row.About, r.FormValue("query")) {
			if offset != 0 {
				offset--
				continue
			}
			findUsers = append(findUsers, User{Id: row.Id, Name: row.FirstName + " " + row.LastName,
				Age: row.Age, About: row.About, Gender: row.Gender})

			if len(findUsers) == limit {
				break
			}
		}

	}
	result, _ := json.Marshal(findUsers)
	w.Write(result)
	return

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
				return findUsers[i].Name < findUsers[j].Name
			case -1:
				return findUsers[i].Name > findUsers[j].Name
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
		w.WriteHeader(http.StatusOK)
		return
	}
}

// func findUsers(file, query, orderField string, limit, offset, order_by int) ([]User, error){

// }
