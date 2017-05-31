package com.example.demo.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SQLQueryImpl;
import org.hibernate.jpa.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * Created by pengchangguo on 16/11/10.
 */
@Component
public class DynamicQueryRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DynamicQueryRepository.class);

	private transient EntityManager entityManager;

	private transient DataSource dataSource;

    @Resource(name = "org.springframework.orm.jpa.SharedEntityManagerCreator#0")
    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Resource(name = "dataSource")
    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T findById(Class<T> clazz, Object id) {
		return entityManager.find(clazz, id);
	}

	public <T> T findById(Class<T> clazz, Object id, LockModeType lockMode) {
		return entityManager.find(clazz, id, lockMode);
	}

	public <T> T findOne(String hql, Object... args) {
		List<T> list = query(hql, args);
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * 基于HQL查询数量接口
	 *
	 * @param hql
	 *            hql语句
	 *
	 * @param args
	 *            参数
	 *
	 * @return 返回数量
	 */
	public Long count(String hql, Object... args) {

		String countHql = hql;

		if (StringUtils.strip(hql).toUpperCase().startsWith("FROM")) {
			countHql = "select count(0) " + hql;
		}

		List<Long> list = query(countHql, args);

		return list.isEmpty() ? 0L : list.get(0);
	}

	/**
	 * 基于HQL查询接口
	 *
	 * @param hql
	 *            hql语句
	 *
	 * @param args
	 *            参数
	 *
	 * @param <T>
	 *            泛型
	 *
	 * @return 返回结果为List
	 */
	public <T> T query(String hql, Object... args) {

		Query query = entityManager.createQuery(hql);

		return executeQuery(query, args);
	}

	/**
	 * 基于HQL的分页查询接口
	 * 
	 * @param hql
	 *            HQL语句
	 *
	 * @param startRow
	 *            从第几行开始
	 *
	 * @param pageSize
	 *            每页大小
	 *
	 * @param args
	 *            HQL中的参数值, 如果HQL中是 "?", 则是按顺序匹配, 如果HQL中是 ":参数名", 则args[0]必须为Map参数集
	 *
	 * @return 结果集合
	 */
	public <T> T queryByPage(String hql, int startRow, int pageSize, Object... args) {
		Query query = entityManager.createQuery(hql);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		return executeQuery(query, args);
	}

	/**
	 * 基于SQL的数量查询
	 *
	 * @param sql
	 *            SQL
	 *
	 * @param args
	 *            参数
	 *
	 * @return 返回数量
	 */
	public Long sqlCount(String sql, Object... args) {

		@SuppressWarnings("rawtypes")
		List<Map> list = sqlQuery(sql, Map.class, args);

		if (list.isEmpty()) {
			return 0L;
		}

		@SuppressWarnings("rawtypes")
		Map.Entry entry = (Map.Entry) list.get(0).entrySet().iterator().next();

		return Long.valueOf(entry.getValue().toString());
	}

	/**
	 * 基于SQL查询
	 *
	 * @param sql
	 *            SQL
	 *
	 * @param args
	 *            参数
	 *
	 * @param <T>
	 *            泛型
	 *
	 * @return 返回结果为List<Map>
	 */
	public <T> T sqlQuery(String sql, Object... args) {

		return sqlQuery(sql, Map.class, args);

	}

	/**
	 * 基于SQL查询
	 *
	 * @param sql
	 *            SQL
	 *
	 * @param transFormClass
	 *            指定返回元素的类型, 如果指定为Map, 则元素为Map. 如果指定为Entity, 则元素为Entity
	 *
	 * @param args
	 *            参数
	 *
	 * @param <T>
	 *            泛型
	 *
	 * @return List<Map> 或 List<T extends Entity>
	 */
	public <T> T sqlQuery(String sql, Class<?> transFormClass, Object... args) {

		@SuppressWarnings("rawtypes")
		QueryImpl query = (QueryImpl) entityManager.createNativeQuery(sql);

		SQLQueryImpl sqlQuery = (SQLQueryImpl) query.getHibernateQuery();

		if (Map.class.isAssignableFrom(transFormClass)) {
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		} else {
			sqlQuery.addEntity(transFormClass);
			// sqlQuery.setResultTransformer(Transformers.aliasToBean(transFormClass));
		}

		return executeQuery(query, args);
	}

	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            实体类
	 * 
	 * @return 保存后的实体类
	 */
	public <T> T save(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	/**
	 * 更新操作
	 *
	 * @param hql
	 *            HQL语句
	 *
	 * @param args
	 *            参数
	 *
	 * @return 影响行数
	 */
	public int update(String hql, Object... args) {

		Query query = entityManager.createQuery(hql);

		return executeUpdate(query, args);
	}

	/**
	 * 基于JPA批量保存实体, 使用当前的Connection
	 *
	 * 效率比较 : async batch > sql batch > jpa batch
	 *
	 * @param entities
	 *            实体集合
	 *
	 * @param batchSize
	 *            批次大小
	 *
	 * @param <T>
	 *            泛型, 约束必须为实体
	 *
	 */
	public <T> void batch(List<T> entities, int batchSize) {

		int total = entities.size();

		int i = 0;

		for (T entity : entities) {

			i++;

			entityManager.persist(entity);

			if (batchSize > 0 && i >= batchSize && i % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}

		}

		if (batchSize == 0 || total < batchSize || i % batchSize != 0) {
			entityManager.flush();
			entityManager.clear();
		}
	}

	/**
	 * 基于SQL多线程批量保存, 每一批都开线程去执行, 每个线程都使用新的连接
	 *
	 * 效率比较 : async batch > sql batch > jpa batch
	 *
	 * @param sql
	 *            SQL
	 *
	 * @param paramList
	 *            参数
	 *
	 * @param batchSize
	 *            批次大小
	 *
	 */
	public void asyncBatch(final String sql, final List<Object[]> paramList, final int batchSize) {

		int threadCount = paramList.size() / batchSize;

		if (threadCount <= 1) {

			Connection connection = getNewConnection();

			batch(connection, sql, paramList, 0, false);

			return;
		}

		if (paramList.size() % batchSize > 0) {
			threadCount += 1;
		}

		/**
		 * execute the sql by multi thread !
		 */

		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			@SuppressWarnings("rawtypes")
			List<Future> futures = new ArrayList<>(0);

			for (int i = 0; i < threadCount; i++) {

				final int beginIndex = i * batchSize;

				int endIndex = (i + 1) * batchSize;

				if (endIndex > paramList.size()) {
					endIndex = paramList.size();
				}

				final int finalEndIndex = endIndex;

				@SuppressWarnings({ "unchecked", "rawtypes" })
				Future future = executor.submit((Callable) () -> {

                    batchByNewConnection(sql, paramList.subList(beginIndex, finalEndIndex), 0);

					return null;
				});

				futures.add(future);
			}

			for (@SuppressWarnings("rawtypes")
			Future future : futures) {
				try {

					future.get();

				} catch (InterruptedException e) {

					Thread.currentThread().interrupt();

				} catch (ExecutionException e) {

					throw new DynamicQueryException(e.getMessage(), e);

				}
			}

		} finally {
			executor.shutdown();
		}
	}

    /**
     * 基于SQL的批量保存, 使用当前连接
     *
     * @param sql
     *            SQL
     *
     * @param paramList
     *            参数
     *
     * @param batchSize
     *            批次大小
     *
     */
    public void batch(String sql, List<Object[]> paramList, int batchSize) {

        SessionImplementor session = (SessionImplementor) entityManager.unwrap(Session.class);

        batch(session.connection(), sql, paramList, batchSize, true);

    }

	/**
	 * 基于SQL的批量保存, 新开连接
	 *
	 * 效率比较 : async batch > sql batch > jpa batch
	 *
	 * @param sql
	 *            SQL
	 *
	 * @param paramList
	 *            参数
	 *
	 * @param batchSize
	 *            批次大小
	 *
	 */
	public void batchByNewConnection(String sql, List<Object[]> paramList, int batchSize) {

		batch(getNewConnection(), sql, paramList, batchSize, false);

	}

	private void batch(Connection connection, String sql, List<Object[]> paramList, int batchSize, boolean isCurrentConnection) {

		PreparedStatement preparedStatement = null;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < paramList.size(); i++) {

				Object[] params = paramList.get(i);

				for (int j = 0; j < params.length; j++) {
					preparedStatement.setObject(j + 1, params[j]);
				}

				preparedStatement.addBatch();

				int currentCount = i + 1;

				if (batchSize > 0 && currentCount >= batchSize && currentCount % batchSize == 0) {
					preparedStatement.executeBatch();
					connection.commit();
				}
			}

			if (batchSize == 0 || paramList.size() < batchSize || paramList.size() % batchSize != 0) {
				preparedStatement.executeBatch();
				connection.commit();
			}

		} catch (Exception e) {
			if (!isCurrentConnection) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new DynamicQueryException("执行sql语句出错, 回滚失败, sql : " + sql, e1);
				}
			}
			throw new DynamicQueryException("执行sql语句出错! sql : " + sql, e);

		} finally {

			if (preparedStatement != null && !isCurrentConnection) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error("游标关闭失败! sql : {}", sql, e);
				}
			}

			if (connection != null && !isCurrentConnection) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("连接关闭失败! sql : {}", sql, e);
				}
			}
		}
	}

	private Connection getNewConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DynamicQueryException("get new connection failed!", e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T executeQuery(Query query, Object... args) {

		/**
		 * Args Is Empty
		 */
		if (args == null || args.length == 0) {
			// noinspection unchecked
			return (T) query.getResultList();
		}

		/**
		 * The First Arg Is Map
		 */
		if (args[0] instanceof Map) {

			// noinspection unchecked
			@SuppressWarnings("rawtypes")
			Map<String, Object> map = (Map) args[0];

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

			// noinspection unchecked
			return (T) query.getResultList();
		}

		/**
		 * Otherwise
		 */
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i + 1, args[i]);
		}
		// noinspection unchecked
		return (T) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private int executeUpdate(Query query, Object... args) {

		/**
		 * Args Is Empty
		 */
		if (args == null || args.length == 0) {
			// noinspection unchecked
			return query.executeUpdate();
		}

		/**
		 * The First Arg Is Map
		 */
		if (args[0] instanceof Map) {

			// noinspection unchecked
			@SuppressWarnings("rawtypes")
			Map<String, Object> map = (Map) args[0];

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

			// noinspection unchecked
			return query.executeUpdate();
		}

		/**
		 * Otherwise
		 */
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i + 1, args[i]);
		}
		// noinspection unchecked
		return query.executeUpdate();
	}


    private class DynamicQueryException extends RuntimeException {

		private static final long serialVersionUID = -696524962926133533L;

		DynamicQueryException(String message, Throwable e) {
			super(message, e);
		}
	}
}
