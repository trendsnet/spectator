/**
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spectator.metrics2;

import com.netflix.spectator.api.AbstractTimer;
import com.netflix.spectator.api.Clock;
import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Measurement;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/** Timer implementation for the metrics2 registry. */
class MetricsTimer extends AbstractTimer {

  private final Id id;
  private final com.yammer.metrics.core.Timer impl;

  /** Create a new instance. */
  MetricsTimer(Clock clock, Id id, com.yammer.metrics.core.Timer impl) {
    super(clock);
    this.id = id;
    this.impl = impl;
  }

  @Override public Id id() {
    return id;
  }

  @Override public boolean hasExpired() {
    return false;
  }

  @Override public void record(long amount, TimeUnit unit) {
    impl.update(amount, unit);
  }

  @Override public Iterable<Measurement> measure() {
    final long now = clock.wallTime();
    return Collections.singleton(new Measurement(id, now, impl.meanRate()));
  }

  @Override public long count() {
    return impl.count();
  }

  @Override public long totalTime() {
    return (long) impl.sum();
  }
}
