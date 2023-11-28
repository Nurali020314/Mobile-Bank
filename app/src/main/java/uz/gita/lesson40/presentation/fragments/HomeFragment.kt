package uz.gita.lesson40.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.lesson40.R
import uz.gita.lesson40.databinding.FragmentHomeBinding
import uz.gita.lesson40.domain.entity.getResponse.Data
import uz.gita.lesson40.presentation.HomeViewModel
import uz.gita.lesson40.presentation.adapter.CardAdapter

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()
    private val dataList: ArrayList<Data> by lazy { ArrayList() }
    private val adapter by lazy { CardAdapter() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recycler)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.apply {
            recycler.adapter = adapter

            addCard.setOnClickListener {
                parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                    .addToBackStack("HomeFragment").replace(R.id.container, AddCardFragment()).commit()
            }
            send.setOnClickListener {
                parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                    .addToBackStack("HomeFragment").replace(R.id.container, TransferFragment()).commit()
            }
            pay.setOnClickListener {
                parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                    .addToBackStack("HomeFragment").replace(R.id.container, PaymentFragment()).commit()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.openSuccesFlow.collect { data ->
                dataList.clear()
                dataList.addAll(data)
                adapter.submitList(dataList)
            }
        }
        adapter.setOnClickClickListener { index ->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Deleting Card")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ ->
                    dataList.removeAt(index)
                    adapter.submitList(dataList)
                }.show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.openErrorFlow.collect { error ->
                if (error == 1) {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.openNetworkFlow.collect {
                Toast.makeText(requireContext(), "No Network", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCards()
    }
}