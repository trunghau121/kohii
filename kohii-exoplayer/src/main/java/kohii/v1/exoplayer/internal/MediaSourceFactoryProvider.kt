/*
 * Copyright (c) 2019 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.v1.exoplayer.internal

import com.google.android.exoplayer2.source.ads.AdsMediaSource
import kohii.v1.media.Media

/**
 * @author eneim (2018/10/27).
 */
interface MediaSourceFactoryProvider {

  fun provideMediaSourceFactory(media: Media): AdsMediaSource.MediaSourceFactory
}
