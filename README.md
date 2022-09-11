# SpringBatchFlatFiles
Applications.properties:
#spring config
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

#datasource config
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=abhishek
spring.datasource.password=manager
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Required Starters:
(1) Spring Batch
(2) oracle Driver
(3) lombok api
(4) jdbc api
