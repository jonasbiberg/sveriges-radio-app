Sveriges radio web app
=========

A restful webservice using the Sveriges radio open api

```
https://sverigesradio.se/api/documentation/v2/index.html
```


Build and start
---------------
```
mvn spring-boot:run
```

Api:s
---------------

Get artists grouped by record label by channel

```
http://localhost:8080/api/v1/channels/{channelId}/recordlabels?limit={limit}&fromDate={fromDate}&toDate={toDate}
```

Parameters
----------
- channelId: Then channel id at Sveriges radio for example 164 (P3)
- limit: Number of songs (default 100)
- fromDate: From date (default today)
- toDate: To date (default tomorrow)

Example Request
-----
```
curl --location --request GET 'http://localhost:8080/api/v1/channels/164/recordlabels?fromDate=2020-01-01&toDate=2020-01-02&limit=100'
```

Example response
------

```
[
    {
        "artists": [
            {
                "name": "Arxx"
            },
            {
                "name": "Esther"
            },
            {
                "name": "Habibi"
            },
            {
                "name": "Hotel Lux"
            },
            {
                "name": "Lite Lisa"
            },
            {
                "name": "Morabeza Tobacco"
            },
            {
                "name": "Yowl"
            }
        ],
        "name": ""
    },
    {
        "artists": [
            {
                "name": "Alice Boman"
            }
        ],
        "name": "Adrian Recordings"
    },
    {
        "artists": [
            {
                "name": "Cherrie"
            }
        ],
        "name": "Araweelo"
    },
    {
        "artists": [
            {
                "name": "Arizona Zervas"
            }
        ],
        "name": "Arizona Zervas"
    },
    {
        "artists": [
            {
                "name": "Ljung"
            }
        ],
        "name": "Astronaut Recordings"
    }
	...
]
```