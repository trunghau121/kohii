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

package kohii.core

import android.view.ViewGroup

// Playback whose Container is also the Renderer.
// This Playback will request the Playable to setup the Renderer as soon as it is active, and
// release the Renderer as soon as it is inactive.
internal class StaticViewRendererPlayback(
  manager: Manager,
  host: Host,
  config: Config,
  container: ViewGroup
) : Playback(manager, host, config, container) {

  override fun onActive() {
    super.onActive()
    rendererHolder?.shouldRequestRenderer(this)
  }

  override fun onInActive() {
    super.onInActive()
    rendererHolder?.shouldReleaseRenderer(this)
  }

  override fun <RENDERER : Any> attachRenderer(renderer: RENDERER?) {
    require(renderer == null || renderer == container)
  }

  override fun <RENDERER : Any> detachRenderer(renderer: RENDERER?): Boolean {
    require(renderer == null || renderer == container)
    return false
  }
}
