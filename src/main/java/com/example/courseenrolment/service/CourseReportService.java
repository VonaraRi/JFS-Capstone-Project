package com.example.courseenrolment.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.ReportCountResponse;

@Service
public class CourseReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseReportService.class);

    private final MongoTemplate mongoTemplate;

    public CourseReportService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ReportCountResponse> countCourseByStatus() {
        logger.info("Generating course count report by status");

        return countCourseByField("status");
    }

    public List<ReportCountResponse> countCourseByCategory() {
        logger.info("Generating course count report by category");

        return countCourseByField("category");
    }

    public List<ReportCountResponse> countCourseByLevel() {
        logger.info("Generating course count report by level");

        return countCourseByField("level");
    }

    public List<ReportCountResponse> countCourseByCapacity() {
        logger.info("Generating course count report by capacity");

        return countCourseByField("capacity");
    }

    private List<ReportCountResponse> countCourseByField(String field) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group(field).count().as("count"),
            Aggregation.project("count").and("_id").as("label"),
            Aggregation.sort(Sort.Direction.ASC, "label")
        );

        AggregationResults<ReportCountResponse> results = mongoTemplate.aggregate(
            aggregation,
            "courses",
        ReportCountResponse.class
        );

        return results.getMappedResults();
    }
}