package ro.cloudSoft.cloudDoc.domain.content;

import java.util.List;

/**
 * Extinde clasa entitate, contoleaza accesul pentru orice tip de entitate de continut
 * Fiecare entiate de continut trebuie sa fie precedata de o lista de control al accesului
 * Entitatile de continut precedate sunt: <b>Document Location</b>, <b>Folder</b> si <b>Content</b>
 * Lista permisiunilor este reprezentata de o lista de perechi
 * <i>cheie, valoare</i>.
 * Cheia este reprezentata de identificatorul utilizatorului, iar valoarea de
 * identificatorul permisiunii.
 * Identificatorii pentru permisiuni sunt urmatorii:
 * <b>1111</b> - <i>Coordonator </i> permisiuni:citire, editare, adaugare , stergere
 * <b>1110</b> - <i>Colaborator</i> permisiuni: citire, editare, adaugare
 * <b>1100</b> - <i>Editor</i> permisiuni: citire, editare
 * <b>1000</b> - <i>Cititor</i> permisiuni: citire
 * 
 */
public class ACL {

    /**
     * Lista de perechi cheie valoare ce defineste permisiunile utilizatorilor
     * la entitatile de continut
     */
    private List<Permission> permissions;

    /**
     * @return the permissions
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}

