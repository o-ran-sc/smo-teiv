PG SQL Schema Generator
#######################

*PG SQL Schema Generator* provides the capability of generating a PostgreSQL schema from the YANG models. This schema
in turn is used in TE&IV for validating, ingesting and exposing the topology.

Algorithm
*********

Overview
========

The *PG SQL Schema Generator* necessitates the execution of multiple processes, each designed to fulfill distinct tasks
ensuring the thorough completion of their designated tasks for the complete schema generation flow. The various stages
involved in the schema generation are:

- :ref:`Model information retrieval <Model Information Retrieval>`
- :ref:`DB schema generation <PG SQL Schema Generation>`

The logic behind each stage is explained below.

Prerequisite
============

The main input for schema generation is the YANG modules. In order to start the process, we need to configure the path
which contains all the YANG modules that should be considered for DB schema generation.

The configuration is done in application.yaml as follows:

.. code-block:: yaml

    yang-model:
      source: classpath:generate-defaults

Model Information Retrieval
===========================

The models are used for identifying the entities & relationships information. For schema generation we need
the following information:

- modules
- entities
- relationships

The logic for retrieving the above information is explained below.

Modules
-------

Modules are identified with the help of the YANG parser.

Refer YangParser.java "src/main/java/org/oran/smo/teiv/pgsqlgenerator/YangParser.java" for implementation.

A module is constructed with the following details:

- name: name of the module.
- namespace: namespace of the module.
- domain: domain of the module. Identified with the help of the statement 'domain' from the module 'o-ran-smo-teiv-common-yang-extensions'
- revision: module revision.
- content: content of the module.
- availableListElements: set to all the list elements defined in the module. Identified with the help of the statement with 'list' as the yang DOM element name.
- availableEntities: Initially constructed as empty list. This will be populated later with all the entities defined in the module.
- availableRelations: set to the list of all relationship names defined in the module. Identified with the help of the statement name 'or-teiv-yext:biDirectionalTopologyRelationship'
- includedModules: set to the list of all the imported modules in the domain.

Entity Types
------------

Entity types are identified from the yang.

An entity type is constructed with the following details:

- entityName: name of the entity.
- storedAt: where the entity information is stored i.e., table name. The table name is generated as
  '<moduleName>_<entityName>'.
- moduleReferenceName: module to which the entity belongs. Identified by checking which of the identified modules has:

   - the same namespace as the entity, and
   - the availableListElements contains the entity name

- consumerData: sourceIds, classifiers and decorators.
- attributes: attributes for the entity. Retrieval of attribute information is detailed in the next section.

Attributes
^^^^^^^^^^

For every identified entity, we also retrieve the attributes belonging to it. An attribute is constructed with the
following information:

- name: name of the attribute
- dataType: dataType of the attribute. The datatype from the model is mapped to the corresponding DB datatype as shown in the below table:

  +-----------------------+----------------+
  | Model Attribute Types | Database Types |
  +=======================+================+
  | STRING                | TEXT           |
  +-----------------------+----------------+
  | COMPLEX_REF           | jsonb          |
  +-----------------------+----------------+
  | DOUBLE                | DECIMAL        |
  +-----------------------+----------------+
  | LONG                  | BIGINT         |
  +-----------------------+----------------+
  | ENUM_REF              | TEXT           |
  +-----------------------+----------------+
  | MO_REF                | TEXT           |
  +-----------------------+----------------+
  | INTEGER               | INTEGER        |
  +-----------------------+----------------+
  | GEO_LOCATION          | GEOGRAPHY      |
  +-----------------------+----------------+

  **Note:** ID model attribute type is mapped to TEXT datatype as part of this algorithm.

- constraints: list of constraints applicable for the attribute.
- defaultValue: default value of the attribute.
- indexTypes: indexes applicable for the attribute. Refer :ref:`Indexing Support <Indexing Support>` for more details on index.

Relationship Types
------------------

Relationship types information is retrieved from the model. The model doesn't support retrieval of relationships
directly, hence we get them by finding the outgoing associations for the identified entities.

A relationship type is constructed with the following information:

