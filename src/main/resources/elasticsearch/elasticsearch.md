PUT - http://192.168.74.137:9200/index_temp

```json
{
    "settings": {
        "index": {
            "number_of_shards": "2",
            "number_of_replicas": "0"
        }
    }
}
```

PUT - http://192.168.74.137:9200/index_mapping

```json
{
    "mappings": {
        "properties": {
            "realname": {
                "type": "text",
                "index": true
            },
            "username": {
                "type": "keyword",
                "index": false
            }
        }
    }
}
```


PUT /index_str
```json

{
  "mappings": {
    "properties": {
      "realname": {
        "type": "text",
        "index": true
      },
      "username": {
        "type": "keyword",
        "index": false
      }
    }
  }
}
```
POST /index_str/_mapping
```json
{
  "properties": {
    "name": {
      "type": "long"
    }
  }
}
```

GET /index_mapping/_analyze
```json
{
  "field": "realname",
  "text": "kenny is good"
}
```

POST /index_str/_mapping
```json
{
  "properties": {
    "id": {
      "type": "long"
    },
    "age": {
      "type": "integer"
    },
    "nickname": {
      "type": "keyword"
    },
    "money1": {
      "type": "float"
    },
    "money2": {
      "type": "double"
    },
    "sex": {
      "type": "byte"
    },
    "score": {
      "type": "short"
    },
    "is_teenager": {
      "type": "boolean"
    },
    "birthday": {
      "type": "date"
    },
    "relationship": {
      "type": "object"
    }
  }
}
```

PUT /my_doc/_doc/1
```json
{
  "id": 1001,
  "name": "kenny-1",
  "desc": "kenny is very good, Kenny非常厲害!",
  "create_date": "2019-12-24"
}
```

```json
{
    "id": 1002,
    "name": "kenny-2",
    "desc": "kenny is fashion, Kenny非常時尚!",
    "create_date": "2019-12-24"
}
```

```json
{
    "id": 1003,
    "name": "kenny-4",
    "desc": "kenny is niubility, Kenny非常非常牛逼!",
    "create_date": "2019-12-26"
}
```
```json
{
    "id": 1004,
    "name": "kenny-4",
    "desc": "kenny is good--!",
    "create_date": "2019-12-27"
}
```
```json
{
    "id": 1005,
    "name": "kenny-5",
    "desc": "kenny is 非常厲害!",
    "create_date": "2019-12-28"
}
```
```json
{
    "id": 1006,
    "name": "kenny-6",
    "desc": "kenny網是非常厲害網站!",
    "create_date": "2019-12-29"
}
```
```json
{
    "id": 1007,
    "name": "kenny-7",
    "desc": "kenny網是非常非常牛逼的網站!",
    "create_date": "2019-12-30"
}
```
### Delete a row
DELETE /my_doc/_doc/5
### Search item
GET /my_doc/_doc/1


```json
```
```json

```