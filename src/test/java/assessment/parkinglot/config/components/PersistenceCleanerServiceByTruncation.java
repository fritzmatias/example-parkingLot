package assessment.parkinglot.config.components;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class PersistenceCleanerServiceByTruncation implements PersistenceCleanerService {
    private final EntityManager entityManager;
    private final Logger logger= LoggerFactory.getLogger(PersistenceCleanerServiceByTruncation.class);

    public PersistenceCleanerServiceByTruncation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void cleanup(String schemaName) {
        this.disableConstraints();
        this.truncateTables(schemaName);
        this.resetSequences(schemaName);
        this.enableConstraints();
    }

    private void disableConstraints() {
        var sql="SET REFERENTIAL_INTEGRITY FALSE";
        var query=this.entityManager.createNativeQuery(sql, String.class);
        query.executeUpdate();
    }
    private void enableConstraints() {
        var sql="SET REFERENTIAL_INTEGRITY TRUE";
        var query=this.entityManager.createNativeQuery(sql, String.class);
        query.executeUpdate();
    }

    private void truncateTables(String schemaName) {
        getSchemaTables(schemaName).forEach(tableName ->{
            var sql=String.format("TRUNCATE TABLE %s RESTART IDENTITY", tableName);
            var query=this.entityManager.createNativeQuery(sql, String.class);
            query.executeUpdate();
            logger.info(String.format("Table %s truncated.",tableName));
        });
    }
    private void resetSequences(String schemaName) {
        getSchemaSequences(schemaName).forEach(sequenceName -> {
            var sql=String.format("ALTER SEQUENCE %s RESTART WITH 1", sequenceName);
            var query=this.entityManager.createNativeQuery(sql, String.class);
            query.executeUpdate();
            logger.info(String.format("Sequence %s truncated.",sequenceName));
        });

    }


    private Set<String> getSchemaTables(String schemaName) {
        String sql = String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA=:table_schema");
        var query=this.entityManager.createNativeQuery(sql, String.class);
        query.setParameter("table_schema", schemaName);
        return queryForList(query);
    }

    private Set<String> getSchemaSequences(String schemaName) {
        String sql = String.format("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA=:table_schema", schemaName);
        var query=this.entityManager.createNativeQuery(sql, String.class);
        query.setParameter("table_schema", schemaName);
        return queryForList(query);
    }

    private Set<String> queryForList(Query query) {
        var rs=query.getResultList();
        Set<String> tables = new HashSet<>(rs.size());
        tables.addAll(rs);
        return tables;
    }

}
