/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.appmgt.impl;

import org.wso2.carbon.appmgt.api.AppManagementException;
import org.wso2.carbon.appmgt.api.model.*;
import org.wso2.carbon.appmgt.impl.dao.AppMDAO;
import org.wso2.carbon.appmgt.impl.service.ServiceReferenceHolder;
import org.wso2.carbon.appmgt.impl.utils.AppManagerUtil;
import org.wso2.carbon.appmgt.impl.utils.URLMapping;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.*;

/**
 * Factory class to create mobile apps.
 */
public class MobileAppFactory extends AppFactory {

    @Override
    public App createApp(GenericArtifact artifact, Registry registry) {

        MobileApp mobileApp = new MobileApp();

        try {

            mobileApp.setType(AppMConstants.MOBILE_ASSET_TYPE);

            String artifactPath = GovernanceUtils.getArtifactPath(registry, artifact.getId());

            mobileApp.setDescription(artifact.getAttribute(AppMConstants.API_OVERVIEW_DESCRIPTION));

            Set<String> tags = new HashSet<String>();
            org.wso2.carbon.registry.core.Tag[] tag = registry.getTags(artifactPath);
            for (org.wso2.carbon.registry.core.Tag tag1 : tag) {
                tags.add(tag1.getTagName());
            }
            mobileApp.addTags(tags);

            //Set Lifecycle status
            if (artifact.getLifecycleState() != null && artifact.getLifecycleState() != "") {
                if (artifact.getLifecycleState().toUpperCase().equalsIgnoreCase(APIStatus.INREVIEW.getStatus())) {
                    mobileApp.setLifecycleStatus(APIStatus.INREVIEW);
                } else {
                    mobileApp.setLifecycleStatus(APIStatus.valueOf(artifact.getLifecycleState().toUpperCase()));
                }
            }

            mobileApp.setCategory(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_CATEGORY));
            mobileApp.setPlatform(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_PLATFORM));
            mobileApp.setAppType(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_TYPE));
            mobileApp.setBanner(artifact.getAttribute(AppMConstants.MOBILE_APP_IMAGES_BANNER));
            if (artifact.getAttribute(AppMConstants.MOBILE_APP_IMAGES_SCREENSHOTS) != null) {
                List<String> screenShots = new ArrayList<>(Arrays.asList(artifact.getAttribute(
                        AppMConstants.MOBILE_APP_IMAGES_SCREENSHOTS).split(",")));
                mobileApp.setScreenShots(screenShots);
            }
            mobileApp.setBundleVersion(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_BUNDLE_VERSION));
            mobileApp.setPackageName(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_PACKAGE_NAME));
            mobileApp.setThumbnail(artifact.getAttribute(AppMConstants.MOBILE_APP_IMAGES_THUMBNAIL));
            mobileApp.setAppUrl(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_URL));

            mobileApp.setRecentChanges(artifact.getAttribute(AppMConstants.MOBILE_APP_OVERVIEW_RECENT_CHANGES));
            mobileApp.setCreatedTime(artifact.getAttribute(AppMConstants.API_OVERVIEW_CREATED_TIME));

        } catch (GovernanceException e) {
            String msg = "Failed to get WebApp fro artifact ";
        } catch (RegistryException e) {
            String msg = "Failed to get LastAccess time or Rating";
        }

        return mobileApp;


    }

}
