/**
 * Copyright 2016 Smoke Turner, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smoketurner.notification.api;

import java.util.Objects;
import javax.annotation.concurrent.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import io.dropwizard.jackson.JsonSnakeCase;
import io.dropwizard.util.Duration;

@Immutable
@JsonSnakeCase
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Rule {

    public static final String MAX_SIZE = "max_size";
    public static final String MAX_DURATION = "max_duration";
    public static final String MATCH_ON = "match_on";

    private final Integer maxSize;
    private final Duration maxDuration;
    private final String matchOn;

    /**
     * Constructor
     *
     * @param maxSize
     *            Maximum number of notifications to include in a roll-up
     * @param maxDuration
     *            Maximum time duration between the first and last notifications
     *            in a roll-up
     * @param matchOn
     *            Group notifications by a specific category
     */
    @JsonCreator
    private Rule(@JsonProperty(MAX_SIZE) final Optional<Integer> maxSize,
            @JsonProperty(MAX_DURATION) final Optional<String> maxDuration,
            @JsonProperty(MATCH_ON) final Optional<String> matchOn) {
        this.maxSize = maxSize.orNull();
        if (maxDuration.isPresent()) {
            this.maxDuration = Duration.parse(maxDuration.get());
        } else {
            this.maxDuration = null;
        }
        this.matchOn = matchOn.orNull();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer maxSize;
        private Duration maxDuration;
        private String matchOn;

        public Builder withMaxSize(final Integer maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder withMaxDuration(final Duration maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public Builder withMatchOn(final String matchOn) {
            this.matchOn = matchOn;
            return this;
        }

        public Rule build() {
            final String duration = (maxDuration != null)
                    ? maxDuration.toString() : null;
            return new Rule(Optional.fromNullable(maxSize),
                    Optional.fromNullable(duration),
                    Optional.fromNullable(matchOn));
        }
    }

    @JsonProperty(MAX_SIZE)
    public Optional<Integer> getMaxSize() {
        return Optional.fromNullable(maxSize);
    }

    @JsonIgnore
    public Optional<Duration> getMaxDuration() {
        return Optional.fromNullable(maxDuration);
    }

    @JsonProperty(MAX_DURATION)
    public Optional<String> getMaxDurationAsString() {
        if (maxDuration == null) {
            return Optional.absent();
        }
        return Optional.of(maxDuration.toString());
    }

    @JsonProperty(MATCH_ON)
    public Optional<String> getMatchOn() {
        return Optional.fromNullable(matchOn);
    }

    @JsonIgnore
    public boolean isValid() {
        return (maxSize != null || maxDuration != null || matchOn != null);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final Rule other = (Rule) obj;
        return Objects.equals(maxSize, other.maxSize)
                && Objects.equals(maxDuration, other.maxDuration)
                && Objects.equals(matchOn, other.matchOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxSize, maxDuration, matchOn);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("maxSize", maxSize)
                .add("maxDuration", maxDuration).add("matchOn", matchOn)
                .toString();
    }
}