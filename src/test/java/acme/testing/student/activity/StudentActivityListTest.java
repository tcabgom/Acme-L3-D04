package acme.testing.student.activity;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentActivityListTest extends TestHarness {


    @Autowired
    protected StudentActivityTestRepository repository;

    // Test methods -------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/student/activity/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100Positive(final int activityRecordIndex, final String title, final String type) {
        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(0);
        super.clickOnButton("Show workbook");

        super.checkListingExists();
        super.sortListing(0, "asc");
        super.checkColumnHasValue(activityRecordIndex, 0, title);
        super.checkColumnHasValue(activityRecordIndex, 1, type);

        super.signOut();
    }

    @Test
    public void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }

    @Test
    public void test300Hacking() {
        // HINT: this test tries to list activities of enrolments not created by the principal

        super.signIn("student2", "student2");
        String param;

        Collection<Enrolment> enrolments = this.repository.findFinishedEnrolmentsByStudent("student1");

        for(Enrolment enrolment : enrolments) {
            param = String.format("enrolmentId=%d", enrolment.getId());
            super.request("/student/activity/list", param);
            super.checkPanicExists();
        }

        super.signOut();
    }

    @Test
    public void test301Hacking() {
        // HINT: this test tries to list activities of enrolments that are not finalised
        super.signIn("student1", "student1");

        Collection<Enrolment> enrolments = this.repository.findUnfinishedEnrolmentsByStudent("student1");
        String param;

        for (Enrolment enrolment : enrolments) {
            param = String.format("enrolmentId=%d", enrolment.getId());
            super.request("/student/activity/list", param);
            super.checkPanicExists();
        }

        super.signOut();
    }


}
