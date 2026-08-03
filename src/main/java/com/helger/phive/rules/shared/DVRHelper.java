/*
 * Copyright (C) 2026 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonempty;
import com.helger.annotation.concurrent.Immutable;
import com.helger.diver.api.coord.DVRCoordinate;
import com.helger.diver.api.version.DVRVersionException;

/**
 * Utility class for phive-rules libs.
 *
 * @author Philip Helger
 */
@Immutable
public final class DVRHelper
{
  private DVRHelper ()
  {}

  /**
   * Create coordinates, capturing version parsing exceptions
   *
   * @param sGroupID
   *        Coordinate group ID
   * @param sArtifactID
   *        Coordinate artifact ID
   * @param sVersion
   *        Coordinate version
   * @return The created {@link DVRCoordinate} and never <code>null</code>.
   */
  @NonNull
  public static DVRCoordinate createCoordinate (@NonNull @Nonempty final String sGroupID,
                                                @NonNull @Nonempty final String sArtifactID,
                                                @NonNull @Nonempty final String sVersion)
  {
    return createCoordinate (sGroupID, sArtifactID, sVersion, null);
  }

  /**
   * Create coordinates, capturing version parsing exceptions
   *
   * @param sGroupID
   *        Coordinate group ID
   * @param sArtifactID
   *        Coordinate artifact ID
   * @param sVersion
   *        Coordinate version
   * @param sClassifier
   *        Optional coordinate classifier
   * @return The created {@link DVRCoordinate} and never <code>null</code>.
   */
  @NonNull
  public static DVRCoordinate createCoordinate (@NonNull @Nonempty final String sGroupID,
                                                @NonNull @Nonempty final String sArtifactID,
                                                @NonNull @Nonempty final String sVersion,
                                                @Nullable final String sClassifier)
  {
    try
    {
      return DVRCoordinate.create (sGroupID, sArtifactID, sVersion, sClassifier);
    }
    catch (final DVRVersionException ex)
    {
      throw new IllegalArgumentException (ex);
    }
  }
}
