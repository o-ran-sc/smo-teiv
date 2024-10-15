# PGSQL schema generator
During the `generate-resources` phase of the Maven build pgsql-schema-generator will run (see `exec-maven-plugin` in the
`teiv` POM), v1 of the SQL files in this directory are **automatically overwritten** if there are changes to the YANG
models.

**NOTE**: After regenerating SQL files, ensure the new v1 SQL files are checked in to version control. This ensures
that the generated SQL files remain in sync with the resource SQL files.
