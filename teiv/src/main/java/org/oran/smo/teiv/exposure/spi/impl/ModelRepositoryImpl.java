/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  SPDX-License-Identifier: Apache-2.0
 *  ============LICENSE_END=========================================================
 */
package org.oran.smo.teiv.exposure.spi.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TiesConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TiesConstants.IN_USAGE;
import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.SCHEMA_ALREADY_EXISTS;
import static org.oran.smo.teiv.utils.TiesConstants.SEMICOLON_SEPARATION;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_MODEL;
import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.exposure.spi.ModuleStatus;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("exposure")
public class ModelRepositoryImpl implements ModelRepository {
    public static final String STATUS = "status";
    public static final String REVISION = "revision";
    public static final String NAME = "name";

    private final DSLContext readDataDslContext;
    private final DSLContext readWriteDataDslContext;
    private final DSLContext writeDataDslContext;

    @Override
    public boolean doesModuleExists(final String schemaName, final String name) {
        final SelectConditionStep<Record1<Integer>> query = readDataDslContext.selectOne().from(String.format(schemaName,
                MODULE_REFERENCE)).where(field(name(NAME)).eq(name));
        return query.fetchAny() != null;
    }

    @Override
    public Optional<Module> getConsumerModuleByName(final String name) {
        final Record moduleRecord = runMethodSafe(() -> readDataDslContext.select().from(table(String.format(
                TIES_CONSUMER_DATA, MODULE_REFERENCE))).where(field("name").eq(name)).and(field(STATUS).eq(
                        ModuleStatus.IN_USAGE.name())).fetchAny());
        return Optional.ofNullable(moduleRecord).map(module -> Module.builder().name((String) module.get(NAME)).namespace(
                (String) module.get("namespace")).revision((String) module.get(REVISION)).ownerAppId((String) module.get(
                        "ownerAppId")).status(ModuleStatus.valueOf((String) module.get(STATUS))).build());
    }

    @Override
    public List<Module> getModules() {
        return runMethodSafe(() -> {
            final List<Module> modules = new ArrayList<>();
            final List<Module> modulesFromModelSchema = getModulesBySchema(TIES_MODEL);
            final List<Module> modulesFromConsumerDataSchema = getModulesBySchema(TIES_CONSUMER_DATA);
            modules.addAll(modulesFromModelSchema);
            modules.addAll(modulesFromConsumerDataSchema);
            return modules;
        });
    }

    @Override
    public List<Module> getDeletingModulesOnStartup() {
        return runMethodSafe(() -> {
            Select<Record> moduleRecords = readDataDslContext.select().from(table(String.format(TIES_CONSUMER_DATA,
                    MODULE_REFERENCE))).where(field(STATUS).eq(ModuleStatus.DELETING.name()));
            Function<Record, Module> buildModuleFunction;
            buildModuleFunction = buildConsumerModules();
            return moduleRecords.stream().map(buildModuleFunction).toList();
        });
    }

    @Override
    public String getModuleContentByName(final String name) {
        return runMethodSafe(() -> {
            String content = getModuleContentByNameFromSchema(TIES_MODEL, name);
            if (content == null || content.equals("")) {
                content = getModuleContentByNameFromSchema(TIES_CONSUMER_DATA, name);
            }
            return content;
        });
    }

    private void createModule(DSLContext transactionalDSL, Module module) {
        createModuleWithDuplicateCheck(() -> transactionalDSL.insertInto(table(String.format(TIES_CONSUMER_DATA,
                MODULE_REFERENCE))).set(field(NAME), module.getName()).set(field("content"), module.getContent()).set(field(
                        "\"ownerAppId\""), module.getOwnerAppId()).set(field(STATUS), module.getStatus().name()).set(field(
                                REVISION), module.getRevision()).execute(), module.getName());
    }

    @Override
    public void updateModuleStatus(String name, ModuleStatus status) {
        runMethodSafe(() -> writeDataDslContext.update(table(String.format(TIES_CONSUMER_DATA, MODULE_REFERENCE))).set(
                field(STATUS), status.name()).where(field("name").eq(name)).execute());
    }

    @Override
    public void deleteModuleByName(String name) {
        runMethodSafe(() -> writeDataDslContext.deleteFrom(table(String.format(TIES_CONSUMER_DATA, MODULE_REFERENCE)))
                .where(field("name").eq(name)).execute());
    }

    private List<Module> getModulesBySchema(final String schemaName) {
        Select<Record> moduleRecords = readDataDslContext.select().from(table(String.format(schemaName, MODULE_REFERENCE)))
                .where(noCondition());
        Function<Record, Module> buildModuleFunction;

        if (schemaName.equals(TIES_CONSUMER_DATA)) {
            moduleRecords = moduleRecords.$where((field(STATUS).eq(IN_USAGE)));
            buildModuleFunction = buildConsumerModules();
        } else {
            buildModuleFunction = buildBuiltInModule();
        }

        return moduleRecords.stream().map(buildModuleFunction).toList();
    }

