
package acme.features.authenticated.student;

import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;
import acme.roles.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatedStudentRepository extends AbstractRepository {

	@Query("select u from UserAccount u where u.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select s from Student s where s.userAccount.id = :id")
	Student findStudentByUserAccountId(int id);

}
