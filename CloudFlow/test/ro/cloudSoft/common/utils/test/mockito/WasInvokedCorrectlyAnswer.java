package ro.cloudSoft.common.utils.test.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Can be used for checking if a method in a mock was invoked correctly (for example, if the parameters were the ones
 * expected). Optionally, it can return a result to the caller of the mock.
 *
 * 
 */
public class WasInvokedCorrectlyAnswer implements Answer<Object> {
	
	private Object objectToReturn = null;
    private boolean wasInvokedCorrectly = false;
	
	public WasInvokedCorrectlyAnswer() {}
	
	public WasInvokedCorrectlyAnswer(Object objectToReturn) {
		this.objectToReturn = objectToReturn;
	}

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        wasInvokedCorrectly = true;
        return objectToReturn;
    }

    public boolean wasInvokedCorrectly() {
        return wasInvokedCorrectly;
    }
}