- name: name of the relationship
- aSideAssociationName: name of the aSide association.
- aSideMOType: aSide entity type.
- aSideModule: module to which aSide entity type belongs.
- aSideMinCardinality: minimum cardinality of the aSide.
- aSideMaxCardinality: maximum cardinality of the aSide.
- bSideAssociationName: name of the bSide association.
- bSideMOType: bSide entity type.
- bSideModule: module to which bSide entity type belongs.
- bSideMinCardinality: minimum cardinality of the bSide.
- bSideMaxCardinality: maximum cardinality of the bSide.
- associationKind: association kind. eg, 'BI_DIRECTIONAL'.
- connectSameEntity: whether the relationship connects the same entity type.
- relationshipDataLocation: type of the table used for storing the relationship instances. Can be one of the following:

   - A_SIDE
   - B_SIDE
   - RELATION

- storedAt: table name where the relationship instances is stored. The logic for determining the table name relies on the cardinality of the relationship.

  +--------------------------------------------------+----------------------------+
  | Case                                             | Relationship instance info |
  +==================================================+============================+
  | 1:1                                              | aSide                      |
  +--------------------------------------------------+----------------------------+
  | 1:N / N:1                                        | N-side                     |
  +--------------------------------------------------+----------------------------+
  | N:M                                              | relation table             |
  +--------------------------------------------------+----------------------------+
  | Relations connecting same Entity Types           | relation table             |
  | 1 : 1 (or) 1 : n (or) m : n                      |                            |
  +--------------------------------------------------+----------------------------+

- moduleReferenceName: module to which the relationship belongs. The relationship module is identified by identifying the module that contains the relationship name in the availableRelations list.
- consumerData: sourceIds, classifiers, decorators.
- aSideStoredAt: table name where aSide entity type instances are stored.
- bSideStoredAt: table name where bSide entity type instances are stored.

Indexing Support
----------------

Indexing is supported for the identified column's based on the column's data type.

Currently, we support indexing on JSONB columns.

- GIN Index: used for columns storing object, eg, decorators.
- GIN TRIGRAM Index: used for columns storing list of entries, eg, classifiers, sourceIds.

Refer IndexType.java "src/main/java/org/oran/smo/teiv/pgsqlgenerator/schema/IndexType.java" for types of index supported.

PG SQL Schema Generation
========================

Data schema
-----------

The information gathered from the model is then used to generate the TE&IV data schema
by creating tables from entities and relationships which is needed for persisting data,
this is performed in numerous steps.

Firstly, the data schema is prepared for use this is done by checking if a baseline data schema
file already exists. If it does not exist or if it's a green field installation,
it copies a skeleton data schema file to the new data schema file location.
Otherwise, if the baseline data schema file exists, it copies it to the new data schema file location.

Once the data schema is prepared the entities and relationships retrieved from the model need to be converted
into structured tables suitable for database storage. It starts by analyzing the relationships
between entities to determine the appropriate tables for storing relationship data,
considering various connection types such as one-to-one, one-to-many, many-to-one and many-to-many.

Next, it iterates over the entities generating the tables and columns based on their attributes.
For each entity, it creates a table with columns representing its attributes and columns to accommodate
associated relationships, ensuring adequate capturing of the relationships between entities.
In the case where there is many-to-many relationships or relationships between same entity type
these relationships are granted their own tables.

For every entity and relationship identified from the model, we add additional columns to store **sourceIds**,
**classifiers** and **decorators** information. This hard coding is necessary as sourceIds, classifiers and decorators
are not transformed as part of the yang model as it is for now considered consumer data.

+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| Column name                             | Type  | Default       | Description                                                                               |
|                                         |       |               |                                                                                           |
|                                         |       | Value         |                                                                                           |
+=========================================+=======+===============+===========================================================================================+
| CD_sourceIds                            | jsonb | []            | Stores sourceIds for entities in entities table and relationships in relationship tables. |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| REL_CD_sourceIds_<RELATIONSHIP_NAME>    | jsonb | []            | Stores sourceIds for relationship inside an entity table.                                 |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| CD_classifiers                          | jsonb | []            | Stores classifiers for entities in entities table.                                        |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| REL_CD_classifiers_<RELATIONSHIP_NAME>  | jsonb | []            | Stores classifiers for relationship inside an entity table.                               |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| CD_decorators                           | jsonb | {}            | Stores decorator for entities in entities table.                                          |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+
| REL_CD_decorators_<RELATIONSHIP_NAME>   | jsonb | {}            | Stores decorator for relationship inside an entity table.                                 |
+-----------------------------------------+-------+---------------+-------------------------------------------------------------------------------------------+

