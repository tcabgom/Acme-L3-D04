package acme.testing.student.activity;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentActivityCreateTest extends TestHarness {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected StudentActivityTestRepository repository;

    // Test data --------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100positive(final String title, final String activityAbstract, final String type, final String periodStart, final String periodEnd, final String furtherInformation) {
        // HINT: Creates a new activity, and then checks that the form has the expected data.

        super.signIn("student1", "student1");
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(0);
        super.clickOnButton("Show workbook");

        super.clickOnButton("Create activity in this workbook");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("activityAbstract", activityAbstract);
        super.fillInputBoxIn("type", type);
        super.fillInputBoxIn("periodStart", periodStart);
        super.fillInputBoxIn("periodEnd", periodEnd);
        super.fillInputBoxIn("furtherInformation", furtherInformation);
        super.clickOnSubmit("Create activity");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(0);
        super.clickOnButton("Show workbook");

        super.sortListing(0, "desc");
        super.checkColumnHasValue(0, 0, title);
        super.checkColumnHasValue(0, 1, type);

        super.clickOnListingRecord(0);
        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("activityAbstract", activityAbstract);
        super.checkInputBoxHasValue("type", type);
        super.checkInputBoxHasValue("periodStart", periodStart);
        super.checkInputBoxHasValue("periodEnd", periodEnd);
        super.checkInputBoxHasValue("furtherInformation", furtherInformation);

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/student/activity/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test200negative(final String title, final String activityAbstract, final String type, final String periodStart, final String periodEnd, final String furtherInformation) {

        super.signIn("student1", "student1");
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(0);
        super.clickOnButton("Show workbook");

        super.clickOnButton("Create activity in this workbook");

        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("activityAbstract", activityAbstract);
        super.fillInputBoxIn("type", type);
        super.fillInputBoxIn("periodStart", periodStart);
        super.fillInputBoxIn("periodEnd", periodEnd);
        super.fillInputBoxIn("furtherInformation", furtherInformation);
        super.clickOnSubmit("Create activity");

        super.checkErrorsExist();
        super.signOut();

    }

    @Test
    public void test300Hacking() {
        // HINT: This test tries to create an activity with incorrect roles or with a student
        // HINT+ who is not the student who created the enrolment.

        Collection<Enrolment> enrolments = this.repository.findFinishedEnrolmentsByStudent("student1");
        String param;

        for (Enrolment enrolment : enrolments) {
            param = String.format("enrolmentId=%d", enrolment.getId());
            super.signIn("student2", "student2");
            super.request("/student/activity/create", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("administrator1", "administrator1");
            super.request("/student/activity/create", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("lecturer1", "lecturer1");
            super.request("/student/activity/create", param);
            super.checkPanicExists();
            super.signOut();
        }
    }

    @Test
    public void test301Hacking() {
        // HINT: This test tries to create an activity in an enrolment that is not finalised.

        Collection<Enrolment> enrolments = this.repository.findUnfinishedEnrolmentsByStudent("student1");
        String param;

        super.signIn("student1", "student1");

        for (Enrolment enrolment : enrolments) {
            param = String.format("enrolmentId=%d", enrolment.getId());
            super.request("/student/activity/create", param);
            super.checkPanicExists();
        }

        super.signOut();
    }

}
