package ro.cloudSoft.cloudDoc.services.bpm.custom.decizieRechemare;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTask;
import ro.cloudSoft.cloudDoc.services.bpm.custom.AutomaticTaskExecutionException;

public class DecizieRechemareUpdateEvent extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException {
		//TODO: functionalitatea din aceasta clasa a fost mutata in "DecizieRechemareActualizareDateInNomenclatorulConcediiPersonalArb", deoarece executia fluxului sare peste acest pas automat
		//mai trebuie investigat de ce  DecizieRechemareActualizareDateInNomenclatorulConcediiPersonalArb
	}
	
}
