/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kogito.examples.openshift;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.kogito.examples.openshift.deployment.CliDeployer;
import org.kogito.examples.openshift.deployment.HttpDeployment;
import org.kogito.examples.openshift.deployment.OperatorDeployer;

public class OnboardingQuarkusExampleKogitoCliIntegrationTest extends OnboardingQuarkusExampleTestBase {

    private static HttpDeployment onboardingDeployment;

    @BeforeAll
    public static void setUpOperatorAndCliDeployment() throws MalformedURLException {
        Map<String, String> payrollServiceLabels = new HashMap<>();
        payrollServiceLabels.put("taxRate", "process");
        payrollServiceLabels.put("vacationDays", "process");
        payrollServiceLabels.put("paymentDate", "process");

        Map<String, String> hrServiceLabels = new HashMap<>();
        hrServiceLabels.put("department", "process");
        hrServiceLabels.put("id", "process");
        hrServiceLabels.put("employeeValidation", "process");

        OperatorDeployer.deployKogitoOperator(project);
        onboardingDeployment = CliDeployer.deployKaasUsingS2iAndWait(project, ONBOARDING_DEPLOYMENT_NAME, new URL(ASSETS_URL), ONBOARDING_GIT_CONTEXT_DIR, Collections.singletonMap("NAMESPACE", project.getName()), Collections.singletonMap("onboarding", "process"));
        CliDeployer.deployKaasUsingS2iAndWait(project, PAYROLL_DEPLOYMENT_NAME, new URL(ASSETS_URL), PAYROLL_GIT_CONTEXT_DIR, new HashMap<>(), payrollServiceLabels);
        CliDeployer.deployKaasUsingS2iAndWait(project, HR_DEPLOYMENT_NAME, new URL(ASSETS_URL), HR_GIT_CONTEXT_DIR, new HashMap<>(), hrServiceLabels);
    }

    @Override
    protected HttpDeployment getKogitoDeployment() {
        return onboardingDeployment;
    }
}
