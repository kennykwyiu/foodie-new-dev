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

### Elasticsearch Query with Sort Explanation

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
  "sort": [
    {
      "age": "asc"
    }
  ]
}
```

#### Detailed Explanation:
1. Query Phase:
    - Executes `match` query for "專門" in desc field
    - Performs full-text search with analysis

2. Sort Phase:
    - Sorts results by age in ascending order
    - Overrides default relevance score sorting

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE description LIKE '%專門%'
ORDER BY age ASC;
```

#### Key Points:
- Default sort is by relevance score (_score)
- Explicit sort overrides default scoring order
- `asc`: ascending order (lowest to highest)
- Can also use `desc` for descending order
- Multiple sort criteria possible

### Elasticsearch Multiple Sort Fields Explanation

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
  "sort": [
    {
      "money": "asc"
    },
    {
      "age": "asc"
    }
  ]
}
```

#### Detailed Explanation:
1. Primary Sort:
    - First sorts by money in ascending order
    - Lowest money values appear first

2. Secondary Sort:
    - When money values are equal
    - Then sorts by age in ascending order
    - Acts as a tiebreaker

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE description LIKE '%專門%'
ORDER BY money ASC, age ASC;
```

#### Key Points:
- Sort order is applied sequentially
- Second sort only affects records with equal first sort values
- Both fields use ascending order (lowest to highest)
- Can mix ascending and descending if needed

### Elasticsearch Sort by Keyword Field Explanation

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
  "sort": [
    {
      "username": "desc"
    }
  ]
}
```

#### Detailed Explanation:
1. Keyword Field Sorting:
    - Sorts by username in descending order
    - Uses exact values (keyword type)
    - Case sensitive
    - Alphabetical order (Z to A)

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE description LIKE '%專門%'
ORDER BY username DESC;
```

#### Key Points:
- Username is a keyword field (not analyzed)
- Exact string comparison
- Descending order means:
    - z comes before a
    - zhangsan comes before lisi
- More efficient than sorting on analyzed text fields


### Elasticsearch Multi-Field Mapping Explanation

#### Index Mapping Structure
```json
{
  "properties": {
    "id": {
      "type": "long"
    },
    "nickname": {
      "type": "text",
      "analyzer": "ik_max_word",
      "fields": {
        "keyword": {
          "type": "keyword"
        }
      }
    }
  }
}
```

#### Field Explanations:

1. `id` Field:
    - Type: `long`
    - For storing numeric IDs
    - Supports range queries and sorting

2. `nickname` Field:
    - Primary mapping:
        - Type: `text`
        - Uses `ik_max_word` analyzer for Chinese text analysis
        - Good for full-text search
    - Sub-field `nickname.keyword`:
        - Type: `keyword`
        - Stores exact string value
        - Good for aggregations and sorting
        - Accessible via `nickname.keyword`

#### Usage Examples:
- Full-text search: Use `nickname`
- Exact match: Use `nickname.keyword`
- Sorting: Use `nickname.keyword`


### Elasticsearch Sort by Keyword Sub-field Explanation

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "專門"
    }
  },
  "sort": [
    {
      "nickname.keyword": "asc"
    }
  ]
}
```

#### Detailed Explanation:
1. Query Phase:
    - Searches for "專門" in desc field
    - Full-text search with analysis

2. Sort Phase:
    - Uses the keyword sub-field of nickname
    - Sorts by exact string value
    - Ascending order (A to Z)
    - Case sensitive sorting

#### Key Points:
- `nickname.keyword`: References the keyword sub-field
- Better for sorting than analyzed text field
- Preserves original string for exact sorting
- Useful for both Chinese and English characters
- Maintains consistent sort order

### Elasticsearch Highlight Query Explanation

#### Query Structure
```json
{
  "query": {
    "match": {
      "desc": "專門"
    }
  },
  "highlight": {
    "pre_tags": ["<span>"],
    "post_tags": ["</span>"],
    "fields": {
      "desc": {}
    }
  }
}
```

#### Detailed Explanation:
1. Query Phase:
    - Searches for "專門" in desc field
    - Performs full-text search

2. Highlight Configuration:
    - `pre_tags`: Opening HTML tag for highlighted text
    - `post_tags`: Closing HTML tag for highlighted text
    - `fields`: Specifies which fields to highlight
    - Will wrap matched terms with `<span></span>`

#### Example Result:
```json
{
  "hits": {
    "_source": {
      "desc": "我是趙六，專門教授式交易系統開發"
    },
    "highlight": {
      "desc": ["我是趙六，<span>專門</span>教授式交易系統開發"]
    }
  }
}
```

#### Use Cases:
- Search result highlighting
- Visual feedback for matching terms
- Improved user experience
- Shows context of matches

### Elasticsearch Match All Query with Pagination Explanation

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
  "from": 0,
  "size": 10
}
```