When it comes to data integrity, constraints are applied to the columns. These constraints include the following:

- **Primary keys:** Used to uniquely identify each record.
- **Foreign keys:** Used for establishing relationships between tables.
- **Uniqueness:** Used to ensure data population and prevent duplicated data.

After this, tables are retrieved from the baseline schema by extracting and parsing the data. This is done by identifying various statements such as table creation, column definitions, constraints, indexes and default values from the retrieved schema file. From this it generates a comprehensive list of tables along with their respective columns and constraints.

A comparison then happens between the tables from the baseline schema and the model service by performing the following actions:

- Identify differences between the tables
- Check table / column consistency
- Verify default values and label any discrepancies
- Verify any changes in the index

The differences from this operation are then used for schema generation by generating PG SQL statements to modify/create database schema based on the identified differences between the models. It first analyzes the differences and then generates appropriate SQL statements for alterations or creations of tables and columns.

These statements cater for the following scenarios:

- Adding new tables / columns
- Constraint definition such as UNIQUE or NOT NULL
- Default value handling
- Existing attributes modification
- Index definition

Finally, the generated schema is written into the prepared SQL file.

Model Schema
------------

Following this procedure, it then proceeds to produce the TE&IV model schema by crafting SQL entries for diverse tables associated with the model, which in turn is used for dynamically loading data in schema service at start up for modules, entities and relationships.

These SQL entries include:

**execution_status:** This table helps in storing the execution status of the schema. This will be used in the kubernetes init containers to confirm the successful execution of the schema.

+---------------+--------------------------+--------------------------------+
| Column name   | Type                     | Description                    |
+===============+==========================+================================+
| schema        | VARCHAR(127) PRIMARY KEY | Name of the schema             |
+---------------+--------------------------+--------------------------------+
| status        | VARCHAR(127)             | Status of the schema execution |
+---------------+--------------------------+--------------------------------+

**hash_info:** Postgres sets a limit of 63 characters for names of the columns, tables and constraints. Characters after the 63rd character are truncated. Names that are longer than 63 characters are hashed using SHA-1 hashing algorithm and used. _hash_info_ tables holds the name, hashedValue and the type of the entry.

Sample entries:

- **Hashed**: UNIQUE_GNBCUUPFunction_REL_ID_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION, UNIQUE_BDB349CDF0C4055902881ECCB71F460AE1DD323E, CONSTRAINT
- **Un-hashed**: NRSectorCarrier, NRSectorCarrier, TABLE

+---------------+--------------------------+-----------------------------------------------------------+
| Column name   | Type                     | Description                                               |
+===============+==========================+===========================================================+
| name          | TEXT PRIMARY KEY         | Table / column / constraint name                          |
+---------------+--------------------------+-----------------------------------------------------------+
| hashedValue   | VARCHAR(63) NOT NULL     | | Hashed version of name column value if over 63          |
|               |                          | | character otherwise same un-hashed value                |
+---------------+--------------------------+-----------------------------------------------------------+
| type          | VARCHAR(511)             | | The type of information associated i.e. Table, column   |
|               |                          | | or constraint                                           |
+---------------+--------------------------+-----------------------------------------------------------+

**module_reference:** For the module reference related module names from provided entities retrieved from the model service are extracted and stored which will be used for execution to module_reference table.

