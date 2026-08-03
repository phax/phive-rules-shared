/*
 * Copyright (C) 2024-2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phive.rules.shared;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.Immutable;
import com.helger.diver.api.coord.DVRCoordinate;
import com.helger.io.resource.IReadableResource;
import com.helger.phive.api.executorset.IValidationExecutorSet;
import com.helger.phive.api.executorset.IValidationExecutorSetRegistry;
import com.helger.phive.api.source.IValidationSource;
import com.helger.phive.xml.schematron.SchematronNamespaceBeautifier;
import com.helger.phive.xml.schematron.ValidationExecutorSchematron;
import com.helger.xml.namespace.IIterableNamespaceContext;

/**
 * Utility class for phive-rules libs.
 *
 * @author Philip Helger
 */
@Immutable
public final class PhiveRulesHelper
{
  private PhiveRulesHelper ()
  {}

  @NonNull
  public static ValidationExecutorSchematron createXSLT (@NonNull final IReadableResource aRes,
                                                         @Nullable final IIterableNamespaceContext aNsCtx)
  {
    SchematronNamespaceBeautifier.addMappings (aNsCtx);
    return ValidationExecutorSchematron.createXSLT (aRes, null, aNsCtx);
  }

  @NonNull
  public static <T extends IValidationSource> IValidationExecutorSet <T> requireVESID (@NonNull final IValidationExecutorSetRegistry <T> aRegistry,
                                                                                       @NonNull final DVRCoordinate aCoord)
  {
    final var ret = aRegistry.getOfID (aCoord);
    if (ret == null)
      throw new PhiveRulesInitializationException (aCoord);
    return ret;
  }
}
