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
### Search all with index
GET /my_doc/_search
### Search item with specific columns
GET /my_doc/_doc/1?_source=id,name
### Search item exist or not
HEAD /my_doc/_doc/1
### Analyze with specific sentence (standard)
POST /_analyze
```json
{
    "analyzer":"standard",
    "text":"I study in kenny.com"
}
```
### Analyze with specific sentence in a index
POST /my_doc/_analyze
```json
{
    "analyzer":"standard",
    "field": "desc",
    "text":"I study in kenny.com"
}
```


### Elasticsearch Simple Analyzer Example

POST /_analyze
```json
{
    "analyzer": "simple",
    "text": "My name is Peter Parker, I am a Super Hero. I don't like the Criminals"
}
```

The simple analyzer will:
- Convert all text to lowercase
- Remove all punctuation
- Split text on whitespace

Result tokens:
```json
[
    "my", "name", "is", "peter", "parker", "i", "am", "a", "super", "hero",
    "i", "dont", "like", "the", "criminals"
]
```


### Elasticsearch Whitespace Analyzer Example

POST /_analyze
```json
{
    "analyzer": "whitespace",
    "text": "My name is Peter Parker, I am a Super Hero. I don't like the Criminals"
}
```

The whitespace analyzer will:
- Only split text on whitespace
- Preserve case (no lowercase conversion)
- Preserve punctuation

Result tokens:
```json
[
    "My", "name", "is", "Peter", "Parker,", "I", "am", "a", "Super", "Hero.", 
    "I", "don't", "like", "the", "Criminals"
]
```

Note: Unlike the simple analyzer, whitespace analyzer keeps the original case, punctuation, and special characters.


### Elasticsearch Stop Analyzer Example

POST /_analyze
```json
{
    "analyzer": "stop",
    "text": "My name is Peter Parker, I am a Super Hero. I don't like the Criminals"
}
```

The stop analyzer will:
- Convert text to lowercase
- Remove stop words (common words like "is", "am", "the", "a")
- Split on whitespace
- Remove punctuation

Result tokens:
```json
[
    "name", "peter", "parker", "super", "hero", "dont", "like", "criminals"
]
```

Note: Stop words that were removed: "my", "is", "i", "am", "a", "the". The analyzer also converted everything to lowercase and removed punctuation.


### Elasticsearch Keyword Analyzer Example

POST /_analyze
```json
{
    "analyzer": "keyword",
    "text": "My name is Peter Parker, I am a Super Hero. I don't like the Criminals"
}
```

The keyword analyzer will:
- Treat the entire text as a single token
- Preserve case, spaces, and punctuation
- No tokenization occurs

Result token:
```json
[
    "My name is Peter Parker, I am a Super Hero. I don't like the Criminals"
]
```

Note: The keyword analyzer is useful when you want to match exact phrases or when dealing with structured content like IDs, email addresses, or zip codes where you don't want any text analysis to be performed.

### IK_MAX_WORD Analyzer Example

POST /_analyze
```json
{
    "analyzer": "ik_max_word",
    "text": "中华人民共和国国歌"
}
```

The ik_max_word analyzer will:
- Break down text into as many possible words/combinations as possible
- Result example: ["中华人民共和国", "中华人民", "中华", "华人", "人民共和国", "人民", "共和国", "国歌"]

### IK_SMART Analyzer Example

POST /_analyze
```json
{
    "analyzer": "ik_smart",
    "text": "中华人民共和国国歌"
}
```

The ik_smart analyzer will:
- Break down text in the most intelligent way
- Only output the most likely words
- Result example: ["中华人民共和国", "国歌"]



### Elasticsearch Mapping for "shop" Index

Field mapping explanations:

1. **id**
   - Type: `long`
   - For storing large integer values

2. **age**
   - Type: `integer`
   - For storing whole numbers

3. **username**
   - Type: `keyword`
   - Exact match field, not tokenized
   - Good for filtering and aggregations

4. **nickname**
   - Type: `text`
   - Analyzer: `ik_max_word`
   - Full-text search with Chinese language support
   - Will be broken into tokens for searching

5. **money**
   - Type: `float`
   - For storing decimal numbers

6. **desc**
   - Type: `text`
   - Analyzer: `ik_max_word`
   - Full-text search with Chinese language support
   - Will be broken into tokens for searching

7. **sex**
   - Type: `byte`
   - Small integer values (-128 to 127)

8. **birthday**
   - Type: `date`
   - For storing date values

9. **face**
   - Type: `text`
   - `index: false` - This field is not searchable
   - Typically used for storing URLs or paths that don't need to be searched



### Elasticsearch Search Query Explanation

#### Endpoint
```
GET /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "程式"
    }
  }
}
```

#### Explanation:
- This query searches in the `shop` index
- Uses the `match` query type which is best for full-text search
- Searches in the `desc` field
- Looks for documents containing the term "程式"
- Will return all documents where the description contains "程式", with relevance scoring
- The `match` query will analyze the search term using the same analyzer that was used during indexing (in this case, ik_max_word)

#### Expected Results:
- Returns matching documents sorted by relevance score
- Each result will include the full document and metadata
- Results will include any documents where the description contains the word "程式" or its analyzed tokens

### Elasticsearch Exists Query Explanation

#### Syntax
```json
GET /index/_search
{
  "query": {
    "exists": {
      "field": "field_name"
    }
  }
}
```

#### Purpose
- Finds documents where the specified field exists
- Returns documents that have any non-null value in the specified field
- Useful for finding documents with or without certain fields

#### Example
```json
GET /shop/_search
{
  "query": {
    "exists": {
      "field": "nickname"
    }
  }
}
```

#### Key Points:
- Does NOT search for specific values
- Only checks if field exists and is not null
- Common uses:
   - Finding documents with missing fields
   - Quality control of data
   - Filtering out incomplete records

#### Note:
The exists query cannot be used to search for specific values like "小". For value searches, use:
- match query
- term query
- wildcard query


### Elasticsearch Source Filtering Query Explanation

#### Endpoint
```
GET /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match_all": {},
  },
  "_source": ["id", "nickname", "age"]
}
```

#### Explanation:
- `match_all`: Returns all documents in the index
- `_source`: Filters which fields to return in the results
   - Only returns: `id`, `nickname`, and `age`
   - Other fields will be excluded from results
   - Reduces response size and network bandwidth

#### Benefits:
- More efficient than retrieving all fields
- Returns only necessary data
- Useful for large documents where only specific fields are needed
- Improves query performance


```json

```
```json

```
```json

```
```json

```
```json

```
```json

```
```json

```