+-----------------+-----------------------+------------------------------------------------+
| Column name     | Type                  | Description                                    |
+=================+=======================+================================================+
| name            | TEXT PRIMARY KEY      | The module name                                |
+-----------------+-----------------------+------------------------------------------------+
| namespace       | TEXT                  | The namespace the module is located            |
+-----------------+-----------------------+------------------------------------------------+
| domain          | TEXT                  | The domain the module is a part of             |
+-----------------+-----------------------+------------------------------------------------+
| includedModules | jsonb                 | | aSideMO's and bSideMO's module reference     |
|                 |                       | | name stored within the Module                |
+-----------------+-----------------------+------------------------------------------------+
| revision        | TEXT NOT NULL         | The revision date of the file                  |
+-----------------+-----------------------+------------------------------------------------+
| content         | TEXT NOT NULL         | | The base64 encoded format of the             |
|                 |                       | | corresponding schema.                        |
+-----------------+-----------------------+------------------------------------------------+
| ownerAppId      | VARCHAR(511) NOT NULL | The identity of the owner App.                 |
+-----------------+-----------------------+------------------------------------------------+
| status          | VARCHAR(127) NOT NULL | | Current status of the module reference to    |
|                 |                       | | track during the pod's life cycle. Needed    |
|                 |                       | | to avoid data loss / corruption.             |
+-----------------+-----------------------+------------------------------------------------+

**entity_info:** For the entity info generation SQL entries are created and stored which will be used for execution to populate entity_info table.

+------------------------------------------+------------------+-----------------------------------------+
| Column name                              | Type             | Description                             |
+==========================================+==================+=========================================+
| name                                     | TEXT NOT NULL    | The entity type name                    |
+------------------------------------------+------------------+-----------------------------------------+
| moduleReferenceName                      | TEXT NOT NULL    | A reference to an associated module     |
+------------------------------------------+------------------+-----------------------------------------+
| | FOREIGN KEY ("moduleReferenceName")    | FOREIGN KEY      | Foreign key constraint                  |
| | REFERENCES ties_model.module_reference |                  |                                         |
| | ("name") ON DELETE CASCADE             |                  |                                         |
+------------------------------------------+------------------+-----------------------------------------+

**relationship_info:** When it comes to relationship info generation module reference names are assigned to relationships. For each relationship the max cardinality is taken and then sorted depending on the connection type:

+------------------------------------------+------------------+-------------------------------------------------------------------+
| Column name                              | Type             | Description                                                       |
+==========================================+==================+===================================================================+
| name                                     | TEXT PRIMARY KEY | The name of the relationship                                      |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| aSideAssociationName                     | TEXT NOT NULL    | The association name for the A-side of the relationship           |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| aSideMOType                              | TEXT NOT NULL    | The type of the managed object on the A-side of the relationship  |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| aSideModule                              | TEXT NOT NULL    | The aSide module name                                             |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| aSideMinCardinality                      | BIGINT NOT NULL  | The minimum cardinality of the A-side of the relationship         |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| aSideMaxCardinality                      | BIGINT NOT NULL  | The maximum cardinality of the A-side of the relationship         |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| bSideAssociationName                     | TEXT NOT NULL    | The association name for the B-side of the relationship           |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| bSideMOType                              | TEXT NOT NULL    | The type of the managed object on the B-side of the relationship  |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| bSideModule                              | TEXT NOT NULL    | The bSide module name                                             |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| bSideMinCardinality                      | BIGINT NOT NULL  | The minimum cardinality of the B-side of the relationship         |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| bSideMaxCardinality                      | BIGINT NOT NULL  | The maximum cardinality of the B-side of the relationship         |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| associationKind                          | TEXT NOT NULL    | The kind of association between entities                          |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| relationshipDataLocation                 | TEXT NOT NULL    | Indicates where associated relationship data is stored            |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| connectSameEntity                        | BOOLEAN NOT NULL | Indicates whether the relationship connects the same entity       |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| moduleReferenceName                      | TEXT PRIMARY KEY | The name of the module reference associated with the relationship |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| | FOREIGN KEY ("aSideModule") REFERENCES | FOREIGN KEY      | Foreign key constraint                                            |
| | ties_model.module_reference ("name")   |                  |                                                                   |
| | ON DELETE CASCADE                      |                  |                                                                   |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| | FOREIGN KEY ("bSideModule") REFERENCES | FOREIGN KEY      | Foreign key constraint                                            |
| | ties_model.module_reference ("name")   |                  |                                                                   |
| | ON DELETE CASCADE |                    |                  |                                                                   |
+------------------------------------------+------------------+-------------------------------------------------------------------+
| | FOREIGN KEY ("moduleReferenceName")    |FOREIGN KEY       | Foreign key constraint                                            |
| | REFERENCES                             |                  |                                                                   |
| | ties_model.module_reference ("name")   |                  |                                                                   |
| | ON DELETE CASCADE                      |                  |                                                                   |
+------------------------------------------+------------------+-------------------------------------------------------------------+

