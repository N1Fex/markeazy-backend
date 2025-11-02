package ru.n1fex.markeazy.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "product_index")
@Setting(settingPath = "/elasticsearch-settings.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "russian_analyzer")
    private String description;

    @Field(type = FieldType.Integer)
    private Integer price;
}
