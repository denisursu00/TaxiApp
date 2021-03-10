package ro.cloudSoft.cloudDoc.dao.content;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

import com.google.common.collect.Iterables;

public class MimeTypeDaoImpl extends HibernateDaoSupport implements MimeTypeDao, InitializingBean
{
	LogHelper log = LogHelper.getInstance(MimeTypeDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	public List<MimeType> getAllMimeTypes()
	{
		String query = "FROM MimeType ORDER BY name, extension";
		return getHibernateTemplate().find(query);
	}

	@Override
	public void saveMimeType(MimeType mimeType) {
		getHibernateTemplate().saveOrUpdate( mimeType );
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveAll(Collection<MimeType> mimeTypes) {
		getHibernateTemplate().saveOrUpdateAll(mimeTypes);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MimeType getMimeType(String extension) {
		String query = "FROM MimeType WHERE LOWER(extension) = LOWER(?)";
		List<MimeType> mimeTypes = getHibernateTemplate().find(query,extension);
		return Iterables.getOnlyElement(mimeTypes, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMimeTypeIdForExtension(String extension) {
		String query = "SELECT mt.id FROM MimeType mt WHERE LOWER(extension) = LOWER(?)";
		List<Long> mimeTypeIds = getHibernateTemplate().find(query,extension);
		return Iterables.getOnlyElement(mimeTypeIds, null);
		
	}
	
	@Override
	public MimeType find(Long id) {
		MimeType mimeType = (MimeType) getHibernateTemplate().get(MimeType.class, id);
		return mimeType;
        
    }
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteMimeType(final Long mimeTypeId) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"DELETE " +
					"FROM MimeType mimeType " +
					"WHERE mimeType.id = (:mimeTypeId) ";
				return session.createQuery(query)
					.setParameter("mimeTypeId", mimeTypeId)
					.executeUpdate();
			}
		});
	}
	
}