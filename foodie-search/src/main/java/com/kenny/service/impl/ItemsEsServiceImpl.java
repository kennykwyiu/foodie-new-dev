package com.kenny.service.impl;

import com.kenny.es.pojo.Items;
import com.kenny.service.ItemsEsService;
import com.kenny.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsEsServiceImpl implements ItemsEsService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searchItems(String keywords,
                                       String sort,
                                       Integer page,
                                       Integer pageSize) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        Pageable pageable = PageRequest.of(page, pageSize);

        // Sort by money descending and age ascending
        SortBuilder sortBuilder = null;
        if (sort.equals("c")) {
            sortBuilder = new FieldSortBuilder("sellCounts")
                    .order(SortOrder.DESC);
        } else if (sort.equals("p")) {
            sortBuilder = new FieldSortBuilder("price")
                .order(SortOrder.ASC);
        } else { // default
            sortBuilder = new FieldSortBuilder("itemName")
                    .order(SortOrder.ASC);
        }

//        SortBuilder sortBuilder = new FieldSortBuilder("money")
//                .order(SortOrder.DESC);
//        SortBuilder sortBuilderAge = new FieldSortBuilder("age")
//                .order(SortOrder.ASC);

        // Build search query with highlighting
        String itemNameField = "itemName";
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameField, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameField)
                        /*
                        // change to define style in front-end with <em>
                        em {
                            color : green;
                            font-weight: bold;
                        }
                        .preTags(preTag)
                        .postTags(postTag)
                        */
                        )

                .withSort(sortBuilder)
//                .withSort(sortBuilderAge)
                .withPageable(pageable)
                .build();

        // Execute search with custom result mapping
        AggregatedPage<Items> pagedItems = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Items> itemHighlightList = new ArrayList<>();
                SearchHits hits = response.getHits();

                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get(itemNameField);
                    String itemName = highlightField.getFragments()[0].toString();

                    // Extract fields from search hit
                    String itemId = (String) h.getSourceAsMap().get("itemId");
                    String imgUrl = (String) h.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) h.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer) h.getSourceAsMap().get("sellCounts");

                    // Create new Items object with highlighted itemName
                    Items item = new Items();
                    item.setItemId(itemId);
                    item.setItemName(itemName);
                    item.setImgUrl(imgUrl);
                    item.setPrice(price);
                    item.setSellCounts(sellCounts);

                    itemHighlightList.add(item);
                }

                return new AggregatedPageImpl<>((List<T>) itemHighlightList,
                                                pageable,
                                                response.getHits().totalHits);
            }
        });

//        System.out.println("Total number of pages after search: " + pagedItems.getTotalPages());
//        List<Items> stuList = pagedItems.getContent();
//        for (Items s : stuList) {
//            System.out.println(s);
//        }

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(pagedItems.getContent());
        gridResult.setPage(page + 1);
        gridResult.setTotal(pagedItems.getTotalPages());
        gridResult.setRecords(pagedItems.getTotalElements());

        return gridResult;
    }
}
