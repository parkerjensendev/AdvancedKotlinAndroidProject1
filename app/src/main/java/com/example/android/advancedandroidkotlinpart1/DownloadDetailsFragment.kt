package com.example.android.advancedandroidkotlinpart1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.android.advancedandroidkotlinpart1.databinding.FragmentDownloadDetailsBinding
const val FILE_NAME = "fileName"
const val STATUS = "status"

/**
 * A simple [Fragment] subclass.
 * Use the [DownloadDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadDetailsFragment : Fragment() {
    lateinit var downloadStatus: DownloadStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            downloadStatus = DownloadStatus(it.getString(STATUS, ""), it.getString(FILE_NAME, ""))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDownloadDetailsBinding.inflate(inflater)
        binding.downloadStatus = downloadStatus

        binding.okButton.setOnClickListener {
            this.view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DownloadDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DownloadDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(FILE_NAME, param1)
                    putString(STATUS, param2)
                }
            }
    }
}

class DownloadStatus(val status: String,  val title: String)