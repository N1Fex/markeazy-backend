package ru.n1fex.markeazy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.n1fex.markeazy.entity.ProductDoc;

import java.util.List;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDoc, Long> {

    @Query("""
            {
              "bool": {
                "should": [
                  {
                    "multi_match": {
                      "query": "?0",
                      "fields": ["title^4", "description^3"],
                      "type": "best_fields",
                      "operator": "or"
                    }
                  },
                  {
                    "match_phrase": {
                      "title": {
                        "query": "?0",
                        "boost": 3
                      }
                    }
                  },
                  {
                    "match_phrase": {
                      "description": {
                        "query": "?0",
                        "boost": 2
                      }
                    }
                  },
                  {
                    "match_phrase_prefix": {
                      "title": {
                        "query": "?0",
                        "boost": 2
                      }
                    }
                  },
                  {
                    "match_phrase_prefix": {
                      "description": {
                        "query": "?0",
                        "boost": 1
                      }
                    }
                  }
                ],
                "minimum_should_match": 1
              }
            }
            """)
    List<ProductDoc> searchByQuery(String query, Pageable pageable);

}
