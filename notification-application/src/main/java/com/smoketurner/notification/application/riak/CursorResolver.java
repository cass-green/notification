/**
 * Copyright 2015 Smoke Turner, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smoketurner.notification.application.riak;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.basho.riak.client.api.cap.ConflictResolver;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.google.common.collect.ImmutableSortedSet;

public class CursorResolver implements ConflictResolver<CursorObject> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CursorResolver.class);

    @Override
    public CursorObject resolve(final List<CursorObject> siblings)
            throws UnresolvedConflictException {
        LOGGER.debug("Found {} siblings", siblings.size());
        if (siblings.size() > 1) {
            final ImmutableSortedSet<CursorObject> cursors = ImmutableSortedSet
                    .copyOf(siblings);
            return cursors.first();
        } else if (siblings.size() == 1) {
            return siblings.iterator().next();
        } else {
            return null;
        }
    }
}