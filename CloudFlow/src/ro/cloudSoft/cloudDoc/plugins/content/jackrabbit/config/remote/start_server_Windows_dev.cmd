REM Server JackRabbit pentru DEZVOLTARE

SET CLOUD_DOC_JR_SERVER_JAR=jackrabbit-standalone-1.5.7.jar

SET CLOUD_DOC_JR_SERVER_PORT=8081

SET CLOUD_DOC_JR_ORACLE_JDBC_URL=jdbc:oracle:thin:@86.123.84.48:1521:xe
SET CLOUD_DOC_JR_ORACLE_USERNAME=clouddoc_dev
SET CLOUD_DOC_JR_ORACLE_PASSWORD=clouddoc_dev

SET CLOUD_DOC_JR_ORACLE_SCHEMA_OBJECT_PREFIX=X

java -Dapp.jackrabbit.cloud_doc_properties.oracle.jdbc_url=%CLOUD_DOC_JR_ORACLE_JDBC_URL% -Dapp.jackrabbit.cloud_doc_properties.oracle.username=%CLOUD_DOC_JR_ORACLE_USERNAME% -Dapp.jackrabbit.cloud_doc_properties.oracle.password=%CLOUD_DOC_JR_ORACLE_PASSWORD% -Dapp.jackrabbit.cloud_doc_properties.oracle.schema_object_prefix=%CLOUD_DOC_JR_ORACLE_SCHEMA_OBJECT_PREFIX% -jar %CLOUD_DOC_JR_SERVER_JAR% -p %CLOUD_DOC_JR_SERVER_PORT%