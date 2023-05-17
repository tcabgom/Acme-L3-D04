
package acme.forms;

import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------
	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	protected Integer					totalAuditors;
	protected Integer					totalAdministrators;
	protected Integer					totalAssistants;
	protected Integer					totalLecturers;
	protected Integer					totalStudents;
	protected Integer					totalCompanys;

	protected Double					linkAndEmailPeepRatio;
	protected Double					criticalBulletinRatio;
	protected Double					nonCriticalBulletinRatio;

	protected Map<String, Statistics>	budgetStatisticsByCurrency;

	protected Statistics				notesPostedInLast10Weeks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
