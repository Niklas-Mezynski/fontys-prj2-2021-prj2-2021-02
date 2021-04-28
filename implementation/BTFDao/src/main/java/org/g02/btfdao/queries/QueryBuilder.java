package org.g02.btfdao.queries;

import org.g02.btfdao.utils.Mapper;
import org.g02.btfdao.utils.SQLField;
import org.g02.btfdao.dao.Savable;

import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {
    public static boolean createDropStatements = false;

    /**
     * @param entityType The entity Class that the Get Statement is for
     * @return The SQL String with placeholders to fill in
     */
    public String createGetSQL(Class<? extends Savable> entityType) {
        String template = "select %1$s from %2$s where %3$s";
        return String.format(template,
                allColumnNames(entityType),
                Mapper.getSQLTableName(entityType),
                String.join(" and ", makeSetToPlaceholders(Mapper.getPrimaryKeyFields(entityType)))
        );
    }

    public String createGetAllSQL(Class<? extends Savable> entityType) {
        String template = "select %1$s from %2$s";
        return String.format(template,
                Mapper.getPrimaryKeyFields(entityType).stream().map(SQLField::getSQLFieldName).collect(Collectors.joining(", ")),
                Mapper.getSQLTableName(entityType)
        );
    }

    public String createInsertSQL(Class<? extends Savable> entityType) {
        var fields = Mapper.getAllSQLFields(entityType).stream()
                .filter(SQLField::isInsertable)
                .collect(Collectors.toList());
        String template =
                "insert into %1$s (%2$s) values (%3$s) returning %4$s";

        return String.format(template,
                Mapper.getSQLTableName(entityType), //1
                fields.stream() //2
                        .map(SQLField::getSQLFieldName)
                        .collect(Collectors.joining(", ")),
                makePlaceholderString(fields),
                allColumnNames(entityType)
        );
    }

    public String createUpdateSQL(Class<? extends Savable> entityType) {
        String template = "update %1$s set %2$s where %3$s returning %4$s"; //1 = table name 2 = all "value=?" 3 = pkmatches 4= pks
        return String.format(template,
                Mapper.getSQLTableName(entityType),
                String.join(", ", makeSetToPlaceholders(Mapper.getAllSQLFields(entityType).stream()
                        .filter(SQLField::isPrimitive)
                        .collect(Collectors.toList()))),
                String.join(" and ", makeSetToPlaceholders(Mapper.getPrimaryKeyFields(entityType))),
                Mapper.getPrimaryKeyFields(entityType).stream().map(SQLField::getFieldName).collect(Collectors.joining(", "))
        );
    }

    public String createRemoveSQL(Class<? extends Savable> entityType) {
        String template = "delete from %1$s where %2$s"; //1= table name 2= pkmatches
        return String.format(template,
                Mapper.getSQLTableName(entityType),
                String.join(" and ", makeSetToPlaceholders(Mapper.getPrimaryKeyFields(entityType)))
        );
    }

    private String allColumnNames(Class<? extends Savable> entityType) {
        return Mapper.getAllSQLFields(entityType).stream()
                .filter(SQLField::isNotIgnored)
                .filter(SQLField::isPrimitive)
                .map(SQLField::getSQLFieldName)
                .collect(Collectors.joining(", "));
    }

    public String createRelationGetRightSQL(SQLField field) {
        String template = "select %1$s from %2$s where %3$s";//1=right pks 2=relation table name 3=left pks with "=?" and "and"
        return String.format(template,
                String.join(", ", field.getRelationTableRightPKnames()),
                field.getRelationTableName(),
                String.join(" and ", makeSetToPlaceholdersFromStrings(field.getRelationTableLeftPKnames()))
        );
    }

    public String createRelationInsertSQL(SQLField field) {
        String template = "insert into %1$s (%2$s, %3$s) values (%4$s, %5$s)"; //1=relation table name 2=left pk relation names 3=right pk relation names 4= combination of ?s
        return String.format(template,
                field.getRelationTableName(),
                String.join(", ", field.getRelationTableLeftPKnames()),
                String.join(", ", field.getRelationTableRightPKnames()),
                makePlaceholderString(field.getRelationTableLeftPKnames()),
                makePlaceholderString(field.getRelationTableRightPKnames())
        );
    }

    public String createRelationRemoveSQL(SQLField field) {
        String template = "delete from %1$s where %2$s";//1=relation table name 2=left pks
        return String.format(template,
                field.getRelationTableName(),
                String.join(" and ", makeSetToPlaceholdersFromStrings(field.getRelationTableLeftPKnames()))
        );
    }

    /**
     * @param classes a list of classes that should be included in the generated tables (weird things could happen if this list is incomplete)
     * @return a SQL-Script that initializes the DB
     */
    public String createTablesCreateStatement(List<Class<? extends Savable>> classes) {
        StringBuilder ret = new StringBuilder();
        for (Class<? extends Savable> aClass : classes) {
            if (createDropStatements)
                ret.append(String.format("drop table if exists %1$s cascade;\n", Mapper.getSQLTableName(aClass))); //remove the old table
            ret.append(createSingleTableCreateStatement(aClass)); //create the table script excluding non primitive types
//            ret.append(createSingleTableAlterStatements(aClass));
        }
        for (Class<? extends Savable> aClass : classes) { //after all base tables have been created
            ret.append(createRelationTablesCreateSQL(aClass));
        }
        return ret.toString();
    }

    private String createRelationTablesCreateSQL(Class<? extends Savable> entityType) {
        var fields = Mapper.getAllSQLFields(entityType).stream() //Fields that need a relationTable
                .filter(f -> !f.isPrimitive())
                .collect(Collectors.toList());
        StringBuilder ret = new StringBuilder();
        for (SQLField field : fields) {
            ret.append(createSingleRelationTableCreateSQL(field));
        }
        return ret.toString();
    }

    /**
     * @param field The field that this table should be generated for
     * @return a SQL statement that creates a "relation table" for a single field
     */
    private String createSingleRelationTableCreateSQL(SQLField field) {
        String template = "create table %1$s ( %2$s , %3$s,\n" + //1=Table Name 2=PrimaryKeys of this class(table) 3= PKs of referencing class(table)
                " FOREIGN KEY (%4$s) REFERENCES %5$s (%6$s),\n" + //4=left PrimaryKeys(relationNames) 5=leftTableName 6=left PrimaryKeys(normal Names)
                "FOREIGN KEY (%7$s) REFERENCES %8$s (%9$s)\n" + //7=right PrimaryKeys(relationNames) 8=rightTableName 9=right PrimaryKeys(normal Names)
                ");\n";
        if (createDropStatements) template = "drop table if exists %1$s cascade;\n" + template;
        String tablename = field.getRelationTableName();
        String leftPKs = String.join(", ", field.getRelationTableLeftPKnameswithType());
        String rightPKs = String.join(", ", field.getRelationTableRightPKnameswithType());
        return String.format(template,
                tablename,//1
                leftPKs,//2
                rightPKs,//3
                String.join(", ", field.getRelationTableLeftPKnames()),//4
                field.getEncapsulatedTableName(),//5
                Mapper.getPrimaryKeyFields(field.getFieldClass()).stream().map(SQLField::getSQLFieldName).collect(Collectors.joining(", ")),//6
                String.join(", ", field.getRelationTableRightPKnames()),//7
                Mapper.getSQLTableName(field.getReferencingClass()),//8
                Mapper.getPrimaryKeyFields(field.getReferencingClass()).stream().map(SQLField::getSQLFieldName).collect(Collectors.joining(", "))//9
        );
    }

    /**
     * @param entityType class for which to generate the table
     * @return a create table statement for the single table excluding things like primary key and relations between tables
     */
    private String createSingleTableCreateStatement(Class<? extends Savable> entityType) {
        var allSQLFields = Mapper.getAllSQLFields(entityType);
        String template = "create table %1$s (\n\t%2$s%3$s);\n";

        return String.format(template,
                Mapper.getSQLTableName(entityType),
                allSQLFields.stream()
                        .filter(SQLField::isNotIgnored)
                        .filter(SQLField::isPrimitive)
                        .map(f -> f.getSQLFieldName() + " " + f.getSQLType())
                        .collect(Collectors.joining(",\n\t")),
                createSingleTablePrimaryKeys(entityType)
        );
    }

    /**
     * @param entityType the entity class that is used
     * @return a properly formatted string of primary keys to be used in the table declaration
     */
    private String createSingleTablePrimaryKeys(Class<? extends Savable> entityType) {
        var fields = Mapper.getPrimaryKeyFields(entityType);
        if (fields.stream().filter(SQLField::isNotIgnored).noneMatch(SQLField::isPrimaryKey)) {
            return "//Danger, no pk set, relations might not work as intended\n"; //Warnung
        }
        return ",\n\t" + fields.stream()
                .filter(SQLField::isNotIgnored)
                .filter(SQLField::isPrimaryKey)
                .map(SQLField::getSQLFieldName)
                .collect(Collectors.joining(", ", "primary key (", ")\n"));
    }

    private String createSingleTableAlterStatements(Class<? extends Savable> entityType) {
        StringBuilder ret = new StringBuilder();

        return ret.toString();
    }

    private List<String> makeSetToPlaceholders(List<SQLField> fields) {
        return fields.stream()
                .map(f -> f.getSQLFieldName() + "=?")
                .collect(Collectors.toList());
    }

    private List<String> makeSetToPlaceholdersFromStrings(List<String> fields) {
        return fields.stream()
                .map(f -> f + "=?")
                .collect(Collectors.toList());
    }

    /**
     * @param fields a list of fields (only length is important)
     * @return a String containing the appropriate amount of placeholders for the provided fields
     */
//    private String makePlaceholderString(List<SQLField> fields){
//        return fields.stream().map(f->"?").collect(Collectors.joining(", "));
//    }
    private String makePlaceholderString(List<?> fields) {
        return fields.stream().map(f -> "?").collect(Collectors.joining(", "));
    }

}
