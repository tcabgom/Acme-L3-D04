package acme.testing.student.enrolment;

import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class StudentEnrolmentCreateTest extends TestHarness {

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test100Positive(final String code, final String motivation, final String goals, final String course) {
        // HINT: this test authenticates as a role or doesnt authenticate, and then lists all enrolments
        // HINT: , creates a new one, and check that it's been created properly.


        super.signIn("student1", "student1");
        //on test roles, username and pass are equal
        super.clickOnMenu("Student", "See your enrolments");
        super.checkListingExists();

        super.clickOnButton("Create enrolment");
        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Create");

        super.clickOnMenu("Student", "See your enrolments");
        super.sortListing(1,"desc");
        super.checkColumnHasValue(0, 1, code);
        super.checkColumnHasValue(0, 2, motivation);
        super.clickOnListingRecord(0);

        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("motivation", motivation);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("course", course);

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/student/enrolment/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    public void test200Negative(final String code, final String motivation, final String goals, final String course) {
        // HINT: this test attempts to create enrolments with incorrect data.

        super.signIn("student1", "student1");
        //on test roles, username and pass are equal
        super.clickOnMenu("Student", "See your enrolments");
        super.clickOnButton("Create enrolment");
        super.checkFormExists();

        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("motivation", motivation);
        super.fillInputBoxIn("goals", goals);
        super.fillInputBoxIn("course", course);
        super.clickOnSubmit("Create");

        super.checkErrorsExist();

        super.signOut();

    }

    @Test
    public void test300Hacking() {

        super.request("/student/enrolment/create");
        super.checkPanicExists();

        super.signIn("administrator1", "administrator1");
        super.request("/student/enrolment/create");
        super.checkPanicExists();
        super.signOut();

        super.signIn("assistant1", "assistant1");
        super.request("/student/enrolment/create");
        super.checkPanicExists();
        super.signOut();

    }
}
