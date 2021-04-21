package com.rohitthebest.extractcolorsfromimage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rohitthebest.extractcolorsfromimage.databinding.ActivityMainBinding
import com.rohitthebest.extractcolorsfromimage.databinding.MainActivityLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

private const val TAG = "MainActivity_tag"

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var includeBinding: MainActivityLayoutBinding

    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        includeBinding = binding.include

        includeBinding.currentSeekbarValueTV.text = includeBinding.seekBar.progress.toString()

        imageUrl = "https://picsum.photos/${Random.nextInt(500, 720)}"

        getImageAndPassForExtractingImage()

        includeBinding.refreshFAB.setOnClickListener {

            applicationContext.clearAppCache()

            imageUrl = "https://picsum.photos/${Random.nextInt(500, 720)}"

            getImageAndPassForExtractingImage()
        }

        includeBinding.seekBar.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        includeBinding.currentSeekbarValueTV.text = progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

        getImageAndPassForExtractingImage()
    }

    private fun getImageAndPassForExtractingImage() {

        Log.d(TAG, "getImageAndPassForExtractingImage: ImageUrl : $imageUrl")

        CoroutineScope(Dispatchers.Main).launch {

            extractColorsFromTheImage(
                applicationContext,
                imageUrl = imageUrl,
                includeBinding.seekBar.progress
            )
        }

    }

    private fun Context.clearAppCache() {

        this.cacheDir.deleteRecursively()
        Log.i(TAG, "clearAppCache: Cleared Successfully")
    }

    private suspend fun extractColorsFromTheImage(
        context: Context,
        imageUrl: String,
        maximumColorCount: Int = 100
    ) {


        withContext(Dispatchers.IO) {

            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    @SuppressLint("SetTextI18n")
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {

                        includeBinding.imageView.setImageBitmap(resource)

                        val paletteBuilder = Palette.Builder(resource)

                        paletteBuilder.maximumColorCount(maximumColorCount).generate { palette ->

                            val vibrantSwatch = palette?.vibrantSwatch
                            val darkVibrantSwatch = palette?.darkVibrantSwatch
                            val lightVibrantSwatch = palette?.lightVibrantSwatch
                            val mutedSwatch = palette?.mutedSwatch
                            val darkMutedSwatch = palette?.darkMutedSwatch
                            val lightMutedSwatch = palette?.lightMutedSwatch

                            Log.d(TAG, "onResourceReady: vibrantSwatch : $vibrantSwatch")
                            Log.d(TAG, "onResourceReady: darkVibrantSwatch : $darkVibrantSwatch")
                            Log.d(TAG, "onResourceReady: lightVibrantSwatch : $lightVibrantSwatch")
                            Log.d(TAG, "onResourceReady: mutedSwatch : $mutedSwatch")
                            Log.d(TAG, "onResourceReady: darkMutedSwatch : $darkMutedSwatch")
                            Log.d(TAG, "onResourceReady: lightMutedSwatch : $lightMutedSwatch")

                            if (vibrantSwatch != null) {

                                includeBinding.vibrantSwatchCV.setCardBackgroundColor(vibrantSwatch.rgb)

                                includeBinding.vibrantSwatch.text =
                                    "Vibrant Swatch : ${vibrantSwatch.rgb}"

                                includeBinding.vibrantSwatchTitleTV.text =
                                    "Title : ${vibrantSwatch.titleTextColor}"
                                includeBinding.vibrantSwatchBodyTV.text =
                                    "Body : ${vibrantSwatch.bodyTextColor}"

                                includeBinding.vibrantSwatchTitleTV.setTextColor(vibrantSwatch.titleTextColor)
                                includeBinding.vibrantSwatchBodyTV.setTextColor(vibrantSwatch.bodyTextColor)
                            } else {

                                includeBinding.vibrantSwatchTitleTV.text = "null"
                                includeBinding.vibrantSwatchBodyTV.text = "null"
                                includeBinding.vibrantSwatch.text =
                                    "Vibrant Swatch : null"

                            }
//------------
                            if (darkVibrantSwatch != null) {

                                includeBinding.darkVibrantSwatch.text =
                                    "Dark Vibrant Swatch : ${darkVibrantSwatch.rgb}"

                                includeBinding.darkVibrantTitleTV.text =
                                    "Title : ${darkVibrantSwatch.titleTextColor}"
                                includeBinding.darkVibrantBodyTextTV.text =
                                    "Body : ${darkVibrantSwatch.bodyTextColor}"


                                includeBinding.darkVibrantSwatchCV.setCardBackgroundColor(
                                    darkVibrantSwatch.rgb
                                )
                                includeBinding.darkVibrantTitleTV.setTextColor(darkVibrantSwatch.titleTextColor)
                                includeBinding.darkVibrantBodyTextTV.setTextColor(darkVibrantSwatch.bodyTextColor)
                            } else {

                                includeBinding.darkVibrantTitleTV.text = "null"
                                includeBinding.darkVibrantBodyTextTV.text = "null"
                                includeBinding.darkVibrantSwatch.text =
                                    "Dark Vibrant Swatch : null"

                            }
//----------------
                            if (lightVibrantSwatch != null) {

                                includeBinding.lightVibrantSwatch.text =
                                    "Light Vibrant Swatch : ${lightVibrantSwatch.rgb}"

                                includeBinding.lightVibrantSwatchTitleTV.text =
                                    "Title : ${lightVibrantSwatch.titleTextColor}"
                                includeBinding.lightVibrantSwatchBodyTextTV.text =
                                    "Body : ${lightVibrantSwatch.bodyTextColor}"


                                includeBinding.lightVibrantSwatchCV.setCardBackgroundColor(
                                    lightVibrantSwatch.rgb
                                )
                                includeBinding.lightVibrantSwatchTitleTV.setTextColor(
                                    lightVibrantSwatch.titleTextColor
                                )
                                includeBinding.lightVibrantSwatchBodyTextTV.setTextColor(
                                    lightVibrantSwatch.bodyTextColor
                                )
                            } else {

                                includeBinding.lightVibrantSwatchTitleTV.text = "null"
                                includeBinding.lightVibrantSwatchTitleTV.text = "null"
                                includeBinding.lightVibrantSwatch.text =
                                    "Light Vibrant Swatch : null"

                            }

//-----------------
                            if (mutedSwatch != null) {

                                includeBinding.mutedSwatch.text =
                                    "Muted Swatch : ${mutedSwatch.rgb}"

                                includeBinding.mutedSwatchTitleTextTV.text =
                                    "Title : ${mutedSwatch.titleTextColor}"
                                includeBinding.mutedSwatchBodyTextTV.text =
                                    "Body : ${mutedSwatch.bodyTextColor}"


                                includeBinding.mutedSwatchCV.setCardBackgroundColor(
                                    mutedSwatch.rgb
                                )
                                includeBinding.mutedSwatchTitleTextTV.setTextColor(mutedSwatch.titleTextColor)
                                includeBinding.mutedSwatchBodyTextTV.setTextColor(mutedSwatch.bodyTextColor)
                            } else {

                                includeBinding.mutedSwatchTitleTextTV.text = "null"
                                includeBinding.mutedSwatchBodyTextTV.text = "null"
                                includeBinding.mutedSwatch.text =
                                    "Muted Swatch : null"

                            }
//-------------------
                            if (lightMutedSwatch != null) {

                                includeBinding.lightMutedSwatch.text =
                                    "Light Muted Swatch : ${lightMutedSwatch.rgb}"

                                includeBinding.lightMutedSwatchTitleTextTV.text =
                                    "Title : ${lightMutedSwatch.titleTextColor}"
                                includeBinding.lightMutedSwatchBodyTextTV.text =
                                    "Body : ${lightMutedSwatch.bodyTextColor}"


                                includeBinding.lightMutedSwatchCV.setCardBackgroundColor(
                                    lightMutedSwatch.rgb
                                )
                                includeBinding.lightMutedSwatchTitleTextTV.setTextColor(
                                    lightMutedSwatch.titleTextColor
                                )
                                includeBinding.lightMutedSwatchBodyTextTV.setTextColor(
                                    lightMutedSwatch.bodyTextColor
                                )
                            } else {

                                includeBinding.lightMutedSwatchTitleTextTV.text = "null"
                                includeBinding.lightMutedSwatchBodyTextTV.text = "null"
                                includeBinding.lightMutedSwatch.text =
                                    "Light Muted Swatch : null"

                            }
//-------------------
                            if (darkMutedSwatch != null) {

                                includeBinding.darkMutedSwatch.text =
                                    "Dark Muted Swatch : ${darkMutedSwatch.rgb}"

                                includeBinding.darkMutedSwatchTitleTextTV.text =
                                    "Title : ${darkMutedSwatch.titleTextColor}"
                                includeBinding.darkMutedSwatchBodyTextTV.text =
                                    "Body : ${darkMutedSwatch.bodyTextColor}"


                                includeBinding.darkMutedSwatchCV.setCardBackgroundColor(
                                    darkMutedSwatch.rgb
                                )
                                includeBinding.darkMutedSwatchTitleTextTV.setTextColor(
                                    darkMutedSwatch.titleTextColor
                                )
                                includeBinding.darkMutedSwatchBodyTextTV.setTextColor(
                                    darkMutedSwatch.bodyTextColor
                                )
                            } else {

                                includeBinding.darkMutedSwatchBodyTextTV.text = "null"
                                includeBinding.darkMutedSwatchTitleTextTV.text = "null"
                                includeBinding.darkMutedSwatch.text =
                                    "Dark Muted Swatch : null"

                            }
//-------------------

                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        //TODO("Not yet implemented")
                    }
                })

        }
    }

}