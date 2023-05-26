package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class StudentEnrolmentFinaliseTest extends TestHarness {
    // Internal state ---------------------------------------------------------

    @Autowired
    protected StudentEnrolmentTestRepository repository;

    // Test methods ------------------------------------------------------------


    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/finalise-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100Positive(final int recordIndex, final String creditCardNibble, final String creditCardHolder, final String expirationDate,final String securityCode) {
        // HINT: this test logs in as a student, lists his or her enrolments,
        // HINT+ selects one of them, finalises it, and then checks that
        // HINT+ the enrolment has properly been finalised

        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(0, "desc");

        super.clickOnListingRecord(recordIndex);
        super.clickOnButton("Finalise");
        super.checkFormExists();
        super.fillInputBoxIn("creditCardNibble", creditCardNibble);
        super.fillInputBoxIn("creditCardHolder", creditCardHolder);
        super.fillInputBoxIn("expirationDate", expirationDate);
        super.fillInputBoxIn("securityCode", securityCode);

        super.clickOnSubmit("Finalise");
        super.checkNotErrorsExist();

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/finalise-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test200Negative(final String creditCardNibble, final String creditCardHolder, final String expirationDate,final String securityCode) {
        // HINT: this test attempts to finalise an enrolment with wrong data.

        super.signIn("student1", "student1");

        super.clickOnMenu("Student", "See your enrolments");
        super.checkListingExists();
        super.sortListing(0, "desc");

        super.clickOnListingRecord(2);
        super.clickOnButton("Finalise");
        super.checkFormExists();
        super.fillInputBoxIn("creditCardNibble", creditCardNibble);
        super.fillInputBoxIn("creditCardHolder", creditCardHolder);
        super.fillInputBoxIn("expirationDate", expirationDate);
        super.fillInputBoxIn("securityCode", securityCode);

        super.clickOnSubmit("Finalise");
        super.checkErrorsExist();

        super.signOut();
    }

    @Test
    public void test300Hacking() {

        // HINT: this test tries to finalise an enrolment not created by the principal

        Collection<Enrolment> enrolments = this.repository.findByStudentUsername("student1");
        String param;

        for (final Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());

            super.request("/student/enrolment/finalise", param);
            super.checkPanicExists();

            super.signIn("student2", "student2");
            super.request("/student/enrolment/finalise", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("administrator1", "administrator1");
            super.request("/student/enrolment/finalise", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("assistant1", "assistant1");
            super.request("/student/enrolment/finalise", param);
            super.checkPanicExists();
            super.signOut();

        }
    }

    @Test
    public void test301Hacking() {
        // HINT: this test tries to finalise an enrolment that was already finalised

        Collection<Enrolment> enrolments = this.repository.findFinalisedByStudentUsername("student1");
        String param;

        super.signIn("student1", "student1");
        for (final Enrolment enrolment : enrolments) {
            param = String.format("id=%d", enrolment.getId());
            super.request("/student/enrolment/finalise", param);
            super.checkPanicExists();
        }
        super.signOut();
    }


}
