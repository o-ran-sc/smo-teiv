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
import static org.oran.smo.teiv.utils.TiesConstants.IN_USAGE;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_MODEL;
import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
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
        return runMethodSafe(() -> readWriteDataDslContext.select().from(table(String.format(TIES_CONSUMER_DATA,
                MODULE_REFERENCE))).where(field(STATUS).eq(ModuleStatus.DELETING.name())).fetchInto(Module.class));
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

    @Override
    public void createModule(Module module) {
        runMethodSafe(() -> writeDataDslContext.insertInto(table(String.format(TIES_CONSUMER_DATA, MODULE_REFERENCE))).set(
                field(NAME), module.getName()).set(field("content"), module.getContent()).set(field("\"ownerAppId\""),
                        module.getOwnerAppId()).set(field(STATUS), module.getStatus().name()).set(field(REVISION), module
                                .getRevision()).execute());
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

    private <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (Exception ex) {
            log.warn("Exception occurred during query execution.", ex);
            throw TiesException.serverSQLException();
        }
    }
}
