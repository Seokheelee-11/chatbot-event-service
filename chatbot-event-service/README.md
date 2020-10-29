post

EventInfo
--------------------------------------------------------------------------------
{
      "eventId": "testevent4",
  "displayName": "string",
    "startDt": "2020-08-09T11:29:54",
  "endDt": "2020-08-30T11:29:54",
    "overLapTF": true,
      "overLapDateType": "ALLTIME",
  "overLapDateCount": 0,
  "rewardTF": true,
  "includeDateTF": false,
  "rewardType": "RANDOMPROB",
     "rewardInfo" : {"starbucks" : 0.1,"tumbler" : 0.3,"mask" : 0.5},
    "resultInfo" : {"starbucks" : {"url" : "www.starbucks.com",
                                    "imgurl" : "www.img.starbucks.com",
                                    "responseMessage" : "스벅 당첨"},
                    "tumbler" : {"url" : "www.tumbzler.com",
                                "imgurl" : "www.img.tumbler.com",
                                "mask" : "www.img.mask.com"}
                    }

}
--------------------------------------------------------------------------------
{
      "eventId": "testevent5",
  "displayName": "테스트이벤트",
    "startDt": "2020-08-09T11:29:54",
  "endDt": "2020-08-30T11:29:54",
    "overLapTF": false,
      "overLapDateType": "ALLTIME",
  "overLapDateCount": 0,
  "includeDateTF": false,
  "rewardTF": true,
  "rewardType": "RANDOM",
     "rewardInfo" : {"starbucks" : 1,"tumbler" : 2,"mask" : 5},
    "resultInfo" : {"starbucks" : {"url" : "www.starbucks.com",
                                    "imgurl" : "www.img.starbucks.com",
                                    "responseMessage" : "스벅 당첨"},
                    "tumbler" : {"url" : "www.tumbzler.com",
                                "imgurl" : "www.img.tumbler.com",
                                "mask" : "www.img.mask.com",
                                  "responseMessage" : "텀블러당첨"},
                     "mask" : {"url" : "www.mask.com",
                                    "imgurl" : "www.img.mask.com",
                                    "responseMessage" : "마스크 당첨"},
                    }

}
--------------------------------------------------------------------------------
{
      "eventId": "testevent9",
  "displayName": "퀴즈테스트이벤트",
    "startDt": "2020-08-09T11:29:54",
  "endDt": "2020-08-30T11:29:54",
    "overLapTF": false,
      "overLapDateType": "MINUTE",
  "overLapDateCount": 1,
  "includeDateTF": true,
  "rewardTF": true,
  "rewardType": "QUIZ",
     "rewardInfo" : {"starbucks" : 0.2,"tumbler" : 0.3,"mask" : 0.5},
    "resultInfo" : {"starbucks" : {"url" : "www.starbucks.com",
                                    "imgurl" : "www.img.starbucks.com",
                                    "responseMessage" : "스벅 당첨"},
                    "tumbler" : {"url" : "www.tumbzler.com",
                                "imgurl" : "www.img.tumbler.com",
                                "mask" : "www.img.mask.com",
                                  "responseMessage" : "텀블러 당첨"},
                     "mask" : {"url" : "www.mask.com",
                                    "imgurl" : "www.img.mask.com",
                                    "responseMessage" : "마스크 당첨"},
                     "default" : {"url" : "www.default.com",
                                    "imgurl" : "www.img.default.com",
                                    "responseMessage" : "퀴즈 정답"}
                    },
"quizAnswer":["파니", "펭수"]

}
--------------------------------------------------------------------------------
{
      "eventId": "testevent10",
  "displayName": "선착순퀴즈테스트이벤트",
    "startDt": "2020-08-09T11:29:54",
  "endDt": "2020-08-30T11:29:54",
    "overLapTF": false,
      "overLapDateType": "MINUTE",
  "overLapDateCount": 1,
  "includeDateTF": true,
  "rewardTF": true,
  "rewardType": "QUIZ_LIMIT",
     "rewardInfo" : {"default" : 1},
    "resultInfo" : {"starbucks" : {"url" : "www.starbucks.com",
                                    "imgurl" : "www.img.starbucks.com",
                                    "responseMessage" : "스벅 당첨"},
                    "tumbler" : {"url" : "www.tumbzler.com",
                                "imgurl" : "www.img.tumbler.com",
                                "mask" : "www.img.mask.com",
                                  "responseMessage" : "텀블러 당첨"},
                     "mask" : {"url" : "www.mask.com",
                                    "imgurl" : "www.img.mask.com",
                                    "responseMessage" : "마스크 당첨"},
                     "default" : {"url" : "www.default.com",
                                    "imgurl" : "www.img.default.com",
                                    "responseMessage" : "퀴즈 정답"}
                    },
"quizAnswer":["파니", "펭수"]

}

POST
EVENTHISTORY
--------------------------------------------------------------------------------
{
  "clnn": "P111111111",
  "eventId": "testevent4"
}
