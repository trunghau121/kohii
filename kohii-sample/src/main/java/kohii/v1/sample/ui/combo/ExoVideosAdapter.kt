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

package kohii.v1.sample.ui.combo

import android.net.Uri
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.exoplayer2.Player
import com.google.android.material.snackbar.Snackbar
import kohii.core.Master
import kohii.core.Playback
import kohii.core.Playback.PlaybackListener
import kohii.media.MediaItem
import kohii.v1.sample.data.DrmItem
import kohii.v1.sample.data.Item

class ExoVideosAdapter(
  val kohii: Master,
  private val items: List<Item>,
  private val onClick: ((ExoVideoHolder, Int) -> Unit)? = null,
  private val onLoad: ((ExoVideoHolder, Int) -> Unit)? = null
) : Adapter<ExoVideoHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ExoVideoHolder {
    val holder = ExoVideoHolder(parent)
    holder.playerContainer.setOnClickListener {
      if (holder.adapterPosition >= 0) {
        onClick?.invoke(holder, holder.adapterPosition)
      }
    }
    return holder
  }

  override fun getItemCount() = Int.MAX_VALUE

  override fun onBindViewHolder(
    holder: ExoVideoHolder,
    position: Int
  ) {
    val item = items[position % items.size]
    holder.videoTitle.text = item.name

    val drmItem = item.drmScheme?.let { DrmItem(item) }
    val mediaItem = MediaItem(Uri.parse(item.uri), item.extension, drmItem)
    val itemTag = "${javaClass.canonicalName}::${item.uri}::${holder.adapterPosition}"

    holder.rebinder = kohii.setUp(mediaItem)
        .with {
          tag = itemTag
          // preLoad = false
          repeatMode = Player.REPEAT_MODE_ONE
        }
        .bind(holder.playerContainer) {
          onLoad?.invoke(holder, position)
          it.addPlaybackListener(object : PlaybackListener {

            override fun beforePlay(playback: Playback<*>) {
              holder.thumbnail.isVisible = false
            }

            override fun afterPause(playback: Playback<*>) {
              holder.thumbnail.isVisible = true
            }

            override fun onVideoSizeChanged(
              playback: Playback<*>,
              width: Int,
              height: Int,
              unAppliedRotationDegrees: Int,
              pixelWidthHeightRatio: Float
            ) {
              holder.aspectRatio = width / height.toFloat()
              holder.playerContainer.setAspectRatio(holder.aspectRatio)
              it.removePlaybackListener(this)
            }

            override fun onError(
              playback: Playback<*>,
              exception: Exception
            ) {
              Snackbar.make(
                  playback.container,
                  exception.localizedMessage ?: "Error",
                  Snackbar.LENGTH_LONG
              )
                  .show()
            }
          })
        }
  }
}
