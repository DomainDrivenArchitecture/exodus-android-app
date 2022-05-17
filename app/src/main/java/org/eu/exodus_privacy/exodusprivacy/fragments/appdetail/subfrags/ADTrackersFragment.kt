package org.eu.exodus_privacy.exodusprivacy.fragments.appdetail.subfrags

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.eu.exodus_privacy.exodusprivacy.R
import org.eu.exodus_privacy.exodusprivacy.databinding.FragmentADTrackersBinding
import org.eu.exodus_privacy.exodusprivacy.fragments.appdetail.AppDetailViewModel

@AndroidEntryPoint
class ADTrackersFragment : Fragment(R.layout.fragment_a_d_trackers) {

    companion object {
        private const val trackersInfoPage =
            "https://reports.exodus-privacy.eu.org/en/info/trackers/"
    }

    private var _binding: FragmentADTrackersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentADTrackersBinding.bind(view)

        viewModel.app.observe(viewLifecycleOwner) { app ->
            binding.apply {
                if (app.exodusTrackers.isEmpty()) {
                    trackersRV.visibility = View.GONE
                    if (app.exodusVersionCode == 0L) {
                        trackersStatusTV.text = getString(R.string.analysed)
                    }
                } else {
                    trackersStatusTV.text = getString(R.string.code_signature_found)
                }
                trackersChip.apply {
                    val trackerNum = app.exodusTrackers.size
                    text = if (app.exodusVersionCode == 0L) "?" else trackerNum.toString()
                    setExodusColor(trackerNum)
                }
                trackersLearnTV.apply {
                    isClickable = true
                    setOnClickListener {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(
                            view.context,
                            Uri.parse(trackersInfoPage)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Chip.setExodusColor(size: Int) {
        val colorRed = ContextCompat.getColor(context, R.color.colorRedLight)
        val colorYellow = ContextCompat.getColor(context, R.color.colorYellow)
        val colorGreen = ContextCompat.getColor(context, R.color.colorGreen)

        val colorStateList = when (size) {
            0 -> ColorStateList.valueOf(colorGreen)
            in 1..4 -> ColorStateList.valueOf(colorYellow)
            else -> ColorStateList.valueOf(colorRed)
        }
        this.chipBackgroundColor = colorStateList
    }
}