Along with this, it ensures that the structure for the model schema SQL file starts with the correct structure by importing the baseline schema information.

Finally, these generated entries and structure are then used to modify the model SQL file.

Consumer Data Schema
^^^^^^^^^^^^^^^^^^^^

Before classifying entities or relationships, a schema must be created and validated.
It can be created, by using its own endpoint, with a Yang Module.
The user must provide a unique module name, to avoid collision of multiple users access that are defining classifiers and decorators.
The schema cannot be modified later on but only deleted and recreated, if needed.
When a schema is successfully created and validated, the user can add the classifiers to the entities or relationships.

Classifiers ref
"""""""""""""""

:ref:`Classifiers Reference <classifiers>`

Decorators ref
""""""""""""""

:ref:`Decorators Reference <decorators>`

The SQL entries for consumer data include
- **module_reference:** For the consumer module reference related module names from provided classifiers or decorators retrieved from the model service are extracted and stored which will be used for execution to module_reference table.

+-------------+-----------------------+-----------------------------------------------------------------+
| Column name | Type                  | Description                                                     |
+=============+=======================+=================================================================+
| name        | TEXT PRIMARY KEY      | The module name                                                 |
+-------------+-----------------------+-----------------------------------------------------------------+
| namespace   | TEXT                  | The namespace the module is located                             |
+-------------+-----------------------+-----------------------------------------------------------------+
| revision    | TEXT NOT NULL         | The revision date of the file                                   |
+-------------+-----------------------+-----------------------------------------------------------------+
| content     | TEXT NOT NULL         | The base64 encoded format of the corresponding schema.          |
+-------------+-----------------------+-----------------------------------------------------------------+
| ownerAppId  | VARCHAR(511) NOT NULL | The identity of the owner App.                                  |
+-------------+-----------------------+-----------------------------------------------------------------+
| status      | VARCHAR(127) NOT NULL | | Current status of the consumer module reference to track      |
|             |                       | | during the pod's life cycle. Needed to avoid data             |
|             |                       | | loss / corruption.                                            |
+-------------+-----------------------+-----------------------------------------------------------------+

**decorators:** There will be the ability for Administrators to decorate topology entities and relationships. We will be storing the schemas for the decorators in this table.

+--------------------------------------------------+------------------+-----------------------------------+
| Column name                                      | Type             | Description                       |
+==================================================+==================+===================================+
| name                                             | TEXT PRIMARY KEY | The key of the decorator.         |
+--------------------------------------------------+------------------+-----------------------------------+
| dataType                                         | VARCHAR(511)     | | The data type of the decorator, |
|                                                  |                  | | needed for parsing.             |
+--------------------------------------------------+------------------+-----------------------------------+
| moduleReferenceName                              | TEXT             | | References the corresponding    |
|                                                  |                  | | module reference the decorator  |
|                                                  |                  | | belongs to.                     |
+--------------------------------------------------+------------------+-----------------------------------+
| | FOREIGN KEY ("moduleReferenceName") REFERENCES | FOREIGN KEY      | Foreign key constraint            |
| | ties_consumer_data.module_reference ("name")   |                  |                                   |
| | ON DELETE CASCADE                              |                  |                                   |
+--------------------------------------------------+------------------+-----------------------------------+

**classifier:** There will be the ability for client applications to apply user-defined keywords/tags (classifiers) to topology entities and relationships. We will be storing the schemas for the classifiers in this table.

+--------------------------------------------------+------------------+-----------------------------------+
| Column name                                      | Type             | Description                       |
+==================================================+==================+===================================+
| name                                             | TEXT PRIMARY KEY | The key of the classifier.        |
+--------------------------------------------------+------------------+-----------------------------------+
| moduleReferenceName                              | TEXT             | | References the corresponding    |
|                                                  |                  | | module reference the classifier |
|                                                  |                  | | belongs to.                     |
+--------------------------------------------------+------------------+-----------------------------------+
| | FOREIGN KEY ("moduleReferenceName") REFERENCES | FOREIGN KEY      | Foreign key constraint            |
| | ties_consumer_data.module_reference ("name")   |                  |                                   |
| | ON DELETE CASCADE                              |                  |                                   |
+--------------------------------------------------+------------------+-----------------------------------+

How to use classifiers and decorators ref
"""""""""""""""""""""""""""""""""""""""""

