<?xml version="1.0" encoding="UTF-8"?>

<process name="${process.name?xml}" xmlns="http://jbpm.org/4.0/jpdl">	
	<on event="start">
		<event-listener class="${EVENT_LISTENER_CLASS_PROCESS_START}">
			<field name="pathsWithManualAssignment">
				<list>
					<#list process.pathsWithManualAssignment as pathWithManualAssignment>
						<object class="${CLASS_WORKFLOW_PATH_WITH_MANUAL_ASSIGNMENT}">
							<field name="transitionName">
								<string value="${pathWithManualAssignment.transitionName}" />
							</field>
							<field name="finalStateCode">
								<string value="${pathWithManualAssignment.outgoingNodeName}" />
							</field>
						</object>
					</#list>
				</list>
			</field>
		</event-listener>
	</on>

	<start name="${process.startNode.name}">
		<on event="start">
			<event-listener class="${EVENT_LISTENER_CLASS_NODE_START}">
				<field name="stateId">
					<long value="${process.startNode.workflowStateId?c}" />
				</field>
			</event-listener>
		</on>

		<#list process.startNode.outgoingTransitions as transition>
			<transition name="${transition.name}" to="${transition.outgoingNodeName}">
				<script expr="${transition.name}" var="${VARIABLE_NAME_LAST_TRANSITION_NAME}" />

				<event-listener class="${EVENT_LISTENER_CLASS_TRANSITION}">
					<field name="transitionId">
						<long value="${transition.workflowTransitionId?c}" />
					</field>
				</event-listener>
			</transition>
		</#list>
	</start>

	<#list process.taskNodes as taskNode>
		<task name="${taskNode.name}" description="${taskNode.description}">
			<on event="start">
				<event-listener class="${EVENT_LISTENER_CLASS_NODE_START}">
					<field name="stateId">
						<long value="${taskNode.workflowStateId?c}" />
					</field>
				</event-listener>
			</on>
			
			<#assign automaticRunning = taskNode.automaticRunning>
			
			<#if automaticRunning == true>
				<assignment-handler class="${APPLICATION_ASSIGNMENT_HANDLER_CLASS}">
					<field name="workflowStateId">
						<long value="${taskNode.workflowStateId?c}" />
					</field>
				</assignment-handler>
			
			<#else>
				<assignment-handler class="${ASSIGNMENT_HANDLER_CLASS}">
					<field name="workflowStateId">
						<long value="${taskNode.workflowStateId?c}" />
					</field>
				</assignment-handler>
			
			</#if>

			<#list taskNode.outgoingTransitions as transition>
				<transition name="${transition.name}" to="${transition.outgoingNodeName}">
					<script expr="${transition.name}" var="${VARIABLE_NAME_LAST_TRANSITION_NAME}" />

					<event-listener class="${EVENT_LISTENER_CLASS_TRANSITION}">
						<field name="transitionId">
							<long value="${transition.workflowTransitionId?c}" />
						</field>
					</event-listener>
				</transition>
			</#list>
		</task>
	</#list>

	<#list process.endNodes as endNode>
		<end name="${endNode.name}">
			<on event="start">
				<event-listener class="${EVENT_LISTENER_CLASS_END_NODE_START}">
					<field name="stateId">
						<long value="${endNode.workflowStateId?c}" />
					</field>
				</event-listener>
			</on>
		</end>
	</#list>
</process>