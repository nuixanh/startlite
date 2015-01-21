package com.clas.starlite.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Admin on 1/21/2015.
 */
@Document(collection="assessment")
public class Assessment {
    private String id;
    private String userId;
    private String customerName;
    private String customerEmail;
    private long created;
    private long modified;

}
