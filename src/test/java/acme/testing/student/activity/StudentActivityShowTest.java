package acme.testing.student.activity;

import acme.entities.activity.Activity;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentActivityShowTest extends TestHarness {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected StudentActivityTestRepository repository;

    // Test data --------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/student/activity/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100positive(final int activityRecordIndex, final String title, final String activityAbstract, final String type, final String periodStart, final String periodEnd, final String furtherInformation) {
        // HINT: lists the activities, clicks on
        // HINT+ one of them, and then checks that the listing has the expected data.

        super.signIn("student1", "student1");
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(0);
        super.clickOnButton("Show workbook");

        super.sortListing(0, "asc");
        super.clickOnListingRecord(activityRecordIndex);
        super.checkFormExists();

        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("activityAbstract", activityAbstract);
        super.checkInputBoxHasValue("type", type);
        super.checkInputBoxHasValue("periodStart", periodStart);
        super.checkInputBoxHasValue("periodEnd", periodEnd);
        super.checkInputBoxHasValue("furtherInformation", furtherInformation);

        super.signOut();
    }

    @Test
    public void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }

    @Test
    public void test300Hacking() {
        // HINT: Tries to access an activity with incorrect roles or with a student
        // HINT+ who is not the owner.

        Collection<Activity> activities = this.repository.findByUser("student1");
        String param;

        for (Activity activity : activities) {
            param = String.format("id=%d", activity.getId());
            super.signIn("student2", "student2");
            super.request("/student/activity/show", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("administrator1", "administrator1");
            super.request("/student/activity/show", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("lecturer1", "lecturer1");
            super.request("/student/activity/show", param);
            super.checkPanicExists();
            super.signOut();
        }
    }

}
