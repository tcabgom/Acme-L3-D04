package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Course;
import acme.testing.TestHarness;
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

    public void test100Positive(final int recordIndex, final String haveRole, final String role, final String code, final String motivation, final String goals, final String course) {
        // HINT: this test updates an enrolment
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(recordIndex);
        super.checkFormExists();

        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Update");

        super.clickOnMenu("Student", "See your enrolments");
        super.checkListingExists();
        super.sortListing(0, "asc");
        super.checkColumnHasValue(recordIndex, 1, code);
        super.checkColumnHasValue(recordIndex, 2, motivation);
        super.clickOnListingRecord(recordIndex);

        super.checkFormExists();
        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);

        if (Integer.parseInt(haveRole) != 0)
            super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test200Negative(final int recordIndex, final String haveRole, final String role, final String code, final String motivation, final String goals, String course) {
        // HINT: this test attempts to update enrolments with incorrect data.
        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(recordIndex);
        super.checkFormExists();

        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Update");

        super.checkErrorsExist();

        if (Integer.parseInt(haveRole) != 0)
            super.signOut();
    }

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

    public void test301Hacking() {
        // HINT: this test tries to update an enrolment that wasn't registered by the principal,
        // HINT+ be it published or unpublished.

        Collection<Enrolment> enrolments = this.repository.findByStudentUsername("student1");
        String param;

        super.signIn("student2", "student2");
        for (final Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/update", param);
            super.checkPanicExists();
        }
        super.signOut();
    }

}


