# TEIV docker-compose

The all the files in this directory should have everything you need to run TEIV with the default sql schemas or with
your own custom schemas generated from you own YANG files.

The docker-compose.yml file in its default state is set up to run TEIV with the default sql schemas generated from the
[default YANG files](../teiv/src/main/resources/models). When ``docker-compose up`` is run the following containers
may start and run:

| Container Name               | Image Name                                                                                                |
|------------------------------|-----------------------------------------------------------------------------------------------------------|
| kafka-producer               | confluentinc/cp-kafka:7.6.1 (optional - automatically populates TEIV)                                     |
| pgsql-schema-generator       | o-ran-sc/smo-teiv-pgsql-schema-generator:latest (optional - if you want to generate your own sql schemas) |
| kafka                        | confluentinc/cp-kafka:7.6.1                                                                               |
| kafka2                       | confluentinc/cp-kafka:7.6.1                                                                               |
| kafka3                       | confluentinc/cp-kafka:7.6.1                                                                               |
| topology-ingestion-inventory | o-ran-sc/smo-teiv-ingestion:latest                                                                        |
| topology-exposure-inventory  | o-ran-sc/smo-teiv-exposure:latest                                                                         |
| zookeeper                    | confluentinc/cp-zookeeper:6.2.1                                                                           |
| dbpostgresql                 | postgis/postgis:13-3.4-alpine                                                                             |

- **topology-ingestion-inventory** - consumes kafka messages, as a cloud events, to populate TEIV
- **topology-exposure-inventory** - allows the user to query TEIV and its data
- **kafka**, **kafka2**, **kafka3** - the three kafka brokers
- **zookeeper** - coordinates and manages the Kafka brokers
- **kafka-producer** - an optional service that will populate TEIV automatically with data present in
  [./cloudEventProducer/events](./cloudEventProducer/events)
- **dbpostgresql** - stores TEIV's data
- **pgsql-schema-generator** - an optional service that allows the user to supply their own YANG files to create their
  own SQL schemas

## Running with default sql schemas

Running with the default sql schemas provided by TEIV you should just run ``docker-compose up`` without any
modifications.
This will bring up TEIV populated with data, and you can start testing it immediately.
[Sample queries can be found here](https://lf-o-ran-sc.atlassian.net/wiki/spaces/SMO/pages/13567704/Sample+TEIV+Queries).

## Running with your own YANG models

**NB:** pgsql-schema-generator might fail due to a permission error, you can run ``sudo chmod -R 777 sql_scripts/`` to 
fix

Running with your own YANG models means TEIV has to generate sql schemas of those YANG files to use in TEIV. To do this
there is a pgsql-schema-generator service provided that will do this. To do this there are a few steps to follow:

1. Delete the current sql schemas from [./sql_scripts](./sql_scripts)
2. Provide your own YANG files in [./generate-defaults](./generate-defaults)
3. Uncomment pgsql-schema-generator from [./docker-compose.yml](./docker-compose.yml) **AND** the depends on in
   dbpostgresql
4. Comment out kafka-producer **OR** provide your own event that will work with the generated sql schema

You should now be able to run ``docker-compose up`` with your own YANG models
[Sample queries can be found here](https://lf-o-ran-sc.atlassian.net/wiki/spaces/SMO/pages/13567704/Sample+TEIV+Queries).