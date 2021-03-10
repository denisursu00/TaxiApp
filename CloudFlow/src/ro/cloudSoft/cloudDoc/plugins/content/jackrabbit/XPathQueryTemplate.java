package ro.cloudSoft.cloudDoc.plugins.content.jackrabbit;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

/**
 * 
 */
public class XPathQueryTemplate {

	public static NodeIterator find(Session session, String query) throws RepositoryException {
		return session.getWorkspace().getQueryManager().createQuery(query, Query.XPATH).execute().getNodes();
	}
}