#### Detailed Explanation:
- `match_all`: Returns all documents in the index
- `from`: Starting position (offset) of results
- `size`: Maximum number of results to return

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
LIMIT 10 OFFSET 0;
```

#### Key Points:
- Default sorting is by relevance score
- First page of results (from: 0)
- Returns maximum 10 documents
- Most basic form of Elasticsearch query
- Useful for:
    - Testing index content
    - Simple pagination
    - Getting initial data sample


### Elasticsearch Match All Query with Offset Pagination

#### Query Structure
```json
{
  "query": {
    "match_all": {}
  },
  "from": 30,
  "size": 10
}
```

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
LIMIT 10 OFFSET 30;
```

#### Key Points:
- Skips first 30 results
- Returns results 31-40
- Fourth page of results (assuming 10 per page)
- Caution: Large offsets can be inefficient
- Better to use search_after for deep pagination


### Elasticsearch Settings with Defaults Explanation

#### Endpoint
```
GET /shop/_settings?include_defaults=true
```

#### Purpose:
- Shows all index settings including defaults
- Displays both custom and system default values
- Useful for understanding complete index configuration

#### Key Settings Usually Shown:
1. Index Settings:
    - `number_of_shards`
    - `number_of_replicas`
    - `max_result_window` (default: 10000)
    - `refresh_interval`

2. Analysis Settings:
    - Default analyzers
    - Custom analyzers
    - Token filters

3. Allocation Settings:
    - Tier preference
    - Routing rules
    - Shard allocation

4. Other Defaults:
    - Query limits
    - Mapping limits
    - Index metadata

### Elasticsearch Update Max Result Window Setting

#### Endpoint
```
PUT /shop/_settings
```

#### Request Body
```json
{
  "index.max_result_window": 20000
}
```

#### Explanation:
- Updates maximum number of results that can be returned
- Default value is 10000
- New setting allows up to 20000 results
- Affects pagination limits
- Impacts from/size query parameters

#### Important Notes:
- Increasing beyond 10000 can:
    - Impact performance
    - Increase memory usage
    - Slow down queries
- Consider using search_after for deep pagination
- Changes take effect immediately


### Elasticsearch Scroll API Explanation

#### Endpoint
```
POST /shop/_search?scroll=1m
```

#### Query Structure
```json
{
  "query": {
    "match_all": {},
  },
  "sort": ["_doc"],
  "size": 5
}
```

#### Detailed Explanation:
1. Scroll Parameters:
    - `scroll=1m`: Keeps search context alive for 1 minute
    - `size`: Returns 5 documents per batch
    - `sort`: ["_doc"] most efficient sort order for scrolling

2. Usage:
    - Returns first batch of results
    - Includes a scroll_id for next batch
    - Must use scroll_id to get subsequent batches
    - Good for processing large result sets

#### Example Next Request:
POST /_search/scroll
```json
{
  "scroll": "1m",
  "scroll_id": "<scroll_id_from_previous_response>"
}
```

#### Benefits over From/Size:
- Memory efficient
- Consistent results
- Good for large datasets
- No 10k document limit

### Elasticsearch Multi Get (mget) API Explanation

#### Endpoint
```
POST /shop/_mget
```

#### Query Structure
```json
{
  "ids": [
    "1001",
    "1003"
  ]
}
```

#### Detailed Explanation:
- Retrieves multiple documents in a single request
- Fetches documents by their IDs
- More efficient than multiple GET requests
- Returns documents in order of requested IDs

#### Equivalent SQL Query
```sql
SELECT *
FROM shop
WHERE id IN ('1001', '1003');
```

#### Key Benefits:
- Reduces network roundtrips
- Batch retrieval of documents
- Maintains order of requested documents
- Better performance than individual gets
- Works across multiple indices if needed

### Elasticsearch Bulk API Example

#### Endpoint
```
POST /_bulk
```

#### Request Body
```json
{"create": {"_index": "shop2", "_type": "_doc", "_id": "2001"}}
{"id": "2001", "nickname": "name2001"}
{"create": {"_index": "shop2", "_type": "_doc", "_id": "2002"}}
{"id": "2002", "nickname": "name2002"}
{"create": {"_index": "shop2", "_type": "_doc", "_id": "2003"}}
{"id": "2003", "nickname": "name2003"}
```

#### Key Points:
- Each operation requires two lines:
    1. Action metadata line
    2. Request body line
- No commas between lines
- Each line must end with newline character (\n)
- Bulk operations are faster than individual requests
- Can combine different operations (create/update/delete)

### Elasticsearch Bulk API Error Explanation

