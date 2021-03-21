package ro.cloudSoft.cloudDoc;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.docs.utils.log.LogHelper;

/**
 * Clasa de baza pentru testele ce au nevoie de Spring
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	locations = {
		"file:**/cloudDoc-constants.xml",
		"file:**/cloudDoc-content.xml",
		"file:**/cloudDoc-context.xml",
		"file:**/cloudDoc-integrityCheck.xml",
		"file:**/cloudDoc-mail.xml",
		"file:**/cloudDoc-misc.xml",
		"file:**/cloudDoc-organization.xml",
		"file:**/cloudDoc-parameters.xml",
		"file:**/cloudDoc-process.xml",
		"file:**/cloudDoc-scheduledTasks.xml",
		"file:**/cloudDoc-security.xml",
		"file:**/cloudDoc-webServices.xml"
	}
)
@Transactional(rollbackFor = Throwable.class)
public abstract class AbstractSpringAwareTest {
	
	static {
		LogHelper.initLog4j();
	}
}