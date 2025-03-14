/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package contracts.schemas

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 201: Create a new classifiers + decorators schema with name module-rapp-model1"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model1 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model1"; ' +
                    '  prefix model1; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status CREATED()
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a new decorators without classifier schema with name module-rapp-model2"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model2 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model2"; ' +
                    '  prefix model2; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    ' ' +
                    '}'))))
        }
        response {
            status CREATED()
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a new classifiers without decorator schema with name module-rapp-model3"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model3 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model3"; ' +
                    '  prefix model3; ' +
                    '   ' +
                    '  import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '    identity classifierTest1 { ' +
                    '        base model:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base model:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status CREATED()
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to wrong inheritance for the classifiers"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model4 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model4"; ' +
                    '  prefix model4; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest1 { ' +
                    '        base model-test; ' +
                    '    } ' +
                    ' ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid schema name",
                "details": "Invalid schema name: Invalid classifier classifierTest1"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to wrong inheritance for the classifiers"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model5 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model5"; ' +
                    '  prefix model5; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf test1 { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf test2 { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf test3 { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base wrong-classifier; ' +
                    '  } ' +
                    '  ' +
                    '    identity urban { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity rural { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    ' ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid schema name",
                "details": "Invalid schema name: Invalid classifier geo-classifier"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to schema already exists"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model1 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model1"; ' +
                    '  prefix model1; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierDuplicate1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierDuplicate1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierDuplicate2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierDuplicate2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Schema already exists"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to schema already exists"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model1 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model1"; ' +
                    '  prefix model1; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  identity classifierDuplicate2 { ' +
                    '        base model:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierDuplicate2 { ' +
                    '        base model:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Schema already exists"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to invalid file input"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model1 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model1"; ' +
                    '  prefix model1; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf urban { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Schema already exists"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifiers and decorators schema, exception thrown due to wrong inheritance for the classifier"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model6 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model6"; ' +
                    '  prefix model6; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest1 { ' +
                    '        base model-test; ' +
                    '    } ' +
                    ' ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid schema name",
                "details": "Invalid schema name: Invalid classifier classifierTest1"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with already existing schema name"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module test-app-module { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:test-app-module"; ' +
                    '  prefix module; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Schema already exists"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with already existing schema name in model schema"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module o-ran-smo-teiv-ran { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:o-ran:smo-teiv-ran"; ' +
                    '  prefix module; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Module 'o-ran-smo-teiv-ran' multiple times in the input, with both conformance types IMPLEMENT and IMPORT."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid file type"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.json')),
                    contentType: p('application/json'),
                    content: $(c(regex(nonEmpty())),
                    p('{"sample1":"test", "sample2":99}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Invalid file"
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Empty file"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())))))
        }
        response {
            status BAD_REQUEST()
            body('''{
            "status": "BAD_REQUEST",
            "message": "Invalid file input",
            "details": "Invalid file input: Missing content at the beginning of the document."
        }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid leaf 1"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-x { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-module-x"; ' +
                    '  prefix module-x; ' +
                    '   ' +
                    '  import o-ran-smo-teiv-common-yang-types { prefix test; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-06-10" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /test:decorators { ' +
                    '        leaf select*fromocucpfunction { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf vendor { ' +
                    '            type string; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity Outdoor { ' +
                    '    base test:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity Rural { ' +
                    '        base test:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity Weekend { ' +
                    '        base test:classifiers; ' +
                    '    } ' +
                    ' ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: 'select*fromocucpfunction' is not a valid YANG identifier."
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid leaf 2"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-x2 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-module-x2"; ' +
                    '  prefix module-x2; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix test; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-06-10" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /test:decorators { ' +
                    '        leaf location { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf vendor { ' +
                    '            type string; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity UPDATEteiv_model.module_referenceSETstatusDELETINGWHEREnameodu-function-model { ' +
                    '    base test:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity Rural { ' +
                    '        base test:classifiers; ' +
                    '    } ' +
                    '   ' +
                    '  identity Weekend { ' +
                    '        base test:classifiers; ' +
                    '    } ' +
                    ' ' +
                    '}}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Unexpected content at end of document. Check curly braces balance throughout document."
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema without any given decorator or classifier"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model7 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model7"; ' +
                    '  prefix model7; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    ' ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest1 { ' +
                    '        base test-classifier; ' +
                    '    } ' +
                    ' ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Encountered '{}', which does nothing. Replace with ';' or un-comment the contents."
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema without any given decorator or classifier 2"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-module8 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-module8"; ' +
                    '  prefix module8; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Invalid schema"
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema missing leaf type for the decorators"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model9 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model9"; ' +
                    '  prefix model9; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '     ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Encountered '{}', which does nothing. Replace with ';' or un-comment the contents."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with wrong decorator type"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model10 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model10"; ' +
                    '  prefix model10; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type wrong; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Cannot resolve typedef 'wrong'."
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with wrong syntax (missing imports)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model11 { ' +
                    '   ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model11"; ' +
                    '  prefix model11; ' +
                    ' ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '  } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid file input",
                    "details": "Invalid file input: Invalid schema"
                }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with wrong syntax (missing revision)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model12 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model12"; ' +
                    '  prefix model12; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: (Sub-)Module does not have a 'revision' statement."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with wrong syntax (missing yang version, namespace, prefix)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-model13 { ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  ' +
                    '    identity classifierTest1 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest2 { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
            "status": "BAD_REQUEST",
            "message": "Invalid file input",
            "details": "Invalid file input: Invalid schema"
        }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a new classifier and decorator schema with wrong syntax (prefix is different then the inheritances)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module module-rapp-module14 { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:module-rapp-model14"; ' +
                    '  prefix module14; ' +
                    '   ' +
                    '  import o-ran-smo-teiv-common-yang-types { prefix testModel; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf test1 { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf test2 { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf test3 { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base geo-classifier; ' +
                    '  } ' +
                    '  ' +
                    '    identity urban { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    '   ' +
                    '  identity rural { ' +
                    '        base geo-classifier; ' +
                    '    } ' +
                    ' ' +
                    '}'))))
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid file input",
                "details": "Invalid file input: Path to schema node '/model:decorators', part of 'augment' statement, cannot be resolved."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 409: Create a new classifiers and decorators schema, exception thrown due to schema exists and is in deleting status"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/schemas")
            headers {
                contentType(multipartFormData())
            }
            multipart(
                    file: named(
                    name: $(c(regex(nonEmpty())), p('file.yang')),
                    contentType: p('application/yang'),
                    content: $(c(regex(nonEmpty())),
                    p('module test-app-in-deleting-status { ' +
                    '  ' +
                    '  yang-version 1.1; ' +
                    '  namespace "urn:o-ran:test-app-in-deleting-status"; ' +
                    '  prefix model1; ' +
                    '   ' +
                    '    import o-ran-smo-teiv-common-yang-types { prefix model; } ' +
                    '  import o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; } ' +
                    '   ' +
                    '  revision "2024-05-08" { ' +
                    '    description ' +
                    '    "Initial revision."; ' +
                    '    or-teiv-yext:label 0.3.0; ' +
                    '  } ' +
                    '   ' +
                    '  augment /model:decorators { ' +
                    '        leaf urban { ' +
                    '            type string; ' +
                    '        } ' +
                    '    leaf rural { ' +
                    '            type boolean; ' +
                    '        } ' +
                    '    leaf weekend { ' +
                    '            type uint32; ' +
                    '        } ' +
                    '    } ' +
                    '   ' +
                    '  identity geo-classifier { ' +
                    '    base model:classifiers; ' +
                    '  } ' +
                    '  identity classifierTest3 { ' +
                    '        base model:classifiers; ' +
                    '  } ' +
                    '   ' +
                    '}'))))
        }
        response {
            status CONFLICT()
            body('''{
                "status": "CONFLICT",
                "message": "Schema in deleting state",
                "details": "Schema test-app-in-deleting-status already exists and is in the process of being deleted. This may take some time, please try again later."
            }''')
        }
    }
]
