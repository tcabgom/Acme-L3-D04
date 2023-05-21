package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentEnrolmentShowTest extends TestHarness {
    // Internal state ---------------------------------------------------------

    @Autowired
    protected StudentEnrolmentTestRepository repository;

    // Test data --------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100positive(final int recordIndex, final String code, final String motivation, final String goals, final String course) {
        // HINT: lists the enrolments, clicks on
        // HINT+ one of them, and then checks that the enrolment has the expected data.

        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1, "asc");
        super.clickOnListingRecord(recordIndex);
        super.checkFormExists();

        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("course", course);

        super.signOut();
    }

    @Test
    public void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }

    @Test
    public void test300Hacking() {
        // HINT: this test tries to show an enrolment that wasn't registered by the principal,
        // HINT+ be it published or unpublished.
        Collection<Enrolment> enrolments = this.repository.findByStudentUsername("student1");
        String param;

        super.signIn("student2", "student2");
        for (Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/show", param);
            super.checkPanicExists();
        }

        super.signOut();
    }
}
