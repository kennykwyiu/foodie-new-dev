package com.test;

import com.kenny.Application;
import com.kenny.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EsTest {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * It is not recommended to use ElasticsearchTemplate for index management (creating, updating mappings, deleting indices)
     * Indices are like databases or tables in a database. We typically don't frequently create, modify, or delete databases/tables through Java code
     * We only perform CRUD operations on the data
     * Similarly in ES, we should primarily use ElasticsearchTemplate for CRUD operations on document data
     * 1. Field type properties are not flexible
     * 2. Unable to set the number of primary and replica shards
     */
//    @Test
//    public void createIndexStu() {
//        Stu stu = new Stu();
//        stu.setStuId(1002L);
//        stu.setName("Spider man");
//        stu.setAge(22);
//        stu.setMoney(18.8f);
//        stu.setSign("I am spider man");
//        stu.setDescription("I wish I am spider man");
//        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
//        esTemplate.index(indexQuery);
//    }


    @Test
    public void deleteIndexStu() {
        esTemplate.deleteIndex(Stu.class);
    }

    //////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void updateStuDoc() {

        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sign", "I am not super man");
        sourceMap.put("money", 88.6f);
        sourceMap.put("age", 33);

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(sourceMap);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1002")
                .withIndexRequest(indexRequest)
                .build();

        // update stu set sign='abc',age=33,money=88.6 where docId='1002'
        esTemplate.update(updateQuery);
    }
    @Test
    public void getStuDoc() {
        GetQuery query = new GetQuery();
        query.setId("1002");
        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }

    @Test
    public void deleteStuDoc() {
        esTemplate.delete(Stu.class, "1002");
    }

// ------------------------- Dividing Line --------------------------------

    @Test
    public void searchStuDoc() {
        // Create pagination with page 0 and size 2
        Pageable pageable = PageRequest.of(0, 2);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "save man"))
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class);
        System.out.println("Total number of pages after search: " + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }

    @Test
    public void highlightStuDoc() {
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        Pageable pageable = PageRequest.of(0, 10);

        // Sort by money descending and age ascending
        SortBuilder sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.DESC);
        SortBuilder sortBuilderAge = new FieldSortBuilder("age")
                .order(SortOrder.ASC);

        // Build search query with highlighting
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "save man"))
                .withHighlightFields(new HighlightBuilder.Field("description")
                        .preTags(preTag)
                        .postTags(postTag))
                .withSort(sortBuilder)
                .withSort(sortBuilderAge)
                .withPageable(pageable)
                .build();

        // Execute search with custom result mapping
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Stu> stuListHighlight = new ArrayList<>();
                SearchHits hits = response.getHits();

                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    // Extract fields from search hit
                    Object stuId = (Object)h.getSourceAsMap().get("stuId");
                    String name = (String)h.getSourceAsMap().get("name");
                    Integer age = (Integer)h.getSourceAsMap().get("age");
                    String sign = (String)h.getSourceAsMap().get("sign");
                    Object money = (Object)h.getSourceAsMap().get("money");

                    // Create new Stu object with highlighted description
                    Stu stuHL = new Stu();
                    stuHL.setDescription(description);
                    stuHL.setStuId(Long.valueOf(stuId.toString()));
                    stuHL.setName(name);
                    stuHL.setAge(age);
                    stuHL.setSign(sign);
                    stuHL.setMoney(Float.valueOf(money.toString()));
                    stuListHighlight.add(stuHL);
                }

                if (stuListHighlight.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>)stuListHighlight);
                }
                return null;
            }
        });

        System.out.println("Total number of pages after search: " + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }

}
