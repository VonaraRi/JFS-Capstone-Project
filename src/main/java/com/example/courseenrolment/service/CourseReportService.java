package com.example.courseenrolment.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.example.courseenrolment.dto.ReportCountResponse;
import com.example.courseenrolment.dto.EnrolmentOverviewResponse;;

@Service
public class CourseReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseReportService.class);

    private final MongoTemplate mongoTemplate;

    public CourseReportService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ReportCountResponse> countCourseByStatus() {
        return countCourseByField("status");
    }

    public List<ReportCountResponse> countCourseByCategory() {
        return countCourseByField("category");
    }

    public List<ReportCountResponse> countCourseByLevel() {
        return countCourseByField("level");
    }

    public List<ReportCountResponse> countCourseByCapacity() {
        return countCourseByField("capacity");
    }

    // --- 1. Enrolments per Course ---
    // Output format: { "label": "Software Engineering Fundamentals", "count": 15 }
    public List<ReportCountResponse> getEnrolmentsPerCourse() {
        logger.info("Generating enrolments per course report (active & unique students)");

        Aggregation aggregation = Aggregation.newAggregation(
            // 1. Filter out DROPPED or CANCELLED records (Adjust status value if needed)
            Aggregation.match(Criteria.where("status").regex("^ENROLLED$|^ACTIVE$", "i")),

            // 2. Convert IDs for $lookup joins
            Aggregation.project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$courseId")).as("convertedCourseId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("convertedUserId"),

            // 3. Join with courses
            Aggregation.lookup("courses", "convertedCourseId", "_id", "courseInfo"),
            Aggregation.unwind("courseInfo", true),

            // 4. Join with users
            Aggregation.lookup("users", "convertedUserId", "_id", "userInfo"),
            Aggregation.unwind("userInfo", true),

            // 5. Group by course title and collect UNIQUE student names
            Aggregation.group("courseInfo.title")
                .addToSet("userInfo.name").as("studentNames"),

            // 6. Calculate total unique active students
            Aggregation.project()
                .and("_id").as("label")
                .and("studentNames").as("studentNames")
                .and(ArrayOperators.Size.lengthOfArray("studentNames")).as("count"),

            // 7. Sort by highest count
            Aggregation.sort(Sort.Direction.DESC, "count")
        );

        return mongoTemplate.aggregate(aggregation, "enrolments", ReportCountResponse.class)
                            .getMappedResults();
    }

    // --- 2. Most Popular Courses (Top 3) ---
    // Output format: { "label": "C++ Programming", "count": 30 }
    public List<ReportCountResponse> getMostPopularCourses() {
        logger.info("Generating most popular courses report (matching enrolments per course pipeline)");

        Aggregation aggregation = Aggregation.newAggregation(
            // 1. Strict status matching to align with getEnrolmentsPerCourse
            Aggregation.match(Criteria.where("status").regex("^ENROLLED$|^ACTIVE$", "i")),

            // 2. Convert IDs for $lookup joins
            Aggregation.project()
                    .and(ConvertOperators.ToObjectId.toObjectId("$courseId")).as("convertedCourseId")
                    .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("convertedUserId"),

            // 3. Join with courses
            Aggregation.lookup("courses", "convertedCourseId", "_id", "courseInfo"),
            Aggregation.unwind("courseInfo", true),

            // 4. Join with users
            Aggregation.lookup("users", "convertedUserId", "_id", "userInfo"),
            Aggregation.unwind("userInfo", true),

            // 5. Group by course title and collect UNIQUE student names (matching enrolments per course)
            Aggregation.group("courseInfo.title")
                    .addToSet("userInfo.name").as("studentNames"),

            // 6. Calculate total unique active students
            Aggregation.project()
                    .and("_id").as("label")
                    .and(ArrayOperators.Size.lengthOfArray("studentNames")).as("count"),

            // 7. Sort by highest count and limit to top 4
            Aggregation.sort(Sort.Direction.DESC, "count"),
            Aggregation.limit(3)
        );

        return mongoTemplate.aggregate(aggregation, "enrolments", ReportCountResponse.class)
                            .getMappedResults();
    }

    // --- 3. Enrolments Overview ---
    // Output format: { "label": "Beginner", "count": 45 }
    public EnrolmentOverviewResponse getOverview() {
        logger.info("Generating aggregated enrolment overview report");

        List<ReportCountResponse> level = getEnrolmentCountByCourseField("level");
        List<ReportCountResponse> status = getEnrolmentCountByCourseField("status");
        List<ReportCountResponse> category = getEnrolmentCountByCourseField("category");
        List<ReportCountResponse> capacity = getEnrolmentCountByCourseField("capacity");

        long totalCourses = mongoTemplate.getCollection("courses").countDocuments();

        return new EnrolmentOverviewResponse(level, status, category, capacity, totalCourses);
    }

    // Helper method to aggregate unique student enrolments per course field
    private List<ReportCountResponse> getEnrolmentCountByCourseField(String field) {
        Aggregation aggregation = Aggregation.newAggregation(
            // 1. Match strict active enrolments
            Aggregation.match(Criteria.where("status").regex("^ENROLLED$|^ACTIVE$", "i")),
            
            // 2. Convert IDs for lookup
            Aggregation.project()
                    .and(ConvertOperators.ToObjectId.toObjectId("$courseId")).as("convertedCourseId")
                    .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("convertedUserId"),

            // 3. Join courses and users
            Aggregation.lookup("courses", "convertedCourseId", "_id", "courseInfo"),
            Aggregation.unwind("courseInfo", true),
            Aggregation.lookup("users", "convertedUserId", "_id", "userInfo"),
            Aggregation.unwind("userInfo", true),

            // 4. Group by specified course field and collect unique student names
            Aggregation.group("courseInfo." + field)
                    .addToSet("userInfo.name").as("studentNames"),
                    
            // 5. Calculate count and set label
            Aggregation.project()
                    .and("_id").as("label")
                    .and(ArrayOperators.Size.lengthOfArray("studentNames")).as("count"),
            Aggregation.sort(Sort.Direction.DESC, "count")
        );

        return mongoTemplate.aggregate(aggregation, "enrolments", ReportCountResponse.class)
                            .getMappedResults();
    }

    // --- 4. Monthly Enrolment Totals ---
    // Output format: { "label": "2026-01", "count": 120 }
    public List<ReportCountResponse> getMonthlyEnrolmentTotals() {
        logger.info("Generating monthly enrolment totals report with student names");

        Aggregation aggregation = Aggregation.newAggregation(
            // 1. Filter active enrolments (or omit if you want all)
            Aggregation.match(Criteria.where("status").regex("^ENROLLED$|^ACTIVE$", "i")),

            // 2. Convert userId string to ObjectId for lookup join
            Aggregation.project()
                    .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("convertedUserId")
                    .and(DateOperators.DateToString.dateOf("enrolmentDate").toString("%Y-%m")).as("month"),

            // 3. Join with users collection
            Aggregation.lookup("users", "convertedUserId", "_id", "userInfo"),
            Aggregation.unwind("userInfo", true),

            // 4. Group by month, count enrolments, and collect unique student names
            Aggregation.group("month")
                    .count().as("count")
                    .addToSet("userInfo.name").as("studentNames"),

            // 5. Shape into ReportCountResponse DTO
            Aggregation.project()
                    .and("_id").as("label")
                    .and("count").as("count")
                    .and("studentNames").as("studentNames"),

            // 6. Sort chronologically
            Aggregation.sort(Sort.Direction.ASC, "label")
        );

        return mongoTemplate.aggregate(aggregation, "enrolments", ReportCountResponse.class)
                            .getMappedResults();
    }

    private List<ReportCountResponse> countCourseByField(String field) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group(field).count().as("count"),
            Aggregation.project("count").and("_id").as("label"),
            Aggregation.sort(Sort.Direction.ASC, "label")
        );

        return mongoTemplate.aggregate(aggregation, "courses", ReportCountResponse.class)
                            .getMappedResults();
    }
}