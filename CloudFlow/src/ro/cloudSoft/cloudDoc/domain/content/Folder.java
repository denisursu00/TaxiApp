package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Collection;

public class Folder extends Entity {

    private Collection<Document> documents;

    public Folder() {
    }

    /**
     * @return the documents
     */
    public Collection<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(Collection<Document> documents) {
        this.documents = documents;
    }
}