    private Function<Record, Module> buildBuiltInModule() {
        return moduleRecord -> Module.builder().name((String) moduleRecord.get(NAME)).namespace((String) moduleRecord.get(
                "namespace")).domain((String) moduleRecord.get("domain")).revision((String) moduleRecord.get(REVISION))
                .build();
    }

    private Function<Record, Module> buildConsumerModules() {
        return moduleRecord -> Module.builder().name((String) moduleRecord.get(NAME)).namespace((String) moduleRecord.get(
                "namespace")).revision((String) moduleRecord.get(REVISION)).ownerAppId((String) moduleRecord.get(
                        "ownerAppId")).status(ModuleStatus.valueOf((String) moduleRecord.get(STATUS))).build();
    }

    private String getModuleContentByNameFromSchema(final String schemaName, final String name) {
        return readDataDslContext.select(field("content")).from(table(String.format(schemaName, MODULE_REFERENCE))).where(
                field("name").eq(name)).limit(1).fetchAnyInto(String.class);
    }

    @Override
    public void createConsumerDataModule(Module module, List<String> classifiers, Map<String, String> decorators) {
        writeDataDslContext.transaction(configuration -> {
            DSLContext transactionalDSL = DSL.using(configuration);
            createModule(transactionalDSL, module);
            if (!classifiers.isEmpty()) {
                storeClassifiers(transactionalDSL, classifiers, module.getName());
            }
            if (!decorators.isEmpty()) {
                storeDecorators(transactionalDSL, decorators, module.getName());
            }
        });
        log.debug("Create consumer data module transaction successful for module {}", module);
    }

    private void storeClassifiers(final DSLContext transactionalDSL, final List<String> elements, final String moduleName) {
        log.debug("Storing classifiers {} for module {}", elements, moduleName);
        insertValues(transactionalDSL, CLASSIFIERS, List.of(NAME, String.format(QUOTED_STRING, MODULE_REFERENCE_NAME)),
                executable -> {
                    for (String element : elements) {
                        executable = executable.values(String.format(SEMICOLON_SEPARATION, moduleName, element),
                                moduleName);
                    }
                });
    }

    private void storeDecorators(final DSLContext transactionalDSL, final Map<String, String> elements,
            final String moduleName) {
        log.debug("Storing decorators {} for module {}", elements.keySet(), moduleName);
        insertValues(transactionalDSL, DECORATORS, List.of(NAME, String.format(QUOTED_STRING, "dataType"), String.format(
                QUOTED_STRING, MODULE_REFERENCE_NAME)), executable -> {
                    for (Map.Entry<String, String> element : elements.entrySet()) {
                        executable = executable.values(String.format(SEMICOLON_SEPARATION, moduleName, element.getKey()),
                                element.getValue(), moduleName);
                    }
                });
    }

    private void insertValues(final DSLContext transactionalDSL, final String tableName, final List<String> columns,
            final Consumer<InsertValuesStepN<?>> consumer) {
        InsertValuesStepN<?> executable = transactionalDSL.insertInto(table(String.format(TIES_CONSUMER_DATA, tableName)))
                .columns(columns.stream().map(column -> field(column, String.class)).toList());

        consumer.accept(executable);

        runMethodSafe(executable::execute);
    }

    private <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (Exception ex) {
            log.warn("Exception occurred during query execution.", ex);
            throw TiesException.serverSQLException();
        }
    }

    private <T> T createModuleWithDuplicateCheck(Supplier<T> supp, String moduleName) {
        log.debug("Creating module: {}", moduleName);
        try {
            return supp.get();
        } catch (IntegrityConstraintViolationException icve) {
            if (isModuleInDeletingState(moduleName)) {
                log.warn("Module in deleting state", icve);
                throw TiesException.schemaInDeletingState(moduleName);
            } else {
                log.warn("Module already exists", icve);
                throw TiesException.invalidFileInput(SCHEMA_ALREADY_EXISTS);
            }
        } catch (Exception ex) {
            log.warn("Exception occurred during query execution.", ex);
            throw TiesException.serverSQLException();
        }
    }

    private boolean isModuleInDeletingState(String moduleName) {
        final SelectConditionStep<Record1<Integer>> query = readDataDslContext.selectOne().from(String.format(
                TIES_CONSUMER_DATA, MODULE_REFERENCE)).where(field(name(NAME)).eq(moduleName).and(field(STATUS).eq(
                        ModuleStatus.DELETING.name())));
        return query.fetchAny() != null;
    }
}