:ref:`How to use classifiers and decorators Reference <how to use classifiers and decorators>`

Skeleton Data and Model SQL Files
=================================

- 00_init-oran-smo-teiv-data.sql "src/main/resources/scripts/00_init-oran-smo-teiv-data.sql"

  Proprietary PG SQL Function

  Create constant if it doesn't exist

  .. code-block:: sql

    CREATE OR REPLACE FUNCTION ties_data.create_constraint_if_not_exists (
    t_name TEXT, c_name TEXT, constraint_sql TEXT
    )
    RETURNS void AS
    BEGIN
      IF NOT EXISTS (SELECT constraint_name FROM information_schema.table_constraints
      WHERE table_name = t_name AND constraint_name = c_name) THEN
        EXECUTE constraint_sql;
      END IF;
    END;

  Example:

  .. code-block:: sql

    SELECT ties_data.create_constraint_if_not_exists(
        'CloudNativeApplication',
    'PK_CloudNativeApplication_id',
    'ALTER TABLE ties_data."CloudNativeApplication" ADD CONSTRAINT "PK_CloudNativeApplication_id" PRIMARY KEY ("id");'
    );

- "01_init-oran-smo-teiv-model.sql "src/main/resources/scripts/01_init-oran-smo-teiv-model.sql"

Unsupported Non-Backward Compatible(NBC) Model Changes
======================================================

The following NBC model changes are unsupported due to their actions resulting in issues for upgrade scenarios.

+-------------------------------------------------------+
| Change                                                |
+=======================================================+
| Delete attributes / entities / relationships          |
+-------------------------------------------------------+
| Modify constraints on the attributes / relationships  |
+-------------------------------------------------------+
| Change datatype of the attributes                     |
+-------------------------------------------------------+
| Rename attributes / relationships / entities          |
+-------------------------------------------------------+
| Change aSide / bSide associated with a relationship   |
+-------------------------------------------------------+
| Change cardinality of aSide / bSide in a relationship |
+-------------------------------------------------------+

There are checks in place to identify any NBC model change from above. These checks will compare the extracted data from baseline schema with data from model service to identify NBC model changes.

NBC checks:

- Verify deletion or modification to any attribute / entities / relationships and their properties.
- Validate constraints on attributes / relationships.
- Identify change to aSide / bSide managed object associated with a relationship.
- Verify cardinality constraints to aSide/bSide of a relationship.

If there is a requirement to update schema with NBC changes, in such case green field installation must be turned on. Green field installation enables the PG SQL Schema generator service to construct a new schema from scratch rather than updating on top of existing baseline schema.

Please refer to BackwardCompatibilityChecker.java "src/main/java/org/oran/smo/teiv/pgsqlgenerator/schema/BackwardCompatibilityChecker.java" for more info.

Local Use
=========

Copy YANG models into the generate-defaults "src/main/resources/generate-defaults" directory. Once done, perform the schema generation process by running the Spring Boot application within the pgsql-schema-generator directory using *mvn spring-boot:run*. The command will also run the Spring Boot tests and output the results.

To run the test suite:

- In your terminal, navigate into the pgsql-schema-generator directory and run 'mvn clean install'
- In your terminal, navigate into the pgsql-schema-generator directory and run 'mvn -Dtest=<Test Name> test'