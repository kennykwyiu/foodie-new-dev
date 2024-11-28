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

