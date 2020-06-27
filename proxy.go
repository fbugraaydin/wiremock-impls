package main

import (
	"bytes"
	"encoding/json"
	"github.com/gin-gonic/gin"
	"net/http"
)

var router *gin.Engine

const WireMockUrl = "http://localhost:8091/wmUrl";

func main() {
	router = gin.Default()
	initializeRouters()
	router.Run(":8090")
}

func initializeRouters() {
	router.POST("/api",proxyIt)
	router.OPTIONS("/api",proxyIt)
}

func proxyIt(c *gin.Context){
	if c.Request.Method == "OPTIONS" {
		c.Header("Allow", "POST, OPTIONS")
		c.Header("Access-Control-Allow-Origin", "*")
		c.Header("Access-Control-Allow-Headers", "origin, content-type, accept")
		c.Header("Content-Type", "application/json")
		c.Status(http.StatusOK)
	} else if c.Request.Method == "POST" {
		var req map[string]interface{}
		var res map[string]interface{}

		c.BindJSON(&req)
		body, _ := json.Marshal(req)
		response, _ := http.Post(WireMockUrl, "application/json", bytes.NewBuffer(body))

		json.NewDecoder(response.Body).Decode(&res)
		c.JSON(http.StatusOK, res)
	}
}