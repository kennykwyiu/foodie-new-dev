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
POST /shop/_search
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
POST /index/_search
```json

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
POST /shop/_search
```json
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
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match_all": {}
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



### Elasticsearch Pagination Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match_all": {}
  },
  "_source": ["id", "nickname", "age"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `match_all`: Returns all documents in the index
- `_source`: Filters returned fields to only:
   - `id`
   - `nickname`
   - `age`
- `from`: Starting point for results (0 = first result)
- `size`: Number of results to return (10 documents)

#### Pagination Parameters:
- `from`: Offset from the first result (0-based)
- `size`: Maximum number of results to return
- Default size is usually 10 if not specified
- Common usage for implementing pagination in applications

#### Example:
- `from: 0, size: 10` returns results 1-10
- `from: 10, size: 10` would return results 11-20
- And so on...


### Elasticsearch Terms Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "terms": {
      "desc": ["開發", "交易"]
    }
  },
  "_source": ["id", "nickname", "desc"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `terms`: Matches documents that contain one or more exact terms
    - Looking for either "開發" OR "交易" in the desc field
    - Terms query is not analyzed (exact match)
    - Case sensitive and requires exact matches

#### Query Components:
- `_source`: Only returns specified fields:
    - id
    - nickname
    - desc
- `from`: Starts from first result (0)
- `size`: Returns up to 10 documents

#### Note:
- Different from `match` query as it doesn't analyze the search terms
- Documents matching either term will be returned
- No relevance scoring based on term frequency


### Elasticsearch Match Phrase Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match_phrase": {
      "desc": {
        "query": "程式 交易"
      }
    }
  },
  "_source": ["id", "nickname", "desc"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `match_phrase`: Matches exact phrases in the specified order
    - Looks for "程式 交易" as a complete phrase
    - Words must appear together and in the same order
    - More strict than regular `match` query

#### Query Components:
- `_source`: Returns only:
    - id
    - nickname
    - desc
- `from`: Starts from first result
- `size`: Returns up to 10 documents

#### Difference from Terms Query:
- Terms query matches individual words independently
- Match phrase requires words to be adjacent and in order
- Better for exact phrase matching


### Elasticsearch Match Phrase Query with Slop Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match_phrase": {
      "desc": {
        "query": "提供 交易",
        "slop": 2
      }
    }
  },
  "_source": ["id", "nickname", "desc"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `match_phrase`: Matches phrases with flexibility
- `slop`: Allows words to be this many positions apart
    - slop: 2 means words can have up to 2 other words between them
    - Will match "提供程式交易" or "提供自動交易"
    - More flexible than exact phrase matching
    - Order still matters, but words don't need to be adjacent

#### Query Components:
- `_source`: Returns specified fields only
- `from` and `size`: Standard pagination parameters
- Will match documents where:
    - Both "提供" and "交易" appear
    - Words are within 2 positions of each other
    - Words appear in the specified order

### Elasticsearch Match Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "交易系統"
    }
  },
  "_source": ["id", "nickname", "desc"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `match`: Standard full-text search query
    - Analyzes the search terms "交易系統"
    - Matches documents containing either or both terms
    - Uses relevance scoring
    - More flexible than `match_phrase`
    - Order doesn't matter
    - Terms don't need to be adjacent

#### Differences from Previous Queries:
- More flexible than `match_phrase`
- No slop parameter needed
- Will match documents containing either "交易" OR "系統"
- Results sorted by relevance score

### Elasticsearch Match Query with AND Operator Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": {
        "query": "交易系統",
        "operator": "and"
      }
    }
  },
  "_source": ["id", "nickname", "desc"],
  "from": 0,
  "size": 10
}
```

#### Explanation:
- `match` with `operator: "and"`:
    - Requires ALL terms to be present
    - Must contain both "交易" AND "系統"
    - Different from default OR behavior
    - Still analyzes the terms
    - Order doesn't matter
    - Terms don't need to be adjacent

#### Key Differences:
- More restrictive than default match query
- Less restrictive than match_phrase
- Documents must contain all specified terms
- Better for filtering when all terms are required

#### Default OR Operator
// This is default, can be omitted
```json
{
  "query": {
    "match": {
      "desc": {
        "query": "交易系統",
        "operator": "or"  
      }
    }
  }
}
```
- Matches if ANY term is found
- Will return documents with either "交易" OR "系統"
- More results than AND operator
- Better for broader searches
- Higher recall, lower precision

### Elasticsearch Minimum Should Match Explanation

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": {
        "query": "孫七喜歡交易",
        "minimum_should_match": "2"
      }
    }
  }
}
```