#### Current Format (Incorrect)
```json
{"create": {"_index": "shop2", "_id": "2001"}}{"id": "2001", "nickname": "name2001"}{"create": {"_index": "shop2", "_id": "2002"}}{"id": "2002", "nickname": "name2002"}{"create": {"_index": "shop2", "_id": "2003"}}{"id": "2003", "nickname": "name2003"}
```

#### Correct Format (Each line must end with newline)
```json
{"create": {"_index": "shop2", "_id": "2001"}}
{"id": "2001", "nickname": "name2001"}
{"create": {"_index": "shop2", "_id": "2002"}}
{"id": "2002", "nickname": "name2002"}
{"create": {"_index": "shop2", "_id": "2003"}}
{"id": "2003", "nickname": "name2003"}
```

#### Key Requirements:
- Each action and its corresponding data must be on separate lines
- Each line must end with a newline character (\n)
- No spaces or commas between lines
- Content-Type header should be 'application/x-ndjson'

### Elasticsearch Bulk API with Index in URL Explanation

#### Endpoint
```
POST /shop2/_bulk
```

#### Correct Format (Each line must end with newline)
```json
{"create": { "_id": "2004"}}
{"id": "2004", "nickname": "name2004"}
{"create": { "_id": "2005"}}
{"id": "2005", "nickname": "name2005"}
{"create": { "_id": "2003"}}
{"id": "2003", "nickname": "name2003"}
```

#### Key Points:
- Index name (shop2) is specified in URL
- No need to specify _index in action metadata
- Each operation still needs two lines
- Must have newline characters between operations
- No spaces or commas between lines

### Elasticsearch Bulk Index Operation Response Explanation

#### Request (Bulk Index Operations)
```json
{"index": { "_id": "2004"}} 
{"id": "2004", "nickname": "index2004"}
{"index": { "_id": "2007"}}
{"id": "2007", "nickname": "name2007"}
{"index": { "_id": "2008"}}
{"id": "2008", "nickname": "name2008"}
```

#### Response Analysis
1. Overall Status:
    - `errors`: false (all operations successful)
    - `took`: 367ms (total processing time)

2. Individual Results:
    - Document 2004:
        - Status: 200 (OK)
        - Result: "updated" (document already existed)
        - Version: 2 (second version of document)

    - Documents 2007 & 2008:
        - Status: 201 (Created)
        - Result: "created" (new documents)
        - Version: 1 (first version)

#### Key Points:
- `index` operation creates or updates documents
- Different from `create` which only creates new documents
- Status 200 indicates update
- Status 201 indicates new creation
- Successful shards indicate replication status

### Elasticsearch Bulk Update Operations Explanation

#### Request Format
```json
{"update": { "_id": "2004"}}
{"doc": { "id": "3304"}}
{"update": { "_id": "2007"}}
{"doc": {"nickname": "nameupdate"}}
```

#### Explanation:
1. First Update Operation:
    - Updates document with ID "2004"
    - Changes the "id" field to "3304"
    - Partial update (other fields remain unchanged)

2. Second Update Operation:
    - Updates document with ID "2007"
    - Changes the "nickname" field to "nameupdate"
    - Partial update (other fields remain unchanged)

#### Key Points:
- `update` operation allows partial document updates
- `doc` specifies the fields to update
- Each update requires two lines:
    1. Update action metadata
    2. Update document data
- Must include newline characters between lines

### Elasticsearch Bulk Delete Operations Explanation

#### Request Format
```json
{"delete": { "_id": "2004"}}
{"delete": { "_id": "2007"}}
```

#### Explanation:
1. First Delete Operation:
    - Deletes document with ID "2004"
    - No second line needed for delete operations

2. Second Delete Operation:
    - Deletes document with ID "2007"
    - No second line needed for delete operations

#### Key Points:
- `delete` operation only requires one line per document
- No document body needed for delete operations
- Multiple deletes can be combined in one bulk request
- Each delete line must end with newline character
- More efficient than individual delete requests

### Elasticsearch Mixed Bulk Operations Explanation

#### Request Format
```json
{"delete": { "_id": "2001"}}
{"delete": { "_id": "2003"}}
{"create": {"_index": "shop2", "_id": "8008"}}
{"id": "2001", "nickname": "name8008"}
{"update": { "_id": "2002"}}
{"doc":{"id": "2222"}}
```

#### Operations Breakdown:
1. Delete Operations:
    - Delete document ID "2001"
    - Delete document ID "2003"
    - Single line per delete operation

2. Create Operation:
    - Create new document with ID "8008"
    - Contains id and nickname fields
    - Requires two lines (metadata + data)

3. Update Operation:
    - Update document ID "2002"
    - Changes id field to "2222"
    - Partial update with doc parameter

#### Key Points:
- Multiple operation types in one bulk request
- Each operation follows its own format rules
- Delete: one line only
- Create/Update: requires two lines
- More efficient than separate API calls


