package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Course;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentEnrolmentUpdateTest extends TestHarness {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected StudentEnrolmentTestRepository repository;

    // Test methods ------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)

    public void test100Positive(final String code, final String motivation, final String goals, final String course) {
        // HINT: this test updates an enrolment

        super.signIn("student1", "student1");
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1, "desc");
        super.clickOnListingRecord(0);
        super.checkFormExists();

        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Update");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1, "desc");
        super.checkColumnHasValue(0, 1, code);
        super.checkColumnHasValue(0, 2, motivation);
        super.clickOnListingRecord(0);

        super.checkFormExists();
        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("course", course);

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test200Negative(final String code, final String motivation, final String goals, String course) {
        // HINT: this test attempts to update enrolments with incorrect data.
        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "desc");
        super.clickOnListingRecord(0);
        super.checkFormExists();

        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Update");

        super.checkErrorsExist();

        super.signOut();
    }

    @Test
    public void test300Hacking() {
        // HINT: this test attempts to update enrolments that are already finalised

        Collection<Enrolment> enrolments = this.repository.findFinalisedByStudentUsername("student1");
        String param;

        super.signIn("student1", "student1");
        for (final Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();
        }
        super.signOut();
    }

    @Test
    public void test301Hacking() {
        // HINT: this test tries to update an enrolment that wasn't registered by the principal,

        Collection<Enrolment> enrolments = this.repository.findByStudentUsername("student1");
        String param;

        for (final Enrolment enrolment : enrolments) {

            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();

            super.signIn("student2", "student2");
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("lecturer1", "lecturer1");
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("administrator1", "administrator1");
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();
            super.signOut();
        }
    }

}