#### Number Format Examples:
```
minimum_should_match: "2"      // Must match at least 2 terms
minimum_should_match: "3"      // Must match at least 3 terms
minimum_should_match: "1"      // Must match at least 1 term
```

#### Percentage Format Examples:
```
minimum_should_match: "75%"    // Must match 75% of terms
minimum_should_match: "50%"    // Must match half of terms
minimum_should_match: "25%"    // Must match quarter of terms
```

#### In This Query:
- Search terms: "孫七喜歡交易" (4 terms)
- `minimum_should_match: "2"`:
    - Must match at least 2 of these terms
    - More flexible than AND operator
    - More restrictive than OR operator
    - Good balance between precision and recall

### Elasticsearch IDs Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "ids": {
      "values": ["1001", "1003"]
    }
  },
  "_source": ["id", "nickname", "desc"]
}
```

#### Explanation:
- `ids`: Retrieves documents based on their IDs
    - Simple and efficient lookup query
    - Fetches documents with specified IDs only
    - Order of IDs doesn't affect relevance scoring
    - Exact match only (no analysis or tokenization)

#### Query Components:
- `values`: Array of document IDs to retrieve
- `_source`: Filters returned fields to:
    - id
    - nickname
    - desc

#### Use Cases:
- Direct document retrieval by ID
- Batch document retrieval
- More efficient than multiple GET requests


### Elasticsearch Multi Match Query Explanation

#### Endpoint
```
POSt /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "multi_match": {
      "query": "程式",
      "fields": ["desc", "nickname"]
    }
  },
  "_source": ["id", "nickname", "age", "desc"]
}
```

#### Explanation:
- `multi_match`: Searches multiple fields with one query
    - Searches for "程式" in both:
        - desc field
        - nickname field
    - Same search term applied to different fields
    - Returns documents matching in either field

#### Query Components:
- `query`: The search term ("程式")
- `fields`: Array of fields to search in
- `_source`: Returns only specified fields:
    - id
    - nickname
    - age
    - desc

#### Benefits:
- More efficient than multiple single-field queries
- Combines results from multiple fields
- Relevance scoring considers matches in any field


### Elasticsearch Multi Match Query with Boost Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "multi_match": {
      "query": "小芳喜歡專精",
      "fields": ["desc", "nickname^10"]
    }
  },
  "_source": ["id", "nickname", "age", "desc"]
}
```

#### Explanation:
- `multi_match`: Searches multiple fields
    - Search terms: "小芳喜歡專精"
    - Fields searched:
        - desc (normal weight)
        - nickname (boosted by 10)

#### Field Boost (^10):
- `nickname^10`: Boosts nickname field importance
    - Matches in nickname field are 10x more important
    - Affects relevance scoring
    - Higher scores for matches in nickname vs desc
    - Prioritizes documents where "小芳" appears in nickname

#### Query Components:
- `_source`: Returns specified fields only
- Boosting useful when:
    - Some fields are more important
    - Need to prioritize matches in certain fields
    - Want to influence relevance scoring

