package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentEnrolmentDeleteTest extends TestHarness {

    @Autowired
    protected StudentEnrolmentTestRepository repository;

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100positive(final String code, final String motivation, final String goals, final String course) {
        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1, "asc");

        super.checkColumnHasValue(1,1,code);
        super.clickOnListingRecord(0);

        super.clickOnSubmit("Delete");
        super.clickOnListingRecord(0);

        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("course", course);

        super.signOut();
    }

    @Test
    public void test200negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }

    @Test
    public void test300Hacking() {
        // HINT: this test tries to delete an enrolment that wasn't registered by the principal,
        // HINT+ be it published or unpublished.
        Collection<Enrolment> enrolments = this.repository.findByStudentUsername("student1");
        String param;

        for(Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/delete", param);
            super.checkPanicExists();

            super.signIn("student2", "student2");
            super.request("/student/enrolment/show", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("administrator1", "administrator1");
            super.request("/student/enrolment/delete", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("lecturer1", "lecturer1");
            super.request("/student/enrolment/delete", param);
            super.checkPanicExists();
            super.signOut();
        }
    }

    @Test
    public void test301Hacking() {
        // HINT: this test tries to delete an enrolment that was finalised

        Collection<Enrolment> enrolments = this.repository.findFinalisedByStudentUsername("student1");

        String param;
        super.signIn("student1", "student1");
        for(Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/delete", param);
            super.checkPanicExists();
        }
        super.signOut();
    }
}
