package main

//go test -cover
//go test -coverprofile=cover.out && go tool cover -html=cover.out -o cover.html

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

type RawUser struct {
	Id        int    `xml:"id"`
	FirstName string `xml:"first_name"`
	LastName  string `xml:"last_name"`
	Age       int    `xml:"age"`
	About     string `xml:"about"`
	Gender    string `xml:"gender"`
}

type RawUsers struct {
	XMLName xml.Name  `xml:"root"`
	List    []RawUser `xml:"row"`
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
			Rq:     SearchRequest{Limit: 50, Offset: -1},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		// wrong oderBy
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Limit: 1, OrderBy: 2},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		// wrong token
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: "Unknown"},
			Rq:     SearchRequest{Limit: 1},
			Rs:     getEmptyResponse(),
			Err:    true,
		},
		// unknown error
		TestCase{
			Client: SearchClient{URL: ts.URL + "wrong", AccessToken: accessToken},
			Rq:     SearchRequest{Limit: 1},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		// timeout
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: "timeout"},
			Rq:     SearchRequest{},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		// filename
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: "StatusInternalServerError"},
			Rq:     SearchRequest{},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		//ErrorBadOrderField
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{OrderField: "bad"},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		//ErrorUnknownOrderField
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{OrderField: "unknown"},
			Rs:     getEmptyResponse(),
			Err:    true,
		},

		//ok
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Query: "Boyd", Offset: 0, Limit: 1, OrderField: "Age"},
			Rs:     &expectedOk,
			Err:    false,
		},

		//unpack
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: accessToken},
			Rq:     SearchRequest{Query: "Boyd", Offset: 0, Limit: 0},
			Rs:     getEmptyResponse(),
			Err:    false,
		},

		//wrong json
		TestCase{
			Client: SearchClient{URL: ts.URL, AccessToken: "wrongJson"},
			Rq:     SearchRequest{Offset: 0, Limit: 1},
			Rs:     getEmptyResponse(),
			Err:    false,
		},
	}

	for i, testCase := range cases {
		resp, err := testCase.Client.FindUsers(testCase.Rq)
		if testCase.Err && err == nil {
			t.Errorf("case "+strconv.Itoa(i)+" error got=%v want=%v", err, testCase.Err)
		}

		if testCase.Rs != nil && !reflect.DeepEqual(resp, testCase.Rs) {
			t.Errorf("case "+strconv.Itoa(i)+" error got=%v want=%v", resp, testCase.Rs)
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

	if r.Header.Get("AccessToken") == "wrongJson" {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("____"))
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
	var rawUsers RawUsers
	err3 := xml.Unmarshal([]byte(byteFile), &rawUsers)
	if err1 != nil || err2 != nil || err3 != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	//fmt.Println("all:  " + strconv.Itoa(len(rawUsers.List)))
	var findUsers []User
	for _, row := range rawUsers.List {
		if strings.Contains(row.FirstName+row.LastName, r.FormValue("query")) ||
			strings.Contains(row.About, r.FormValue("query")) {
			if offset != 0 {
				offset--
				continue
			}
			findUsers = append(findUsers, User{Id: row.Id, Name: row.FirstName + " " + row.LastName,
				Age: row.Age, About: row.About, Gender: row.Gender})

			if len(findUsers) == limit-1 {
				break
			}
		}
	}

	Sort(findUsers, orderField, orderBy)

	//fmt.Println("find:   " + strconv.Itoa(len(findUsers)))
	result, _ := json.Marshal(findUsers)
	w.Write(result)
	return
}

func Sort(users []User, orderField string, orderBy int) {
	switch orderField {
	case "Id":
		sort.Slice(users[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return users[i].Id < users[j].Id
			case -1:
				return users[i].Id > users[j].Id
			default:
				return true
			}
		})

	case "Name":
		sort.Slice(users[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return users[i].Name < users[j].Name
			case -1:
				return users[i].Name > users[j].Name
			default:
				return true
			}
		})

	case "Age":
		sort.Slice(users[:], func(i, j int) bool {
			switch orderBy {
			case 1:
				return users[i].Age < users[j].Age
			case -1:
				return users[i].Age > users[j].Age
			default:
				return true
			}
		})

	default:
		return
	}
}