### Elasticsearch Boolean Query with Multiple Conditions Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "multi_match": {
            "query": "專精開發",
            "fields": ["desc", "nickname"]
          }
        },
        {
          "term": {
            "sex": 0
          }
        },
        {
          "term": {
            "birthday": "1999-01-14"
          }
        }
      ]
    }
  }
}
```

#### Explanation:
- `bool`: Combines multiple query conditions
- `must`: All conditions must match (AND logic)
    1. `multi_match`: Searches for "專精開發" in:
        - desc field
        - nickname field
    2. `term`: Exact match for sex = 0 (female)
    3. `term`: Exact match for birthday = "1999-01-14"

#### Query Components:
- All three conditions must be satisfied
- `_source`: Returns only specified fields:
    - id
    - nickname
    - sex
    - desc
    - birthday

#### Use Case:
- Complex search with multiple criteria
- Combination of full-text search and exact matches
- Precise filtering of results
#### Equivalent SQL Query
```sql
SELECT id, nickname, sex, description, birthday
FROM shop
WHERE (description LIKE '%專精%' OR description LIKE '%開發%'
   OR nickname LIKE '%專精%' OR nickname LIKE '%開發%')
  AND sex = 0
  AND birthday = '1999-01-14';
```

#### Key Differences:
- SQL uses LIKE for text search (less sophisticated)
- Elasticsearch provides better full-text search capabilities
- SQL is exact matching
- Elasticsearch includes relevance scoring

### Elasticsearch Boolean Query with Should Conditions Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "bool": {
      "should": [
        {
          "multi_match": {
            "query": "專精開發",
            "fields": ["desc", "nickname"]
          }
        },
        {
          "term": {
            "sex": 0
          }
        },
        {
          "term": {
            "birthday": "1999-01-14"
          }
        }
      ]
    }
  }
}
```

#### Equivalent SQL Query
```sql
SELECT id, nickname, sex, description, birthday
FROM shop
WHERE description LIKE '%專精%' OR description LIKE '%開發%'
   OR nickname LIKE '%專精%' OR nickname LIKE '%開發%'
   OR sex = 0
   OR birthday = '1999-01-14';
```

#### Key Differences from Previous Must Query:
- `should`: Any condition can match (OR logic)
- Documents matching more conditions get higher scores
- More flexible than `must`
- Returns more results than `must`
- Similar to SQL's OR operator

#### Scoring:
- Documents matching multiple conditions score higher
- More matches = higher relevance
- Unlike SQL, maintains relevance scoring

### Elasticsearch Boolean Query with Must Not Conditions Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "bool": {
      "must_not": [
        {
          "multi_match": {
            "query": "專精開發",
            "fields": ["desc", "nickname"]
          }
        },
        {
          "term": {
            "sex": 0
          }
        },
        {
          "term": {
            "birthday": "1999-01-15"
          }
        }
      ]
    }
  }
}
```

#### Equivalent SQL Query
```sql
SELECT id, nickname, sex, description, birthday
FROM shop
WHERE (description NOT LIKE '%專精%' AND description NOT LIKE '%開發%'
   AND nickname NOT LIKE '%專精%' AND nickname NOT LIKE '%開發%')
   AND sex != 0
   AND birthday != '1999-01-15';
```

#### Key Points:
- `must_not`: Excludes documents matching any condition (NOT logic)
- Excludes documents where:
    - desc or nickname contains "專精" or "開發"
    - sex equals 0
    - birthday equals "1999-01-15"
- Similar to SQL's NOT and != operators
- Returns documents that don't match any of the conditions

### Elasticsearch Complex Boolean Query Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "bool": {
      "must": [
        { "match": { "desc": "諮詢" } },
        { "match": { "nickname": "孫七" } }
      ],
      "should": [
        { "match": { "sex": 1 } }
      ],
      "must_not": [
        { "term": { "birthday": "1999-01-15" } }
      ]
    }
  }
}
```

#### Equivalent SQL Query
```sql
SELECT id, nickname, sex, description, birthday
FROM shop
WHERE description LIKE '%諮詢%'
  AND nickname = '孫七'
  AND birthday != '1999-01-15'
ORDER BY 
  CASE WHEN sex = 1 THEN 1 ELSE 0 END DESC;
```

#### Explanation:
- `must`: Documents MUST match these conditions (AND)
    - desc contains "諮詢"
    - nickname is "孫七"
- `should`: Boosts score if matches (optional)
    - Higher score if sex = 1
