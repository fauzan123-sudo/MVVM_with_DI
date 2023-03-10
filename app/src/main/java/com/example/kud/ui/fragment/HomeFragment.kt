package com.example.kud.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kud.R
import com.example.kud.data.adapter.AdapterBanner
import com.example.kud.data.adapter.AdapterData
import com.example.kud.data.adapter.AdapterKategori
import com.example.kud.data.model.DataXXX
import com.example.kud.databinding.FragmentHomeBinding
import com.example.kud.ui.base.BaseFragment
import com.example.kud.ui.viewModel.HomeViewModel
import com.example.kud.utils.NetworkResult
import com.example.kud.utils.TokenManager
import com.example.kud.utils.handleApiError
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    RecyclerViewClickListener {

    @Inject
    lateinit var tokenManager: TokenManager

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapterData: AdapterData
    private lateinit var adapterCategorise: AdapterKategori

    private var mList = mutableListOf<String>()
    private lateinit var recyclerviewItems: RecyclerView

    lateinit var imageUrl: ArrayList<String>

    lateinit var sliderView: SliderView

    lateinit var adapterBanner: AdapterBanner


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderView = binding.imageSlider

        imageUrl = ArrayList()

        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Ffull-stack-node-thumbnail.png&w=1920&q=75") as ArrayList<String>

        adapterBanner = AdapterBanner( imageUrl)

        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        sliderView.setSliderAdapter(adapterBanner)


        sliderView.scrollTimeInSec = 3

        sliderView.isAutoCycle = true

        sliderView.startAutoCycle()

        adapterData = AdapterData(requireContext())
        adapterCategorise = AdapterKategori()

        adapterData.listener = this

        recyclerviewItems = binding.recItemsHome
        recyclerviewItems.adapter = adapterData
        recyclerviewItems.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.edtSearch.setOnClickListener {

        }

        binding.imageView2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_checkOutFragment)
        }

        viewModel.drug()
        viewModel.getDrug.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val status = it.data!!.status
                    if (status == 200) {
                        val data = it.data.data
                        adapterData.differ.submitList(data)
                        mList.add(it.toString())
                    } else {
                        handleApiError(status.toString())
                    }
                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }


    }

    private fun pindah() {
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
    }

    override fun onItemClicked(view: View, data: DataXXX) {
        val dataName = data.nama
        val dataCreateBy = data.created_by
        val dataCreateAt = data.created_at
        val dataUpdateAt = data.updated_at
        val dataUpdateBy = data.updated_by
        val dataImage = data.foto
        val dataPrice = data.harga
        val dataDetail = data.deskripsi
        val stockDetail = data.stok
        val idCategory = data.id_kategori
        val idDrug = data.id_obat
        val sendData = DataXXX(
            dataCreateAt,
            dataCreateBy,
            dataDetail,
            dataImage,
            dataPrice,
            idCategory,
            idDrug,
            dataName,
            stockDetail,
            dataUpdateAt,
            dataUpdateBy
        )
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(sendData)
        findNavController().navigate(action)
    }
}