- `must_not`: Documents must NOT match (NOT)
    - Excludes birthday = "1999-01-15"

#### Key Differences from SQL:
- Elasticsearch provides relevance scoring
- `should` clause affects ranking but not filtering
- SQL ORDER BY approximates should behavior


### Elasticsearch Boolean Query with Boost Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "desc": {
              "query": "專精",
              "boost": 2
            }
          }
        },
        {
          "match": {
            "desc": {
              "query": "教授",
              "boost": 10
            }
          }
        }
      ]
    }
  }
}
```

#### Equivalent SQL Query
```sql
SELECT id, nickname, description
FROM shop
WHERE description LIKE '%專精%' 
   OR description LIKE '%教授%'
ORDER BY 
  CASE 
    WHEN description LIKE '%教授%' THEN 10
    WHEN description LIKE '%專精%' THEN 2
    ELSE 0 
  END DESC
LIMIT 10 OFFSET 0;
```

#### Key Points:
- `boost`: Affects relevance scoring
    - "教授" matches get 10x boost
    - "專精" matches get 2x boost
    - Higher boost = higher ranking in results
- `should`: Either term can match
- Empty `sort`: Uses relevance scoring
- `from` and `size`: Standard pagination
- SQL equivalent is approximate (SQL can't exactly replicate Elasticsearch scoring)

### Elasticsearch Query with Post Filter Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "專門"
    }
  },
  "post_filter": {
    "range": {
      "money": {
        "gt": 1000,
        "lt": 8000
      }
    }
  }
}
```

#### Detailed Explanation:
1. Query Phase:
    - First executes `match` query on "desc" field
    - Finds documents containing "專門"
    - Calculates relevance scores

2. Post Filter Phase:
    - Applied after query execution
    - Filters results by money range
    - Only keeps documents where:
        - money > 1000
        - money < 8000
    - Doesn't affect relevance scoring

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE description LIKE '%專門%'
  AND money > 1000 
  AND money < 8000
ORDER BY relevance_score DESC;
```

#### Key Differences:
- Post filter vs WHERE clause:
    - Post filter applies after scoring
    - Doesn't affect relevance calculation
    - More efficient for faceted search
- Elasticsearch scoring:
    - Provides relevance ranking
    - Better full-text search capabilities
    - More sophisticated than SQL LIKE


### Elasticsearch Range Query Operators

#### All Range Operators
```json
{
  "range": {
    "field_name": {
      "gt":  100,     // greater than
      "gte": 100,     // greater than or equal to
      "lt":  200,     // less than
      "lte": 200      // less than or equal to
    }
  }
}
```

#### Examples with Money Range
```json
{
  "post_filter": {
    "range": {
      "money": {
        "gte": 1000,  // money >= 1000
        "lte": 8000   // money <= 8000
      }
    }
  }
}
```

#### Equivalent SQL Comparisons
```sql
-- gt (>)
WHERE money > 1000

-- gte (>=)
WHERE money >= 1000

-- lt (<)
WHERE money < 8000

-- lte (<=)
WHERE money <= 8000

-- Combined
WHERE money >= 1000 AND money <= 8000
```

#### Common Use Cases:
- Price ranges
- Date ranges
- Numeric value filtering
- Age restrictions
- Score thresholds

### Elasticsearch Query with Post Filter Term Explanation

#### Endpoint
```
POST /shop/_search
```

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "專門"
    }
  },
  "post_filter": {
    "term": {
      "sex": 1
    }
  }
}
```

#### Detailed Explanation:
1. Query Phase:
    - Executes `match` query for "專門" in desc field
    - Calculates relevance scores
    - Full-text search with analysis

2. Post Filter Phase:
    - Applied after query execution
    - Filters for exact match sex = 1 (male)
    - Doesn't affect relevance scoring

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE description LIKE '%專門%'
  AND sex = 1
ORDER BY relevance_score DESC;
```

#### Key Points:
- Post filter applies after scoring
- Term query requires exact match
- More efficient than including in main query when:
    - Need to maintain original relevance scores
    - Doing faceted search
    - Filter is optional or dynamic




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